package com.mosl.tabapp.dashboard.base

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.mosl.tabapp.R
import com.mosl.tabapp.R.color
import com.mosl.tabapp.R.dimen
import com.mosl.tabapp.R.drawable
import com.mosl.tabapp.R.layout
import com.mosl.tabapp.databinding.ViewCurrentQuarterBinding
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.nio.charset.Charset
import java.util.LinkedHashMap
import java.util.Objects
import java.util.Random

class CurrentQuarterLayout @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

  internal var binding: ViewCurrentQuarterBinding = DataBindingUtil.inflate(
      LayoutInflater.from(context), R.layout.view_current_quarter, this, true
  )

  override fun onFinishInflate() {
    super.onFinishInflate()
  }

  fun loadData(jsonObject: JSONObject) {
    Timber.d("load data %s", jsonObject.toString())
    binding.tableCurrentQuaterStatusFirst.removeAllViews()
    binding.tableCurrentQuaterStatusSecond.removeAllViews()
    binding.tableCurrentQuaterStatusThird.removeAllViews()
    binding.tableCurrentQuaterStatusFourth.removeAllViews()

    if (jsonObject.has("Table")) {
      renderTableData(jsonObject)
    }
    if (jsonObject.has("Table1")) {
      renderTable1Data(jsonObject)
    }
  }

  private fun renderTable1Data(jsonObject: JSONObject) {
    val jsonArray = jsonObject.getJSONArray(TABLE1)
    val jsonObj = jsonArray.getJSONObject(0)
    if (jsonObj.has(TABLE)) {
      val table = jsonObj.getString(TABLE)
      if (table.equals("4")) {
        convertToTableData(
            jsonObj, binding.tableCurrentQuaterStatusFourth
            , binding.tvCurrentQuarterStatusFourthHeading
        )
      } else if (table.equals("2")) {
        val headingName = jsonObj.getString(HEADING) ?: ""
        loadCurrentStatusAlternateTable(
            headingName, jsonArray, binding.tableCurrentQuaterStatusSecond,
            binding.tvCurrentQuarterStatusSecondHeading
        )
      }
    }
  }

  fun convertToTableData(
    jsonObj: JSONObject,
    table: TableLayout,
    tvHeading: TextView
  ) {
    val keyList = ArrayList<String>()
    val headingName = jsonObj.getString(HEADING) ?: ""
    val iter = jsonObj.keys()
    while (iter.hasNext()) {
      val columnName: String = iter.next()
          .toString()
      keyList.add(columnName)
    }
    val yAxisList = ArrayList<HashMap<String, String>>()
    for (keySet in keyList) {
      val keyValue = HashMap<String, String>()
      if ((!keySet.equals(HEADING, ignoreCase = true)) && (!keySet.equals(
              TABLE, ignoreCase = true
          ))
      ) {
        val columnData = jsonObj.getString(keySet)
        keyValue.put(keySet, columnData)
        yAxisList.add(keyValue)
      }
    }
    loadCurrentStatusTable(headingName, yAxisList, table, tvHeading)
  }

  private fun renderTableData(jsonObject: JSONObject) {
    val jsonArray = jsonObject.getJSONArray(TABLE)
    val jsonObj = jsonArray
        .getJSONObject(0)
    if (jsonObj.has(TABLE)) {
      val table = jsonObj.getString(TABLE) ?: ""
      if (table.equals("3")) {
        convertToTableData(
            jsonObj, binding.tableCurrentQuaterStatusThird,
            binding.tvCurrentQuarterStatusThirdHeading
        )
      } else if (table.equals("1")) {
        val headingName = jsonObj.getString(HEADING) ?: ""
        loadCurrentStatusAlternateTable(
            headingName, jsonArray, binding.tableCurrentQuaterStatusFirst,
            binding.tvCurrentQuarterStatusFirstHeading
        )
      }
    }
  }

  private fun loadCurrentStatusAlternateTable(
    headingName: String,
    jsonArray: JSONArray?,
    table: TableLayout,
    tvHeading: TextView
  ) {
    if (jsonArray == null) return

    val tableMap = LinkedHashMap<String, List<Map<String, String>>>()

    for (x in 0 until jsonArray.length()) {
      val jsonRowObject = jsonArray.getJSONObject(x)
      val array = ByteArray(7) // length is bounded by 7
      Random().nextBytes(array)
      val businessChannel = String(array, Charset.forName("UTF-8"))
      //  String businessChannel = jsonRowObject.getString("Heading");
      val paramNameJsonArray = jsonRowObject.names()
      val list = ArrayList<Map<String, String>>()
      for (y in 0 until paramNameJsonArray.length()) {
        val paramName = paramNameJsonArray.getString(y)
        val paramValue = jsonRowObject.getString(paramName) ?: ""
        if (!paramName.equals("Heading", ignoreCase = true) && !paramName.equals(
                "Table", ignoreCase = true
            )
        ) {
          val map = HashMap<String, String>()
          map[paramName] = paramValue
          list.add(map)
        }
      }
      tableMap[businessChannel] = list
    }

    //draw table

    //draw table
    val rowHeadingSet: Set<String> = tableMap.keys

    //header
    //header
    val headingRow = TableRow(context)
    table.addView(headingRow)
    headingRow.layoutParams = TableRow.LayoutParams(
        TableRow.LayoutParams.MATCH_PARENT,
        TableRow.LayoutParams.WRAP_CONTENT
    )
    headingRow.setBackgroundColor(resources.getColor(color.mosl_web_color))

    val headingIterator: Iterator<*> = tableMap.keys.iterator()

    if (headingIterator.hasNext()) {
      val rowHead = headingIterator.next()
          .toString()
      val mm = tableMap[rowHead]
      for (cellmap in Objects.requireNonNull(
          mm
      )!!) {
        var headingText = cellmap.keys.iterator()
            .next()
            .toString()
        headingText = headingText.replace("_".toRegex(), " ")
        val headingCell = TextView(context)
        headingRow.addView(headingCell)
        headingCell.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        headingCell.setBackgroundResource(drawable.cell_shape)
        val padding = resources.getDimension(dimen._5sdp)
            .toInt()
        headingCell.setPadding(padding, padding, padding, padding)
        //column.setTextAppearance(context, android.R.attr.textAppearanceMedium);
        headingCell.text = headingText
      }
    }

    //row data
    //row data
    for (column1Heading in rowHeadingSet) {
      val tableRow = TableRow(context)
      table.addView(tableRow)
      tableRow.layoutParams = TableRow.LayoutParams(
          TableRow.LayoutParams.MATCH_PARENT,
          TableRow.LayoutParams.WRAP_CONTENT
      )
      val rowList =
        tableMap[column1Heading]
      for (map in Objects.requireNonNull(
          rowList
      )!!) {
        val value = map.values.iterator()
            .next()
            .toString()
        val textView = TextView(context)
        tableRow.addView(textView)
        textView.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        textView.setBackgroundResource(drawable.cell_shape)
        val padding = resources.getDimension(dimen._5sdp)
            .toInt()
        textView.setPadding(padding, padding, padding, padding)
        textView.text = value
      }
    }
    tvHeading.setText(headingName)
    tvHeading.visibility = View.VISIBLE
    table.visibility = View.VISIBLE
  }

  private fun loadCurrentStatusTable(
    headingName: String,
    arrayList: ArrayList<HashMap<String, String>>,
    table: TableLayout,
    tvHeading: TextView
  ) {
    val inFla: LayoutInflater = LayoutInflater.from(context)
    if (arrayList.size > 0) {
      for (i in arrayList.indices) {
        val filterData: Map<String, String> = arrayList.get(i)
        val keySet = filterData.keys

        val value: String = arrayList.get(i)
            .get(keyName(keySet)) ?: ""
        val tableRow =
          inFla.inflate(layout.table_current_quarter_status_three, null)
        val tvKey = tableRow.findViewById<TextView>(R.id.tvKey)
        val tvValue = tableRow.findViewById<TextView>(R.id.tvValue)
        tvKey.setText(keyName(keySet).replace("_", " "))
        tvValue.text = value
        table.addView(tableRow)
      }
    }
    tvHeading.setText(headingName)
    tvHeading.setVisibility(View.VISIBLE)
    table.visibility = View.VISIBLE
  }

  private fun keyName(keySet: Set<String>?): String {
    var key: String? = ""
    if (keySet != null) {
      for (s in keySet) {
        key = s
      }
    }
    return key ?: ""
  }
}

const val TABLE = "Table"
const val TABLE1 = "Table1"
const val HEADING = "Heading"