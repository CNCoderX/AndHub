package com.cncoderx.github.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author cncoderx
 */

public class TimeUtils {

    public static String formatDate(Date date) {
        Date now = new Date();
        long e = new Date().getTime() - date.getTime();
        String formatted = timeAgoFromMs(e);
        if (formatted != null)
            return formatted;

        SimpleDateFormat formatter;
        if (date.getYear() == now.getYear()) {
            formatter = new SimpleDateFormat("d MMM", Locale.ENGLISH);
        } else {
            formatter = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
        }
        return "on " + formatter.format(date);
    }

    private static String timeAgoFromMs(long e) {
        long t = Math.round(e / 1e3f);
        long n = Math.round(t / 60f);
        long r = Math.round(n / 60f);
        long o = Math.round(r / 24f);
        long i = Math.round(o / 30f);
        long a = Math.round(i / 12f);
//        long t = Math.round(e / 1e3);
//        long n = Math.round(t / 60);
//        long r = Math.round(n / 60);
//        long o = Math.round(r / 24);
//        long i = Math.round(o / 30);
//        long a = Math.round(i / 12);
        if (0 > e) {
            return "just now";
        }
        if (10 > t) {
            return "just now";
        }
        if (45 > t) {
            return t + " seconds ago";
        }
        if (90 > t ) {
            return "a minute ago";
        }
        if (45 > n) {
            return n + " minutes ago";
        }
        if (90 > n) {
            return "an hour ago";
        }
        if (24 > r) {
            return r + " hours ago";
        }
        if (36 > r) {
            return "a day ago";
        }
        if (30 > o) {
            return o + " days ago";
        }
//        if (45 > o ) {
//            return "a month ago";
//        }
//        if (12 > i) {
//            return i + " months ago";
//        }
//        if (18 > i) {
//            return "a year ago";
//        }
//        return a + " years ago";
        return null;
    }
}
