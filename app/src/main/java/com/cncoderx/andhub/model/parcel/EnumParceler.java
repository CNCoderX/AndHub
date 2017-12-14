package com.cncoderx.andhub.model.parcel;

import android.os.Parcel;
import android.text.TextUtils;

/**
 * @author cncoderx
 */
public class EnumParceler {
    public static final String INVALID_ENUM = "";

    public static <E extends Enum<E>> E read(Parcel in, Class<E> clazz) {
        String e = in.readString();
        return TextUtils.isEmpty(e) ? null : Enum.valueOf(clazz, e);
    }

    public static <E extends Enum<E>> void write(Enum<E> e, Parcel dest) {
        if (e == null) {
            dest.writeString(INVALID_ENUM);
        } else {
            dest.writeString(e.toString());
        }
    }
}
