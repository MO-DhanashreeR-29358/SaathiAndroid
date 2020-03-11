package com.mosl.tabapp.birthdayanniversary

import com.mosl.mobile.domain.birthdayanniversary.BirthdayRequestInput


sealed class BirthdayAnniversaryUiEvent {
  data class GetDataList(val request: BirthdayRequestInput) : BirthdayAnniversaryUiEvent()
}