package com.cncoderx.github.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cncoderx.github.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author cncoderx
 */
public class TimeFormatter {

    public static String format(Context context, @NonNull Date date) {
        Date now = new Date();
        long e = new Date().getTime() - date.getTime();
        String formatted = timeAgoFromMs(context, e);
        if (formatted != null)
            return formatted;

        SimpleDateFormat formatter;
        if (date.getYear() == now.getYear()) {
            formatter = new SimpleDateFormat(context.getString(R.string.time_format));
        } else {
            formatter = new SimpleDateFormat(context.getString(R.string.time_format_full));
        }
        return formatter.format(date);
    }

    private static String timeAgoFromMs(Context context, long e) {
        long t = Math.round(e / 1e3f);
        long n = Math.round(t / 60f);
        long r = Math.round(n / 60f);
        long o = Math.round(r / 24f);
//        long i = Math.round(o / 30f);
//        long a = Math.round(i / 12f);
        if (0 > e) {
            return context.getString(R.string.just_now);
        }
        if (10 > t) {
            return context.getString(R.string.just_now);
        }
        if (45 > t) {
            return context.getString(R.string.seconds_ago, t);
        }
        if (90 > t ) {
            return context.getString(R.string.minute_ago);
        }
        if (45 > n) {
            return context.getString(R.string.minutes_ago, n);
        }
        if (90 > n) {
            return context.getString(R.string.hour_age);
        }
        if (24 > r) {
            return context.getString(R.string.hours_ago, r);
        }
        if (36 > r) {
            return context.getString(R.string.day_ago);
        }
        if (30 > o) {
            return context.getString(R.string.days_ago, o);
        }
//        if (45 > o ) {
//            return context.getString(R.string.month_ago);
//        }
//        if (12 > i) {
//            return i + context.getString(R.string.months_ago, i);
//        }
//        if (18 > i) {
//            return context.getString(R.string.year_ago);
//        }
//        return context.getString(R.string.years_ago, a);
        return null;
    }
}
