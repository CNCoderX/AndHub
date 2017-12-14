package com.cncoderx.andhub.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoderx.andhub.model.parcel.DateParceler;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * @author cncoderx
 */
public class Commit implements Parcelable {
    public Contact author;
    public Contact committer;
    public String message;
    @SerializedName("comment_count")
    public int commentCount;

    protected Commit(Parcel in) {
        author = in.readParcelable(Contact.class.getClassLoader());
        committer = in.readParcelable(Contact.class.getClassLoader());
        message = in.readString();
        commentCount = in.readInt();
    }

    public static final Creator<Commit> CREATOR = new Creator<Commit>() {
        @Override
        public Commit createFromParcel(Parcel in) {
            return new Commit(in);
        }

        @Override
        public Commit[] newArray(int size) {
            return new Commit[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(author, flags);
        dest.writeParcelable(committer, flags);
        dest.writeString(message);
        dest.writeInt(commentCount);
    }

    public static class Contact implements Parcelable {
        public String name;
        public String email;
        public Date date;

        protected Contact(Parcel in) {
            name = in.readString();
            email = in.readString();
            date = DateParceler.read(in);
        }

        public static final Creator<Contact> CREATOR = new Creator<Contact>() {
            @Override
            public Contact createFromParcel(Parcel in) {
                return new Contact(in);
            }

            @Override
            public Contact[] newArray(int size) {
                return new Contact[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeString(email);
            DateParceler.write(date, dest);
        }
    }
}
