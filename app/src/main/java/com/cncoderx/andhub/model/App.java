package com.cncoderx.andhub.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * @author cncoderx
 */
public class App implements Parcelable {
    public String name;
    @SerializedName("client_id")
    public String clientId;

    protected App(Parcel in) {
        name = in.readString();
        clientId = in.readString();
    }

    public static final Creator<App> CREATOR = new Creator<App>() {
        @Override
        public App createFromParcel(Parcel in) {
            return new App(in);
        }

        @Override
        public App[] newArray(int size) {
            return new App[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(clientId);
    }
}
