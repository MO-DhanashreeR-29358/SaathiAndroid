package com.mosl.mobile.ekyc.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pramod on 16-12-2016.
 */

public class ValidationRule {

    /**
     * this will replace space from start & end and also replace multiple space into one in mid
     * @param input
     * @return
     */
    public static String trimFull(String input){
        String newInput=input;
        if(input!=null){
            newInput = newInput.replaceAll("[ ]+", " ");
            if(input.length()==1){
                newInput = input.trim();
            }

        }
        return newInput;
    }

    public static boolean validateMobile(String input){
        //first character should b >=7
        //total length = 10

        if(MOSLCommon.isBlank(input)){
            return false;
        }
        input = input.trim();

        if(input.length()!=10){
            return false;
        }
        int start = Integer.parseInt(input.charAt(0)+"");
        if(start<7){
            return false;
        }
        return true;
    }

    public static boolean validateTel(String input){
        //total length = 11

        input = input.replace("~", "");
        if(MOSLCommon.isBlank(input)){
            return false;
        }
        input = input.trim();

        if(input.length()!=11){
            return false;
        }

        return true;
    }

    /**
     * Password must use of both upper-case and lower-case letters (case sensitivity)
     * Password must include of one or more numerical digits
     * Password must include special characters, such as @, #, $
     * @param password
     * @return
     */
    public static boolean validatePassword(String password){
        String PASSWORD_PATTERN ="((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$]).{8,12})";

        if(MOSLCommon.isBlank(password)){
            return false;
        }
        password = password.trim();

        if(password.equalsIgnoreCase("mosl_123")){
            return false;
        }
        if(password.equalsIgnoreCase("mosl@123")){
            return false;
        }

        Pattern pattern=Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher=pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean validateClientCode(String clientCode){
        String CODE_PATTERN ="^[a-zA-Z][a-zA-Z0-9]{4,9}$";

        if(MOSLCommon.isBlank(clientCode)){
            return false;
        }

        clientCode=clientCode.trim();

        /*//space not allowed
        if(clientCode.contains(" ")){
            return false;
        }

        //length between 5 to 10
        if(clientCode.length()<5 || clientCode.length()>10){
            return false;
        }
*/
        Pattern pattern=Pattern.compile(CODE_PATTERN);
        Matcher matcher=pattern.matcher(clientCode);
        return matcher.matches();
    }

    /**
     * allow only alphabet and space without any special character and number
     * @param input
     * @return
     */
    public static boolean validateOnlyAlphabetWithoutSplChar(String input){
        String CODE_PATTERN ="^[a-zA-Z ]*$";

        if(MOSLCommon.isBlank(input)){
            return false;
        }
        Pattern pattern=Pattern.compile(CODE_PATTERN);
        Matcher matcher=pattern.matcher(input);
        return matcher.matches();
    }

    /**
     * allow only alphabet and number and space without any special character and number
     * @param input
     * @return
     */
    public static boolean validateOnlyAlphabetAndNumberWithoutSplChar(String input){
        String CODE_PATTERN ="^[a-zA-Z0-9 ]*$";

        if(MOSLCommon.isBlank(input)){
            return false;
        }
        Pattern pattern=Pattern.compile(CODE_PATTERN);
        Matcher matcher=pattern.matcher(input);
        return matcher.matches();
    }

    /**
     * allow only number
     * @param input
     * @return
     */
    public static boolean validateOnlyNumber(String input){
        String CODE_PATTERN ="^[0-9 ]*$";

        if(MOSLCommon.isBlank(input)){
            return false;
        }
        Pattern pattern=Pattern.compile(CODE_PATTERN);
        Matcher matcher=pattern.matcher(input);
        return matcher.matches();
    }

    /**
     * allow only alpha numeric with space but only number not allowed
     * no spl characters allowed
     * @param input
     * @return
     */
    public static boolean validateAddress(String input){
        if(validateOnlyNumber(input)){
            return false;
        } else if (input.length() < 5) {
            return false;
        } else {
            return validateOnlyAlphabetAndNumberWithoutSplChar(input);
        }
    }

    /**
     * allow only alphabet and number and space without any special character and number
     * If user entered then It should be exact 15 digits
     * @param input
     * @return
     */
    public static boolean validateGST(String input){
        if (input.length() < 15) {
            return false;
        } else {
            return validateOnlyAlphabetAndNumberWithoutSplChar(input);
        }
    }

    /**
     * this will replace space from start & end and also replace multiple space in mid
     * @param input
     * @return
     */
    public static String trimFullWithMid(String input){
        String newInput=input;
        if(input!=null){
            newInput = newInput.replaceAll("[ ]+", "");
            if(input.length()==1){
                newInput = input.trim();
            }

        }
        return newInput;
    }
}
