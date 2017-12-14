package com.cncoderx.andhub.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoderx.andhub.model.parcel.EnumParceler;

/**
 * @author cncoderx
 */
public class GitTree implements Parcelable {
    public String sha;
    public File[] tree;

    protected GitTree(Parcel in) {
        sha = in.readString();
        tree = in.createTypedArray(File.CREATOR);
    }

    public static final Creator<GitTree> CREATOR = new Creator<GitTree>() {
        @Override
        public GitTree createFromParcel(Parcel in) {
            return new GitTree(in);
        }

        @Override
        public GitTree[] newArray(int size) {
            return new GitTree[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sha);
        dest.writeTypedArray(tree, flags);
    }

    public static class File implements Parcelable {
        public String path;
        public String mode;
        public Type type;
        public int size;
        public String sha;

        protected File(Parcel in) {
            path = in.readString();
            mode = in.readString();
            type = EnumParceler.read(in, Type.class);
            size = in.readInt();
            sha = in.readString();
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
            dest.writeString(path);
            dest.writeString(mode);
            EnumParceler.write(type, dest);
            dest.writeInt(size);
            dest.writeString(sha);
        }
    }

    public static enum Type {
        tree, blob
    }
}
