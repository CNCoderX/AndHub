package com.cncoderx.github.sdk.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * @author cncoderx
 */
public class Label implements Parcelable {
    public int id;
    public String url;
    public String name;
    public String color;
    @SerializedName("default")
    public boolean isDefault;

    protected Label(Parcel in) {
        id = in.readInt();
        url = in.readString();
        name = in.readString();
        color = in.readString();
        isDefault = in.readByte() != 0;
    }

    public static final Creator<Label> CREATOR = new Creator<Label>() {
        @Override
        public Label createFromParcel(Parcel in) {
            return new Label(in);
        }

        @Override
        public Label[] newArray(int size) {
            return new Label[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(url);
        dest.writeString(name);
        dest.writeString(color);
        dest.writeByte((byte) (isDefault ? 1 : 0));
    }
}
