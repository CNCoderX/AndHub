package com.cncoderx.andhub.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoderx.andhub.model.parcel.DateParceler;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * @author cncoderx
 */
public class Milestone implements Parcelable {
    public String url;
    @SerializedName("html_url")
    public String htmlUrl;
    @SerializedName("labels_url")
    public String labelsUrl;
    public int id;
    public int number;
    public String state;
    public String title;
    public String description;
    public User creator;
    @SerializedName("open_issues")
    public int openIssues;
    @SerializedName("closed_issues")
    public int closedIssues;
    @SerializedName("created_at")
    public Date createdAt;
    @SerializedName("updated_at")
    public Date updatedAt;
    @SerializedName("closed_at")
    public Date closedAt;
    @SerializedName("due_on")
    public Date dueOn;

    protected Milestone(Parcel in) {
        url = in.readString();
        htmlUrl = in.readString();
        labelsUrl = in.readString();
        id = in.readInt();
        number = in.readInt();
        state = in.readString();
        title = in.readString();
        description = in.readString();
        creator = in.readParcelable(User.class.getClassLoader());
        openIssues = in.readInt();
        closedIssues = in.readInt();
        createdAt = DateParceler.read(in);
        updatedAt = DateParceler.read(in);
        closedAt = DateParceler.read(in);
        dueOn = DateParceler.read(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(htmlUrl);
        dest.writeString(labelsUrl);
        dest.writeInt(id);
        dest.writeInt(number);
        dest.writeString(state);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeParcelable(creator, flags);
        dest.writeInt(openIssues);
        dest.writeInt(closedIssues);
        DateParceler.write(createdAt, dest);
        DateParceler.write(updatedAt, dest);
        DateParceler.write(closedAt, dest);
        DateParceler.write(dueOn, dest);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Milestone> CREATOR = new Creator<Milestone>() {
        @Override
        public Milestone createFromParcel(Parcel in) {
            return new Milestone(in);
        }

        @Override
        public Milestone[] newArray(int size) {
            return new Milestone[size];
        }
    };
}
