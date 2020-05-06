package com.mosl.tabapp.common;

import android.widget.EditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Vijay on 04/04/2017.
 */

public class CRMValidationRule {

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean validateEditText(EditText editText, TextInputLayout textInputLayout, String error) {
        if (editText.getText().toString().trim().isEmpty()) {
            textInputLayout.setError(error);
            editText.requestFocus();
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    public static boolean validateTel(String input) {
        //total length = 11

//        input = input.replace("~", "");
        if (CRMAppUtil.isBlank(input)) {
            return false;
        }
        input = input.trim();

        if (input.length() != 11) {
            return false;
        }

        return true;
    }


    public static boolean validateAadhar(String input) {
        //total length = 11

//        input = input.replace("~", "");
        if (CRMAppUtil.isBlank(input)) {
            return false;
        }
        input = input.trim();

        if (input.length() != 12) {
            return false;
        }

        return true;
    }

    //Pancard number Format
    public static boolean isValidPannumber(String pan) {
        String PAN_PATTEN = ("[A-Z]{5}[0-9]{4}[A-Z]{1}");
        Pattern pattern = Pattern.compile(PAN_PATTEN);
        Matcher matcher = pattern.matcher(pan);
        return matcher.matches();
    }

    public static boolean validateMobile(String input) {
        //first character should b >=7
        //total length = 10

        if (CRMAppUtil.isBlank(input)) {
            return false;
        }
        input = input.trim();

        return input.length() == 10;
    }

    public static boolean validateRefreeCode(String input) {
        if (CRMAppUtil.isBlank(input)) {
            return false;
        }
        input = input.trim();
        if (input.length() < 4) {
            return false;
        }

        return true;
    }

    public static boolean validateRefreeName(String input) {
        if (CRMAppUtil.isBlank(input)) {
            return false;
        }
        input = input.trim();
        if (input.length() < 3) {
            return false;
        }

        return true;
    }


}
