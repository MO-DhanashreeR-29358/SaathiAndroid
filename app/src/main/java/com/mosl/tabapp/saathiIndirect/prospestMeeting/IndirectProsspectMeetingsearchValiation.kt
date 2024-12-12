package com.mosl.tabapp.saathiIndirect.prospestMeeting

import android.content.Context
import com.mosl.validator.core.Validator
import com.mosl.validator.data.ValidationResult
import javax.inject.Inject

class IndirectProsspectMeetingsearchValiation @Inject constructor(val context: Context): Validator<String, String>()
{
    override fun execute(input: String): ValidationResult<String> {
        val errorMessage=when{
                input.isNullOrEmpty() ->{
                    "Kindly enter any one fields for search.!"
                }
            else -> {
                null
            }
        }

        return if (errorMessage != null) {
            withMessage(errorMessage)
        } else {
            withData(input)
        }

    }
}