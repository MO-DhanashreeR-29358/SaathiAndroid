package com.mosl.tabapp.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.mosl.app.core.error.ErrorModel
import com.mosl.app.core.error.getErrorOnFailure
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.views.BlockingProgressLayout
import com.mosl.app.coreui.views.ErrorLayout
import com.mosl.tabapp.R
import com.mosl.tableview.TableFixHeaders
import com.mosl.tableview.rowadapter.TableRow
import com.mosl.tableview.rowadapter.TableRowAdapter
import io.reactivex.rxjava3.subjects.PublishSubject

class TableWrapperView<T : TableRow>(
  context: Context,
  attributeSet: AttributeSet
) : FrameLayout(context, attributeSet) {
  lateinit var tableView: TableFixHeaders
  private lateinit var errorLayout: ErrorLayout
  private lateinit var progressBar: BlockingProgressLayout
  private var tableAdapter: TableRowAdapter<T>? = null

  init {
    LayoutInflater.from(getContext())
      .inflate(R.layout.view_wrapped_table, this, true)
  }

  override fun onFinishInflate() {
    super.onFinishInflate()
    tableView = findViewById(R.id.table_view)
    errorLayout = findViewById(R.id.error_layout)
    progressBar = findViewById(R.id.progress_bar)
  }

  fun setRetryListener(retryListener: OnClickListener) {
    errorLayout.retryListener = (retryListener)
  }

  fun setRetrySubject(retrySubject: PublishSubject<DataAction>) {
    errorLayout.retrySubject = retrySubject
  }

  @JvmOverloads
  fun setData(
    dataList: List<T>,
    errorModel: ErrorModel = ErrorModel.NoDataAvailable
  ) {
    tableAdapter?.setData(dataList)
    if (dataList.isEmpty()) {
      showError(errorModel)
    } else {
      scrollToTop()
    }
  }

  fun setAdapter(adapter: TableRowAdapter<T>?) {
    tableAdapter = adapter
    if (adapter != null) {
      tableView.adapter = adapter
    }
  }

  fun scrollToTop() {
    tableView.scrollTo(0, 0)
  }

  fun toggleProgress(inProgress: Boolean) {
    if (inProgress) {
      errorLayout.visibility = View.GONE
      progressBar.visibility = View.VISIBLE
      tableView.visibility = View.VISIBLE
    } else {
      progressBar.visibility = View.GONE
    }
  }

  fun showError(
    throwable: Throwable,
    sectionId: Int? = null
  ) {
    showError(throwable.getErrorOnFailure(), sectionId)
  }

  fun isRetryVisible(): Boolean {
    return errorLayout.isRetryVisible()
  }

  fun showError(
    errorModel: ErrorModel,
    sectionId: Int? = null
  ) {
    (tableView.adapter as? TableRowAdapter<*>)?.clear()
    errorLayout.showError(errorModel, sectionId)
    tableView.visibility = View.INVISIBLE
  }
}