package com.cncoderx.andhub.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoderx.andhub.model.parcel.DateParceler;
import com.cncoderx.andhub.model.parcel.EnumParceler;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Map;

/**
 * @author cncoderx
 */
public class Event implements Parcelable {
    public String id;
    public EventType type;
    @SerializedName("public")
    public boolean isPublic;
    public Map<String, Object> payload;
    public Repo repo;
    public User actor;
    public User org;
    @SerializedName("created_at")
    public Date createdAt;

    public Event() {
    }

    protected Event(Parcel in) {
        id = in.readString();
        type = EnumParceler.read(in, EventType.class);
        isPublic = in.readByte() != 0;
//        payload = in.readParcelable(Payload.class.getClassLoader());
        repo = in.readParcelable(Repo.class.getClassLoader());
        actor = in.readParcelable(User.class.getClassLoader());
        org = in.readParcelable(User.class.getClassLoader());
        createdAt = DateParceler.read(in);
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        EnumParceler.write(type, dest);
        dest.writeByte((byte) (isPublic ? 1 : 0));
//        dest.writeParcelable(payload, flags);
        dest.writeParcelable(repo, flags);
        dest.writeParcelable(actor, flags);
        dest.writeParcelable(org, flags);
        DateParceler.write(createdAt, dest);
    }

    public static class Repo implements Parcelable {
        public int id;
        public String name;
        public String url;

        protected Repo(Parcel in) {
            id = in.readInt();
            name = in.readString();
            url = in.readString();
        }

        public static final Creator<Repo> CREATOR = new Creator<Repo>() {
            @Override
            public Repo createFromParcel(Parcel in) {
                return new Repo(in);
            }

            @Override
            public Repo[] newArray(int size) {
                return new Repo[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(name);
            dest.writeString(url);
        }
    }
}
