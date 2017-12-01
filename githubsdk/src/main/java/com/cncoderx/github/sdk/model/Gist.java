package com.cncoderx.github.sdk.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoderx.github.sdk.parcel.DateParcel;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @author cncoderx
 */
public class Gist implements Parcelable {
    public String id;
    public String description;
    @SerializedName("public")
    public boolean isPublic;
    public User owner;
    public User user;
    public Map<String, File> files;
    public boolean truncated;
    public int comments;
    @SerializedName("created_at")
    public Date createdAt;
    @SerializedName("updated_at")
    public Date updatedAt;
    public Gist[] forks;
    public History[] history;

    protected Gist(Parcel in) {
        id = in.readString();
        description = in.readString();
        isPublic = in.readByte() != 0;
        owner = in.readParcelable(User.class.getClassLoader());
        user = in.readParcelable(User.class.getClassLoader());
        files = readFilesMap(in);
        truncated = in.readByte() != 0;
        comments = in.readInt();
        createdAt = DateParcel.read(in);
        updatedAt = DateParcel.read(in);
        forks = in.createTypedArray(Gist.CREATOR);
        history = in.createTypedArray(History.CREATOR);
    }

    private Map<String, File> readFilesMap(Parcel in) {
        int size = in.readInt();
        Map<String, File> map = new LinkedTreeMap<>();
        for (int i = 0; i < size; i++) {
            String key = in.readString();
            File value = in.readParcelable(File.class.getClassLoader());
            map.put(key, value);
        }
        return map;
    }

    private void writeFilesMap(Map<String, File> files, Parcel in, int flags) {
        if (files == null || files.size() == 0) {
            in.writeInt(0);
            return;
        }
        int size = files.size();
        in.writeInt(size);
        Set<Map.Entry<String, File>> entries = files.entrySet();
        for (Map.Entry<String, File> entry : entries) {
            in.writeString(entry.getKey());
            in.writeParcelable(entry.getValue(), flags);
        }
    }

    public static final Creator<Gist> CREATOR = new Creator<Gist>() {
        @Override
        public Gist createFromParcel(Parcel in) {
            return new Gist(in);
        }

        @Override
        public Gist[] newArray(int size) {
            return new Gist[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(description);
        dest.writeByte((byte) (isPublic ? 1 : 0));
        dest.writeParcelable(owner, flags);
        dest.writeParcelable(user, flags);
        writeFilesMap(files, dest, flags);
        dest.writeByte((byte) (truncated ? 1 : 0));
        dest.writeInt(comments);
        DateParcel.write(createdAt, dest);
        DateParcel.write(updatedAt, dest);
        dest.writeTypedArray(forks, flags);
        dest.writeTypedArray(history, flags);
    }

    public static class File implements Parcelable {
        public int size;
        @SerializedName("raw_url")
        public String url;
        public String type;
        public String language;
        public boolean truncated;
        public String content;

        protected File(Parcel in) {
            size = in.readInt();
            url = in.readString();
            type = in.readString();
            language = in.readString();
            truncated = in.readByte() != 0;
            content = in.readString();
        }

        public static final Creator<File> CREATOR = new Creator<File>() {
            @Override
            public File createFromParcel(Parcel in) {
                return new File(in);
            }

            @Override
            public File[] newArray(int size) {
                return new File[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(size);
            dest.writeString(url);
            dest.writeString(type);
            dest.writeString(language);
            dest.writeByte((byte) (truncated ? 1 : 0));
            dest.writeString(content);
        }
    }

    public static class History implements Parcelable {
        public String version;
        public User user;
        @SerializedName("committed_at")
        public Date committedAt;
        @SerializedName("change_status")
        public Status changeStatus;

        protected History(Parcel in) {
            version = in.readString();
            user = in.readParcelable(User.class.getClassLoader());
            committedAt = DateParcel.read(in);
            changeStatus = in.readParcelable(Status.class.getClassLoader());
        }

        public static final Creator<History> CREATOR = new Creator<History>() {
            @Override
            public History createFromParcel(Parcel in) {
                return new History(in);
            }

            @Override
            public History[] newArray(int size) {
                return new History[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(version);
            dest.writeParcelable(user, flags);
            DateParcel.write(committedAt, dest);
            dest.writeParcelable(changeStatus, flags);
        }
    }

    public static class Status implements Parcelable {
        public int deletions;
        public int additions;
        public int total;

        protected Status(Parcel in) {
            deletions = in.readInt();
            additions = in.readInt();
            total = in.readInt();
        }

        public static final Creator<Status> CREATOR = new Creator<Status>() {
            @Override
            public Status createFromParcel(Parcel in) {
                return new Status(in);
            }

            @Override
            public Status[] newArray(int size) {
                return new Status[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(deletions);
            dest.writeInt(additions);
            dest.writeInt(total);
        }
    }
}
