package com.mosl.tabapp.setappointment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import com.mosl.saathi.api.model.createmeeting.AutoCompleteAttendeeRequest
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.constants.MOSLConstants.ATTENDEEE_NOT_FOUND_MESSAGE
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragSetAppiontmentBinding
import com.mosl.tabapp.databinding.ItemListAddedAttendeeBinding
import com.mosl.tabapp.databinding.ItemListAddedSplAttendeeBinding
import com.mosl.tabapp.leadViewDetail.AttendeeAdapter
import com.mosl.tabapp.leadViewDetail.SetAppointmentFragment
import com.mosl.tabapp.model.AddedAttendeeModel
import com.mosl.tabapp.model.AddedSplAttendeeModel
import com.mosl.tabapp.model.SplAttendeeModel
import java.net.URLEncoder
import java.util.ArrayList
import java.util.Calendar
import java.util.Date

class SetAppointmentFragment : CommonFragment(), OnClickListener, OnEditorActionListener {

  private lateinit var binding: FragSetAppiontmentBinding

  private val arrSelectProduct = ArrayList<RowData>()
  private val arrSelectWeek = ArrayList<RowData>()
  private val arrSelectWeekDay = ArrayList<RowData>()
  private val arrActivityType = ArrayList<RowData>()
  private val arrRepeatMode = ArrayList<RowData>()

