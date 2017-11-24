package com.cncoderx.github.sdk.model;

import android.os.Parcel;

import java.util.Date;

/**
 * @author cncoderx
 */
public class DateParcel {
    public static final long INVALID_TIME = -1;

    public static Date readDate(Parcel in) {
        long time = in.readLong();
        return time == INVALID_TIME ? null : new Date(time);
    }

    public static void writeDate(Date date, Parcel dest) {
        if (date == null) {
            dest.writeLong(INVALID_TIME);
        } else {
            dest.writeLong(date.getTime());
        }
    }
}
