package com.symverse.sct20.common.util;

public class StringHelper {

    public static String addZero(int zeroCount, String number){
        int numberOfDigits = String.valueOf(number).length();
        int numberOfLeadingZeroes = zeroCount - numberOfDigits;
        StringBuilder sb = new StringBuilder();
        if (numberOfLeadingZeroes > 0) {
            for (int i = 0; i < numberOfLeadingZeroes; i++) {
                sb.append("0");
            }
        }
        sb.append(number);
        return sb.toString();
    }

    public static String addZero(int zeroCount, Integer number){
        return addZero(zeroCount, String.valueOf(number));
    }

    public static String prefixHex(String content){
        return "0x" + content;
    }

    public static String removePrefixHex(String content){
        return "0x" + content;
    }

    public static String toHexDecimal(int arg) {
        return Integer.toHexString(arg);
    }

    public static String asciiToHex(String asciiStr) {
        char[] chars = asciiStr.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char ch : chars) {
            hex.append(toHexDecimal((int) ch));
        }

        return hex.toString();
    }

    public static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

}
