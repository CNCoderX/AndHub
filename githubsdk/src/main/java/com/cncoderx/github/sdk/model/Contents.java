package com.cncoderx.github.sdk.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * @author cncoderx
 */
public class Contents implements Parcelable {
    public String type; // file dir symlink submodule
    public String encoding;  // just for file type
    public int size;
    public String name;
    public String path;
    public String content; // just for file type
    public String sha;
    public String url;
    @SerializedName("git_url")
    public String gitUrl;
    @SerializedName("html_url")
    public String htmlUrl;
    @SerializedName("download_url")
    public String downloadUrl;

    public String target;  // just for symlink type
    @SerializedName("submodule_git_url")
    public String subGitUrl;

    public static final String TYPE_FILE = "file";
    public static final String TYPE_DIRECTION = "dir";
    public static final String TYPE_SYMLINK = "symlink";
    public static final String TYPE_SUBMODULE = "submodule";

    protected Contents(Parcel in) {
        type = in.readString();
        encoding = in.readString();
        size = in.readInt();
        name = in.readString();
        path = in.readString();
        content = in.readString();
        sha = in.readString();
        url = in.readString();
        gitUrl = in.readString();
        htmlUrl = in.readString();
        downloadUrl = in.readString();
        target = in.readString();
        subGitUrl = in.readString();
    }

    public static final Creator<Contents> CREATOR = new Creator<Contents>() {
        @Override
        public Contents createFromParcel(Parcel in) {
            return new Contents(in);
        }

        @Override
        public Contents[] newArray(int size) {
            return new Contents[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(encoding);
        dest.writeInt(size);
        dest.writeString(name);
        dest.writeString(path);
        dest.writeString(content);
        dest.writeString(sha);
        dest.writeString(url);
        dest.writeString(gitUrl);
        dest.writeString(htmlUrl);
        dest.writeString(downloadUrl);
        dest.writeString(target);
        dest.writeString(subGitUrl);
    }
}
