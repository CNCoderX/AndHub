package com.cncoderx.andhub.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author cncoderx
 */

public class Permissions implements Parcelable {
    public boolean admin;
    public boolean push;
    public boolean pull;

    protected Permissions(Parcel in) {
        admin = in.readByte() != 0;
        push = in.readByte() != 0;
        pull = in.readByte() != 0;
    }

    public static final Creator<Permissions> CREATOR = new Creator<Permissions>() {
        @Override
        public Permissions createFromParcel(Parcel in) {
            return new Permissions(in);
        }

        @Override
        public Permissions[] newArray(int size) {
            return new Permissions[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (admin ? 1 : 0));
        dest.writeByte((byte) (push ? 1 : 0));
        dest.writeByte((byte) (pull ? 1 : 0));
    }
}
