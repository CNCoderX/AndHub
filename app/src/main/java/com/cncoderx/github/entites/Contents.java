package com.cncoderx.github.entites;

import com.google.gson.annotations.SerializedName;

/**
 * @author cncoderx
 */
public class Contents {
    private String type;
    private String encoding;
    private int size;
    private String name;
    private String path;
    private String content;
    private String sha;
    private String url;
    @SerializedName("git_url")
    private String gitUrl;
    @SerializedName("html_url")
    private String htmlUrl;
    @SerializedName("download_url")
    private String downloadUrl;
    @SerializedName("_links")
    private Links links;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public static class Links {
        private String git;
        private String self;
        private String html;

        public void setGit(String git) {
            this.git = git;
        }
        public String getGit() {
            return git;
        }

        public void setSelf(String self) {
            this.self = self;
        }
        public String getSelf() {
            return self;
        }

        public void setHtml(String html) {
            this.html = html;
        }
        public String getHtml() {
            return html;
        }
    }
}
