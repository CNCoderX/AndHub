package com.cncoderx.andhub.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoderx.andhub.model.parcel.DateParceler;
import com.cncoderx.andhub.model.parcel.EnumParceler;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * @author cncoderx
 */
public class Profile implements Parcelable {
    public int id;
    public String login;
    @SerializedName("gravatar_id")
    public String avatarId;
    @SerializedName("avatar_url")
    public String avatarUrl;
    public User.Type type;
    @SerializedName("description")
    public String desc; // only for organization
    @SerializedName("site_admin")
    public boolean isAdmin;
    public String name;
    public String company;
    public String blog;
    public String location;
    public String email;
    public boolean hireable;
    public String bio;
    @SerializedName("public_repos")
    public int publicRepos;
    @SerializedName("public_gists")
    public int publicGists;
    public int followers;
    public int following;
    @SerializedName("created_at")
    public Date createdAt;
    @SerializedName("updated_at")
    public Date updatedAt;
    @SerializedName("total_private_repos")
    public int privateRepos;
    @SerializedName("private_gists")
    public int privateGists;
    @SerializedName("disk_usage")
    public int diskUsage;
    public int collaborators;
    @SerializedName("two_factor_authentication")
    public boolean twoFactorAuth;

    public Profile() {
    }

    protected Profile(Parcel in) {
        id = in.readInt();
        login = in.readString();
        avatarId = in.readString();
        avatarUrl = in.readString();
        type = EnumParceler.read(in, User.Type.class);
        desc = in.readString();
        isAdmin = in.readByte() != 0;
        name = in.readString();
        company = in.readString();
        blog = in.readString();
        location = in.readString();
        email = in.readString();
        hireable = in.readByte() != 0;
        bio = in.readString();
        publicRepos = in.readInt();
        publicGists = in.readInt();
        followers = in.readInt();
        following = in.readInt();
        createdAt = DateParceler.read(in);
        updatedAt = DateParceler.read(in);
        privateRepos = in.readInt();
        privateGists = in.readInt();
        diskUsage = in.readInt();
        collaborators = in.readInt();
        twoFactorAuth = in.readByte() != 0;
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(login);
        dest.writeString(avatarId);
        dest.writeString(avatarUrl);
        EnumParceler.write(type, dest);
        dest.writeString(desc);
        dest.writeByte((byte) (isAdmin ? 1 : 0));
        dest.writeString(name);
        dest.writeString(company);
        dest.writeString(blog);
        dest.writeString(location);
        dest.writeString(email);
        dest.writeByte((byte) (hireable ? 1 : 0));
        dest.writeString(bio);
        dest.writeInt(publicRepos);
        dest.writeInt(publicGists);
        dest.writeInt(followers);
        dest.writeInt(following);
        DateParceler.write(createdAt, dest);
        DateParceler.write(updatedAt, dest);
        dest.writeInt(privateRepos);
        dest.writeInt(privateGists);
        dest.writeInt(diskUsage);
        dest.writeInt(collaborators);
        dest.writeByte((byte) (twoFactorAuth ? 1 : 0));
    }
}
