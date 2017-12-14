package com.cncoderx.andhub.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author cncoderx
 */
public class File {
    public Type type; // file dir symlink submodule
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

    public static enum Type {
        file, dir, symlink, submodule
    }
}
