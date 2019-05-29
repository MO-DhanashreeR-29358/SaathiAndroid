package com.mosl.app.core.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

class Dispatchers(
    @JvmField val io: Scheduler,
    @JvmField val computation: Scheduler,
    @JvmField val main: Scheduler,
    @JvmField val db: Scheduler
)

@JvmField
val RxDispatchers = Dispatchers(
    io = Schedulers.io(),
    computation = Schedulers.computation(),
    main = AndroidSchedulers.mainThread(),
    db = Schedulers.from(Executors.newSingleThreadExecutor {
      Thread(it, "db").apply { isDaemon = true }
    })
)