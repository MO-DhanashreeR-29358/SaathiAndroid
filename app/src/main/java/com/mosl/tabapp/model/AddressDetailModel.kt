package com.mosl.tabapp.model

import com.mosl.tabapp.common.CRMAppUtil
import java.io.Serializable

/**
 * Created by Vijay on 30/03/2017.
 */
class AddressDetailModel(
    var addressTypeID: Int,
    var address1: String,
    var address2: String,
    var address3: String,
    var countryID: String = "0",
    var stateID: String = "0",
    var cityID: String = "0",
    var pincode: String,
    var isPrimary: Boolean,
    var businessUnitID: String
) : Serializable {
    val json: String
        get() {
            var json = "{"
            json += "\"AddressTypeID\": \"$addressTypeID\","
            json += "\"Address1\": \"$address1\","
            json += "\"Address2\": \"$address2\","
            if (!CRMAppUtil.isBlank(address3)) {
                json += "\"Address3\": \"$address3\","
            }
            json += "\"StateID\": \"$stateID\","
            json += "\"CountryID\": \"$countryID\","
            json += "\"CityID\": \"$cityID\","
            json += "\"Pincode\": \"$pincode\","
            json += "\"isPrimary\": \"$isPrimary\","
            json += "\"BusinessUnitID\": \"$businessUnitID\""
            json += "}"
            return json
        }
}