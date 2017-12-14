package com.cncoderx.andhub.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoderx.andhub.model.parcel.DateParceler;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * @author cncoderx
 */
public class Notification implements Parcelable {
    public String id;
    public Repository repository;
    public Subject subject;
    public String reason;
    public boolean unread;
    @SerializedName("updated_at")
    public Date updatedAt;
    @SerializedName("last_read_at")
    public Date readAt;

    protected Notification(Parcel in) {
        id = in.readString();
        repository = in.readParcelable(Repository.class.getClassLoader());
        subject = in.readParcelable(Subject.class.getClassLoader());
        reason = in.readString();
        unread = in.readByte() != 0;
        updatedAt = DateParceler.read(in);
        readAt = DateParceler.read(in);
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeParcelable(repository, flags);
        dest.writeParcelable(subject, flags);
        dest.writeString(reason);
        dest.writeByte((byte) (unread ? 1 : 0));
        DateParceler.write(updatedAt, dest);
        DateParceler.write(readAt, dest);
    }

    public static class Subject implements Parcelable {
        public String title;
        public String type;

        protected Subject(Parcel in) {
            title = in.readString();
            type = in.readString();
        }

        public static final Creator<Subject> CREATOR = new Creator<Subject>() {
            @Override
            public Subject createFromParcel(Parcel in) {
                return new Subject(in);
            }

            @Override
            public Subject[] newArray(int size) {
                return new Subject[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(title);
            dest.writeString(type);
        }
    }
}
