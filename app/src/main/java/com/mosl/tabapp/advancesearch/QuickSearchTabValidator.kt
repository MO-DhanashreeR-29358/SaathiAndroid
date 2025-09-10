package com.mosl.tabapp.advancesearch

import android.content.Context
import com.mosl.mobile.domain.advanceSearch.AdvanceSearchInput
import com.mosl.validator.core.Validator
import com.mosl.validator.data.ValidationResult
import javax.inject.Inject
import android.util.Patterns

class QuickSearchTabValidator @Inject constructor(val context: Context):Validator<AdvanceSearchInput,AdvanceSearchInput>() {
    override fun execute(input: AdvanceSearchInput): ValidationResult<AdvanceSearchInput> {

        val errorMessage= when{

            (input.leadId!=null  && input.leadId.toString().length<5)->{
                "Lead ID should be more than or equal to 5 digit."
            }
            (input.mobileNo?.isNotEmpty() == true && input.mobileNo?.length!! < 10)->{
                "Mobile number should be 10 digit."
            }
            (input.emailId?.isNotEmpty() == true && !input.emailId.isValidEmail())->{
                "Please enter valid email id"
            }
            (input.aadharNo?.isNotEmpty()==true && input.aadharNo?.length!!<12)->{
                "Aadhaar number should be 12 digit."
            }
            (input.panNo?.isNotEmpty()== true && input.panNo?.length!!<10 ) ->{
                "PAN Number must contain: Minimum 10 characters, first 5 characters should be alphabets, next 4 characters should be numeric and last character should be alphabet"
            }

            (input.ucId!=null && input.ucId.toString().length<6)->{
                "UCID should be more than 6 digit."
            }
            (input.fucId!=null && input.fucId.toString().length<6)->{
                "Family UCIDshould be more than 6 digit."
            }
            (input.leadId==null && input.clientCode.isNullOrEmpty() && input.mobileNo.isNullOrEmpty() &&
                    input.emailId.isNullOrEmpty() && input.name.isNullOrEmpty() && input.aadharNo.isNullOrEmpty() &&
                    input.panNo.isNullOrEmpty() && input.ucId==null && input.fucId==null)->{
                        "Please enter any key to search"
                    }
            else -> {
                null
            }
        }
        return if (errorMessage != null) {
            withMessage(errorMessage)
        } else {
            withData( AdvanceSearchInput(
                leadId = input.leadId,
                clientCode =input.clientCode,
                mobileNo = input.mobileNo,
                emailId = input.emailId,
                name=input.name,
                aadharNo = input.aadharNo,
                panNo = input.panNo,
                ucId = input.ucId,
                fucId = input.fucId,
                searchTypeId = input.searchTypeId
            )
            )
        }
    }

    fun CharSequence?.isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

}