  private var addedAttendeeCount: Int = 0
  private var addedSplAttendeeCount: Int = 0
  private var str_LeadID = ""
  private var productCodeStr: String? = null
  private val addedAttendeeModelList = ArrayList<AddedAttendeeModel>()
  private var attendeeAdapter: AttendeeAdapter? = null
  private val addedSplAttendeeModelList = ArrayList<AddedSplAttendeeModel>()
  private var splAttendeeAdapter: SplAttendeeAdapter? = null
  private var weekCodeStr: String? = null
  private var weekDayCodeStr: String? = null
  private var today: Calendar? = null
  private var dialogDateTime: Dialog? = null
  private var hour: Int = 0
  private var minute: Int = 0
  private var mainFromDate: String? = null
  private var fromPostingDate: String? = null
  private var mainFromDateStartDate: String? = null
  private var fromPostingDateStartDate: String? = null
  private var hourStartDate: Int = 0
  private var minuteStartDate: Int = 0
  private var mainFromDateEndDate: String? = null
  private var fromPostingDateEndDate: String? = null
  private var hourEndDate: Int = 0
  private var minuteEndDate: Int = 0
  private var currentYear: Int = 0
  private var month: Int = 0
  private var day: Int = 0
  private var currentYearStartDate: Int = 0
  private var monthStartDate: Int = 0
  private var dayStartDate: Int = 0
  private var currentYearEndDate: Int = 0
  private var monthEndDate: Int = 0
  private var dayEndDate: Int = 0
  private var activityTypeCodeStr: String? = null
  private var fromDat: Date? = null//, toDate;
  private var meetingId: String? = ""
  private var createMode = true
  private var mUCID = "0"

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragSetAppiontmentBinding.bind(view)
  }

  override fun onClick(v: View?) {
    TODO(
        "not implemented"
    ) //To change body of created functions use File | Settings | File Templates.
  }

  override fun onEditorAction(
    v: TextView?,
    actionId: Int,
    event: KeyEvent?
  ): Boolean {
    TODO(
        "not implemented"
    ) //To change body of created functions use File | Settings | File Templates.
  }

  private fun setCount(cnt: Int) {
    binding.tvAttendeeCount.text = when {
      cnt <= 0 -> ""
      else -> String.format("(%s)", cnt.toString())
    }
  }

  companion object {
    val TAG = SetAppointmentFragment::class.java.simpleName
  }

  inner class AddedSplAttendeeAdapter private constructor(
    internal var mContext: Context,
    internal var attendeeModelList: MutableList<AddedSplAttendeeModel>
  ) : BaseAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(mContext)

    override fun getCount(): Int {
      return attendeeModelList.size
    }

    override fun getItem(position: Int): Any {
      return attendeeModelList[position]
    }

    override fun getItemId(position: Int): Long {
      return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(
      position: Int,
      view1: View,
      parent: ViewGroup
    ): View {
      var view = view1
      val holder = Holder()
      view = inflater.inflate(R.layout.item_list_added_spl_attendee, parent, false)
      val viewBinding = ItemListAddedSplAttendeeBinding.bind(view)
      val attendeeModel = attendeeModelList[position]

      holder.tvAttendeeName = viewBinding.tvAttendeeName
      holder.ivDelete = viewBinding.ivDelete

      holder.tvAttendeeName!!.text = String.format(
          "%s-%s", attendeeModel.attendeeEmplyeeCode,
          attendeeModel.attendeeName
      )
      holder.ivDelete!!.setOnClickListener { _ ->
        addedSplAttendeeCount--
        binding.tvSplAttendeeCount.text = String.format("(%s)", addedSplAttendeeCount.toString())
        attendeeModelList.removeAt(position)
        notifyDataSetChanged()
      }

      return view
    }

    internal inner class Holder {
      var tvAttendeeName: TextView? = null
      var ivDelete: ImageView? = null
    }
  }

  inner class AddedAttendeeAdapter private constructor(
    internal var mContext: Context,
    internal var attendeeModelList: MutableList<AddedAttendeeModel>
  ) : BaseAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(mContext)

    override fun getCount(): Int {
      return attendeeModelList.size
    }

    override fun getItem(position: Int): Any {
      return attendeeModelList[position]
    }

    override fun getItemId(position: Int): Long {
      return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(
      position: Int,
      view1: View,
      parent: ViewGroup
    ): View {
      var view = view1
      val holder = Holder()
      view = inflater.inflate(R.layout.item_list_added_attendee, parent, false)

      val viewBinding = ItemListAddedAttendeeBinding.bind(view)

      val attendeeModel = attendeeModelList[position]

      holder.tvAttendeeName = viewBinding.tvAttendeeName
      holder.ivDelete = viewBinding.ivDelete

      holder.tvAttendeeName!!.text = String.format(
          "%s-%s", attendeeModel.attendeeEmplyeeCode,
          attendeeModel.attendeeName
      )
      holder.ivDelete!!.setOnClickListener { _ ->
        addedAttendeeCount--
        setCount(addedAttendeeCount)
        attendeeModelList.removeAt(position)
        notifyDataSetChanged()
      }

      return view
    }

    internal inner class Holder {
      var tvAttendeeName: TextView? = null
      var ivDelete: ImageView? = null
    }
  }

  inner class SplAttendeeAdapter private constructor(internal var mContext: Context) : BaseAdapter(),
      Filterable {
    private var inflater: LayoutInflater? = null
    private var attendeeModelList: List<SplAttendeeModel> = ArrayList()

    init {
      inflater = LayoutInflater.from(mContext)
    }

    override fun getCount(): Int {
      return attendeeModelList.size
    }

    override fun getItem(position: Int): SplAttendeeModel {
      return attendeeModelList[position]
    }

    override fun getItemId(position: Int): Long {
      return position.toLong()
    }

    override fun getView(
      position: Int,
      convertView: View?,
      parent: ViewGroup
    ): View {
      var convertView = convertView
      var holderView: HolderView? = null
      val clientModel = attendeeModelList[position]
      if (convertView == null) {
        inflater = requireContext().getSystemService(
            Context.LAYOUT_INFLATER_SERVICE
        ) as LayoutInflater
        convertView = inflater!!.inflate(R.layout.item_list_attendee, parent, false)
        holderView = HolderView(convertView!!)
        convertView!!.tag = holderView
      } else {
        holderView = convertView.tag as HolderView
      }
      // viewColor(position, convertView);

      viewColor(position, convertView)
      if (clientModel.isNoData) {
        holderView.tvAttendeeName.text = clientModel.attendeeName
      } else {
        holderView.tvAttendeeName.text = String.format(
            "%s-%s-%s", clientModel.attendeeCode,
            clientModel.attendeeName, clientModel.branchCode
        )
      }

      return convertView
    }

    override fun getFilter(): Filter {
      return object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
          val filterResults = FilterResults()
          val modelList = ArrayList<SplAttendeeModel>()
          try {
            if (constraint != null) {
              if (CRMAppUtil.isNetworkEnabled(requireContext())) {
                val request = AutoCompleteAttendeeRequest(
                    URLEncoder.encode(constraint.toString(), "utf-8"),
                    URLEncoder.encode(constraint.toString(), "utf-8")
                )
                try {
                  val dataList = saathiApi.getAutoCompleteSplAttendee(request)
                      .blockingFirst()
                  for ((_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, employeeCode1, _, _, _, _, _, _, _, _, _, _, employeeName1, _, branchCode) in dataList) {
                    val employeeName = stringOrEmpty(employeeName1)
                    val employeeCode = stringOrEmpty(employeeCode1)
                    val BranchCode = stringOrEmpty(branchCode)
                    modelList.add(
                        SplAttendeeModel(
                            employeeName, employeeCode,
                            BranchCode
                        )
                    )
                    filterResults.values = modelList
                    filterResults.count = modelList.size
                  }
                } catch (e: Exception) {
                  modelList.add(
                      SplAttendeeModel.getNoData(getErrorOnFailureNew(e))
                  )
                  filterResults.values = modelList
                  filterResults.count = modelList.size
                  return filterResults
                }

              } else {
                noInternetConnectionDialog()
              }
            }
          } catch (ex: Exception) {
            ex.printStackTrace()
          }

          if (filterResults.count == 0) {
            modelList.add(SplAttendeeModel.getNoData(ATTENDEEE_NOT_FOUND_MESSAGE))
            filterResults.values = modelList
            filterResults.count = modelList.size
          }
          return filterResults
        }

        override fun publishResults(
          constraint: CharSequence,
          results: FilterResults?
        ) {
          if (results != null && results.count > 0) {
            attendeeModelList = results.values as List<SplAttendeeModel>
            notifyDataSetChanged()
          } else {
            notifyDataSetInvalidated()
          }
        }
      }
    }

    private fun viewColor(
      position: Int,
      convertView: View
    ) {
      if (position % 2 == 1) {
        convertView.setBackgroundColor(Color.parseColor("#F5F5F5"))
      } else {
        convertView.setBackgroundColor(Color.parseColor("#FFFFFF"))
      }
    }

    internal inner class HolderView internal constructor(parent: View) {
      var tvAttendeeName: TextView = parent.findViewById(R.id.tvAttendeeName)

    }
  }

}