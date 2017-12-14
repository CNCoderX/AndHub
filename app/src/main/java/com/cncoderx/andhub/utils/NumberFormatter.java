package com.cncoderx.andhub.utils;

import java.util.Locale;

/**
 * @author cncoderx
 */
public class NumberFormatter {

    public static String format(int number) {
        if (number > 1000) {
            float f = number / 1000f;
            if (f - number / 1000 == 0) {
                return String.format(Locale.US, "%dk", (int) f);
            } else {
                return String.format(Locale.US, "%.1fk", f);
            }
        }
        return Integer.toString(number);
    }
}
