package com.mosl.tabapp.service.location

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mosl.app.core.network.isNetworkConnected
import com.mosl.app.core.utils.obtain
import com.mosl.mobile.domain.location.LocationRepository
import com.mosl.mobile.domain.location.LocationResponse
import com.mosl.mobile.saathi.AppComponent
import com.mosl.mobile.user.login.UserManager
import com.mosl.saathi.api.SaathiApi
import com.mosl.tabapp.SaathiAppNew
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit.MINUTES
import javax.inject.Inject

class DataSyncWorker(
  val context: Context,
  workerParams: WorkerParameters
) : Worker(context, workerParams) {
  @Inject lateinit var userManager: UserManager
  @Inject lateinit var saathiApi: SaathiApi
  @Inject lateinit var repository: LocationRepository
  protected val disposable: CompositeDisposable = CompositeDisposable()

  init {
    context.applicationContext.obtain<AppComponent>(AppComponent::class.java)
        .inject(this)
  }

  override fun doWork(): Result {

    try {
      if (userManager.isLoggedIn()) {
        if (SaathiAppNew.get().getDB()?.daoAccess()?.getLocationList()?.size != 0) {
          LocationUpdate()
        }
      }
      return Result.success()
    } catch (e: Exception) {
      e.printStackTrace()
      Timber.d(e.toString())
      return Result.retry()
    }

  }

  private fun LocationUpdate() {
    if (context.isNetworkConnected()) {
      disposable.add(repository.getGeoLocation(
          SaathiAppNew.get().getDB()?.daoAccess()?.getLocationList()!!.transformLocationData()
      ).debounce(1, MINUTES)
          .subscribeOn(Schedulers.computation())
          .subscribe { this.renderLocation(it) })
    }

  }

  private fun renderLocation(uiModel: LocationResponse) {
    uiModel.isSuccessful.let {
      SaathiAppNew.get()
          .getDB()
          ?.daoAccess()
          ?.deleteAll()
    }
  }
}