package com.cncoderx.github.sdk.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * @author cncoderx
 */
public class Comment implements Parcelable {
    public int id;
    public String body;
    public User user;
    @SerializedName("created_at")
    public Date createdAt;
    @SerializedName("updated_at")
    public Date updatedAt;

    public Comment() {
    }

    protected Comment(Parcel in) {
        id = in.readInt();
        body = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        createdAt = DateParcel.readDate(in);
        updatedAt = DateParcel.readDate(in);
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(body);
        dest.writeParcelable(user, flags);
        DateParcel.writeDate(createdAt, dest);
        DateParcel.writeDate(updatedAt, dest);
    }
}
