package com.cncoderx.github.entites;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * @author cncoderx
 */
public class Repositories {
    @SerializedName("total_count")
    private int totalCount;
    @SerializedName("incomplete_results")
    private boolean incompleteResults;
    private List<Item> items;
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
    public int getTotalCount() {
        return totalCount;
    }

    public void setIncompleteResults(boolean incompleteResults) {
        this.incompleteResults = incompleteResults;
    }
    public boolean getIncompleteResults() {
        return incompleteResults;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
    public List<Item> getItems() {
        return items;
    }

    public static class Item {
        private int id;
        private String name;
        @SerializedName("full_name")
        private String fullName;
        private Owner owner;
        @SerializedName("private")
        private boolean isPrivate;
        @SerializedName("html_url")
        private String htmlUrl;
        private String description;
        private boolean fork;
        private String url;
        @SerializedName("created_at")
        private Date createDate;
        @SerializedName("updated_at")
        private Date updateDate;
        @SerializedName("pushed_at")
        private Date pushDate;
        private String homepage;
        private int size;
        @SerializedName("stargazers_count")
        private int starCount;
        @SerializedName("watchers_count")
        private int watcherCount;
        private String language;
        @SerializedName("forks_count")
        private int forkCount;
        @SerializedName("open_issues_count")
        private int issueCount;
        @SerializedName("master_branch")
        private String masterBranch;
        @SerializedName("default_branch")
        private String defaultBranch;
        private double score;

        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }
        public String getFullName() {
            return fullName;
        }

        public void setOwner(Owner owner) {
            this.owner = owner;
        }
        public Owner getOwner() {
            return owner;
        }

        public void setPrivate(boolean isPrivate) {
            this.isPrivate = isPrivate;
        }
        public boolean isPrivate() {
            return isPrivate;
        }

        public void setHtmlUrl(String htmlUrl) {
            this.htmlUrl = htmlUrl;
        }
        public String getHtmlUrl() {
            return htmlUrl;
        }

        public void setDescription(String description) {
            this.description = description;
        }
        public String getDescription() {
            return description;
        }

        public void setFork(boolean fork) {
            this.fork = fork;
        }
        public boolean getFork() {
            return fork;
        }

        public void setUrl(String url) {
            this.url = url;
        }
        public String getUrl() {
            return url;
        }

        public void setCreateDate(Date createDate) {
            this.createDate = createDate;
        }
        public Date getCreateDate() {
            return createDate;
        }

        public void setUpdateDate(Date updateDate) {
            this.updateDate = updateDate;
        }
        public Date getUpdateDate() {
            return updateDate;
        }

        public void setPushDate(Date pushDate) {
            this.pushDate = pushDate;
        }
        public Date getPushDate() {
            return pushDate;
        }

        public void setHomepage(String homepage) {
            this.homepage = homepage;
        }
        public String getHomepage() {
            return homepage;
        }

        public void setSize(int size) {
            this.size = size;
        }
        public int getSize() {
            return size;
        }

        public void setStarCount(int starCount) {
            this.starCount = starCount;
        }
        public int getStarCount() {
            return starCount;
        }

        public void setWatcherCount(int watcherCount) {
            this.watcherCount = watcherCount;
        }
        public int getWatcherCount() {
            return watcherCount;
        }

        public void setLanguage(String language) {
            this.language = language;
        }
        public String getLanguage() {
            return language;
        }

        public void setForkCount(int forkCount) {
            this.forkCount = forkCount;
        }
        public int getForkCount() {
            return forkCount;
        }

        public void setIssueCount(int issueCount) {
            this.issueCount = issueCount;
        }
        public int getIssueCount() {
            return issueCount;
        }

        public void setMasterBranch(String masterBranch) {
            this.masterBranch = masterBranch;
        }
        public String getMasterBranch() {
            return masterBranch;
        }

        public void setDefaultBranch(String defaultBranch) {
            this.defaultBranch = defaultBranch;
        }
        public String getDefaultBranch() {
            return defaultBranch;
        }

        public void setScore(double score) {
            this.score = score;
        }
        public double getScore() {
            return score;
        }
    }
}
