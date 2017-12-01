package com.cncoderx.github.sdk.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoderx.github.sdk.parcel.DateParcel;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * @author cncoderx
 */
public class PullRequest implements Parcelable {
    public int id;
    public int number;
    public String state;
    public String title;
    public String body;
    public User user;
    public User assignee;
    public List<User> assignees;
    public List<Label> labels;
    public Milestone milestone;
    public boolean locked;
    public int comments;
    @SerializedName("closed_at")
    public Date closedAt;
    @SerializedName("created_at")
    public Date createdAt;
    @SerializedName("updated_at")
    public Date updatedAt;
    @SerializedName("merged_at")
    public Date mergedAt;

    protected PullRequest(Parcel in) {
        id = in.readInt();
        number = in.readInt();
        state = in.readString();
        title = in.readString();
        body = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        assignee = in.readParcelable(User.class.getClassLoader());
        assignees = in.createTypedArrayList(User.CREATOR);
        labels = in.createTypedArrayList(Label.CREATOR);
        milestone = in.readParcelable(Milestone.class.getClassLoader());
        locked = in.readByte() != 0;
        comments = in.readInt();
        closedAt = DateParcel.read(in);
        createdAt = DateParcel.read(in);
        updatedAt = DateParcel.read(in);
        mergedAt = DateParcel.read(in);
    }

    public static final Creator<PullRequest> CREATOR = new Creator<PullRequest>() {
        @Override
        public PullRequest createFromParcel(Parcel in) {
            return new PullRequest(in);
        }

        @Override
        public PullRequest[] newArray(int size) {
            return new PullRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(number);
        dest.writeString(state);
        dest.writeString(title);
        dest.writeString(body);
        dest.writeParcelable(user, flags);
        dest.writeParcelable(assignee, flags);
        dest.writeTypedList(assignees);
        dest.writeTypedList(labels);
        dest.writeParcelable(milestone, flags);
        dest.writeByte((byte) (locked ? 1 : 0));
        dest.writeInt(comments);
        DateParcel.write(closedAt, dest);
        DateParcel.write(createdAt, dest);
        DateParcel.write(updatedAt, dest);
        DateParcel.write(mergedAt, dest);
    }
}
