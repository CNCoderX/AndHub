package com.cncoderx.github.sdk.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author cncoderx
 */
public class Repositories implements Parcelable {
    @SerializedName("total_count")
    public int totalCount;

    @SerializedName("incomplete_results")
    public boolean incompleteResults;

    public List<Repository> items;

    protected Repositories(Parcel in) {
        totalCount = in.readInt();
        incompleteResults = in.readByte() != 0;
        items = in.createTypedArrayList(Repository.CREATOR);
    }

    public static final Creator<Repositories> CREATOR = new Creator<Repositories>() {
        @Override
        public Repositories createFromParcel(Parcel in) {
            return new Repositories(in);
        }

        @Override
        public Repositories[] newArray(int size) {
            return new Repositories[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(totalCount);
        dest.writeByte((byte) (incompleteResults ? 1 : 0));
        dest.writeTypedList(items);
    }
}
