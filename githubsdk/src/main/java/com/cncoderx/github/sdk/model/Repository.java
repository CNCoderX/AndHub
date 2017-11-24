package com.cncoderx.github.sdk.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * @author cncoderx
 */
public class Repository implements Parcelable {
    public int id;
    public String name;
    @SerializedName("full_name")
    public String fullName;
    public User owner;
    @SerializedName("private")
    public boolean isPrivate;
    public String description;
    public boolean fork;
    @SerializedName("created_at")
    public Date createdAt;
    @SerializedName("updated_at")
    public Date updatedAt;
    @SerializedName("pushed_at")
    public Date pushedAt;
    public int size;
    @SerializedName("stargazers_count")
    public int starCount;
    @SerializedName("watchers_count")
    public int watcherCount;
    public String language;
    @SerializedName("forks_count")
    public int forkCount;
    @SerializedName("open_issues_count")
    public int issueCount;
    @SerializedName("master_branch")
    public String masterBranch;
    @SerializedName("default_branch")
    public String defaultBranch;
    public double score;
    public List<String> topics;
    @SerializedName("has_issues")
    public boolean hasIssues;
    @SerializedName("has_wiki")
    public boolean hasWiki;
    @SerializedName("has_pages")
    public boolean hasPages;
    @SerializedName("has_downloads")
    public boolean hasDownloads;
    public Permissions permissions;
    @SerializedName("allow_rebase_merge")
    public boolean allowRebaseMerge;
    @SerializedName("allow_squash_merge")
    public boolean allowSquashMerge;
    @SerializedName("allow_merge_commit")
    public boolean allowMergeCommit;
    @SerializedName("subscribers_count")
    public int subscribersCount;
    @SerializedName("network_count")
    public int networkCount;
    public User organization;
    public Repository parent;
    public Repository source;

    public Repository() {
    }

    protected Repository(Parcel in) {
        id = in.readInt();
        name = in.readString();
        fullName = in.readString();
        owner = in.readParcelable(User.class.getClassLoader());
        isPrivate = in.readByte() != 0;
        description = in.readString();
        fork = in.readByte() != 0;
        createdAt = DateParcel.readDate(in);
        updatedAt = DateParcel.readDate(in);
        pushedAt = DateParcel.readDate(in);
        size = in.readInt();
        starCount = in.readInt();
        watcherCount = in.readInt();
        language = in.readString();
        forkCount = in.readInt();
        issueCount = in.readInt();
        masterBranch = in.readString();
        defaultBranch = in.readString();
        score = in.readDouble();
        topics = in.createStringArrayList();
        hasIssues = in.readByte() != 0;
        hasWiki = in.readByte() != 0;
        hasPages = in.readByte() != 0;
        hasDownloads = in.readByte() != 0;
        permissions = in.readParcelable(Permissions.class.getClassLoader());
        allowRebaseMerge = in.readByte() != 0;
        allowSquashMerge = in.readByte() != 0;
        allowMergeCommit = in.readByte() != 0;
        subscribersCount = in.readInt();
        networkCount = in.readInt();
        organization = in.readParcelable(User.class.getClassLoader());
        parent = in.readParcelable(Repository.class.getClassLoader());
        source = in.readParcelable(Repository.class.getClassLoader());
    }

    public static final Creator<Repository> CREATOR = new Creator<Repository>() {
        @Override
        public Repository createFromParcel(Parcel in) {
            return new Repository(in);
        }

        @Override
        public Repository[] newArray(int size) {
            return new Repository[size];
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
        dest.writeString(fullName);
        dest.writeParcelable(owner, flags);
        dest.writeByte((byte) (isPrivate ? 1 : 0));
        dest.writeString(description);
        dest.writeByte((byte) (fork ? 1 : 0));
        DateParcel.writeDate(createdAt, dest);
        DateParcel.writeDate(updatedAt, dest);
        DateParcel.writeDate(pushedAt, dest);
        dest.writeInt(size);
        dest.writeInt(starCount);
        dest.writeInt(watcherCount);
        dest.writeString(language);
        dest.writeInt(forkCount);
        dest.writeInt(issueCount);
        dest.writeString(masterBranch);
        dest.writeString(defaultBranch);
        dest.writeDouble(score);
        dest.writeStringList(topics);
        dest.writeByte((byte) (hasIssues ? 1 : 0));
        dest.writeByte((byte) (hasWiki ? 1 : 0));
        dest.writeByte((byte) (hasPages ? 1 : 0));
        dest.writeByte((byte) (hasDownloads ? 1 : 0));
        dest.writeParcelable(permissions, flags);
        dest.writeByte((byte) (allowRebaseMerge ? 1 : 0));
        dest.writeByte((byte) (allowSquashMerge ? 1 : 0));
        dest.writeByte((byte) (allowMergeCommit ? 1 : 0));
        dest.writeInt(subscribersCount);
        dest.writeInt(networkCount);
        dest.writeParcelable(organization, flags);
        dest.writeParcelable(parent, flags);
        dest.writeParcelable(source, flags);
    }
}
