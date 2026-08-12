package com.framework.utils;

import java.time.LocalDate;
import java.util.Random;

/**
 * General-purpose utility methods used across the framework.
 */
public class GeneralUtil {

    private static final Random RANDOM = new Random();

    public static String intToString(int value) {
        return Integer.toString(value);
    }

    public static int stringToInt(String value) {
        return Integer.parseInt(value);
    }

    public static boolean stringEquals(String value1, String value2) {
        return value1 != null && value1.equalsIgnoreCase(value2);
    }

    public static LocalDate today() {
        return LocalDate.now();
    }

    public static LocalDate yesterday() {
        return LocalDate.now().minusDays(1);
    }

    public static LocalDate tomorrow() {
        return LocalDate.now().plusDays(1);
    }

    public static LocalDate localDate(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }

    public static String[] toCharArray(String stringArray) {
        char[] charArray = stringArray.toCharArray();
        String[] stringArrayResult = new String[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            stringArrayResult[i] = String.valueOf(charArray[i]);
        }
        return stringArrayResult;
    }

    public static int[] toIntArray(int intArray) {
        String stringInt = Integer.toString(intArray);
        char[] charArray = stringInt.toCharArray();
        int[] result = new int[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            result[i] = Integer.parseInt(String.valueOf(charArray[i]));
        }
        return result;
    }

    public static int getRandom(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }

    public static int getRandom(int[] array) {
        return array[RANDOM.nextInt(array.length)];
    }

    public static String getRandom(String[] array) {
        return array[RANDOM.nextInt(array.length)];
    }

    public static String getTimeStamp() {
        return java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("dd-MMM-yyyy_hh-mm-ss-a"));
    }
}
