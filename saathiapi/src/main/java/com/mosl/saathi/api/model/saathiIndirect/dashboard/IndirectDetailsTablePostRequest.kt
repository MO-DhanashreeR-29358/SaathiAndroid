package com.mosl.saathi.api.model.saathiIndirect.dashboard

import com.google.gson.annotations.SerializedName

data class IndirectDetailsTablePostRequest(

	@field:SerializedName("EmployeeCode")
	val employeeCode: String? = null,
	@field:SerializedName("frequency")
	val frequency: String? = null,
	@field:SerializedName("flt_userstatus")
	val fltUserstatus: String? = null,
	@field:SerializedName("fltUserStatus")
	val fltUserStatus: String? = null,
	@field:SerializedName("category")
	val category: String? = null,
	@field:SerializedName("Product")
	val product: Any? = null,
	@field:SerializedName("Parameter")
	val parameter: Any? = null,
	@field:SerializedName("date")
	val date: Any? = null,
	@field:SerializedName("Filter")
	val filter: Any? = null,
	@field:SerializedName("CalculatedPageP")
	val calculatedPageP: Int? = null,
	@field:SerializedName("PageSizeP")
	val pageSizeP: Int? = null,
	@field:SerializedName("TotalCountP")
	val totalCountP: Int? = null,
	@field:SerializedName("PageP")
	val pageP: Int? = null,
	@field:SerializedName("StageCode")
	val stageCode: Any? = null,
	@field:SerializedName("Days")
	val days: Any? = null,
	@field:SerializedName("flt_product")
	val fltProduct: Any? = null,
	@field:SerializedName("flt_prospect")
	val fltProspect: Any? = null,
	@field:SerializedName("flt_period")
	val fltPeriod: Any? = null,
	@field:SerializedName("rowfilter")
	val rowfilter: Any? = null,
	@field:SerializedName("colfilter")
	val colfilter: Any? = null,
	@field:SerializedName("Key1")
	val key1: Any? = null,
	@field:SerializedName("flt_Hierarchy")
	val fltHierarchy: String? = null,
	@field:SerializedName("PageSize")
	val pageSize: Int? = null,
	@field:SerializedName("Page")
	val page: Int? = null,
	@field:SerializedName("CalculatedPage")
	val calculatedPage: Int? = null,
	@field:SerializedName("TotalCount")
	val totalCount: Int? = null,







)
