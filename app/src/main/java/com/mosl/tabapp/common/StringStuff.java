package com.mosl.tabapp.common;

import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.Hashtable;

public class StringStuff {

    public static Hashtable substitute = new Hashtable();
    private static DecimalFormat patternINRDecorator = new DecimalFormat("######");
    private static DecimalFormat patternINRCommaDecorator = new DecimalFormat("#,##,###");
    private static DecimalFormat patternCommaDecorator = new DecimalFormat("#,##,##0.00");
    private static DecimalFormat patternMarketCommaDecorator = new DecimalFormat("#,##,##0.0000");

    static {
        substitute.put(":sqt", "'");
        substitute.put(":til", "~");
        substitute.put(":comma", ",");
        substitute.put(":amp", "&");
        substitute.put(":pipe", "|");
        substitute.put(":lt", "<");
        substitute.put(":gt", ">");
        substitute.put(":lb", "{");
        substitute.put(":rb", "}");
        substitute.put(":lsq", "[");
        substitute.put(":rsq", "]");
        substitute.put(":eq", "=");
        substitute.put(":scol", ";");
        substitute.put(":nl", "\n");
        substitute.put(":lp", "(");
        substitute.put(":rp", ")");
        substitute.put(":col", ":");
        substitute.put(":hash", "#");
    }

    public static String commaDecorator(String input) {
        if (input == null) return "";
        if (input.equals("--") || input.trim().length() == 0) return input;

        double value = toDbl(input);
        try {
            String output = patternCommaDecorator.format(value);
            if (input.startsWith("-") && !output.contains("-")) output = "-" + output;
            return output;
        } catch (Exception ignored) {
        }
        return input;
    }

    public static String commaDecoratorMarket(String input) {
        if (input == null) return "";
        if (input.equals("--") || input.trim().length() == 0) return input;

        double value = toDbl(input);
        try {
            String output = patternMarketCommaDecorator.format(value);
            if (input.startsWith("-") && !output.contains("-")) output = "-" + output;
            return output;
        } catch (Exception ignored) {
        }
        return input;
    }

    public static String withoutDecimal(String input) {
        if (input == null) return "";
        if (input.equals("--") || input.trim().length() == 0) return input;

        double value = toDbl(input);
        try {
            String output = patternINRCommaDecorator.format(value);
            if (input.startsWith("-") && !output.contains("-")) output = "-" + output;
            return output;
        } catch (Exception ignored) {
        }
        return input;
    }

    public static String commaDecoratorWithoutDecimal(String input) {
        if (input == null) return "";
        if (input.equals("--") || input.trim().length() == 0) return input;

        double value = toDbl(input);
        try {
            String output = patternINRCommaDecorator.format(value);
            if (input.startsWith("-") && !output.contains("-")) output = "-" + output;
            return output;
        } catch (Exception ignored) {
        }
        return input;
    }



    /**
     * Example:it convert String to double value
     *
     * @param str String
     * @return double
     */
    public static double toDbl(String str) {
        try {
            if (isStringParseable(str)) return Double.parseDouble(str);
        } catch (Exception e) {
            // Logger.log(e.getClass().toString() + ":NumberWorker");
        }
        return 0;
    }

    private static boolean isStringParseable(String str) {
        return (str != null) && (str.length() > 0) && (!str.equals("NA")) && (!str.equals("%") && (!str.equals("--")));
    }

    public static String formatStr(String str) {
        Enumeration keys = substitute.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            str = replaceAll(str, key, (String) substitute.get(key));
        }
        return str;
    }

    public static String replaceAll(String str, String expr, String repl) {
        int start = 0;
        int end = str.indexOf(expr);
        String newStr = "";
        while (end > -1) {
            newStr += str.substring(start, end) + repl;
            str = str.substring(end + expr.length(), str.length());
            end = str.indexOf(expr);
        }
        newStr += str;
        return newStr;
    }

}