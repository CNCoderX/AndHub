package com.cncoderx.github.sdk.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * @author cncoderx
 */

public class Auth implements Parcelable {
    public int id;
    public String[] scopes;
    public String token;
    public String note;
    @SerializedName("created_at")
    public Date createdAt;
    @SerializedName("updated_at")
    public Date updatedAt;
    public String fingerprint;
    public App app;
    public User user;

    protected Auth(Parcel in) {
        id = in.readInt();
        scopes = in.createStringArray();
        token = in.readString();
        note = in.readString();
        createdAt = DateParcel.readDate(in);
        updatedAt = DateParcel.readDate(in);
        fingerprint = in.readString();
        app = in.readParcelable(App.class.getClassLoader());
        user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<Auth> CREATOR = new Creator<Auth>() {
        @Override
        public Auth createFromParcel(Parcel in) {
            return new Auth(in);
        }

        @Override
        public Auth[] newArray(int size) {
            return new Auth[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeStringArray(scopes);
        dest.writeString(token);
        dest.writeString(note);
        DateParcel.writeDate(createdAt, dest);
        DateParcel.writeDate(updatedAt, dest);
        dest.writeString(fingerprint);
        dest.writeParcelable(app, flags);
        dest.writeParcelable(user, flags);
    }
}
