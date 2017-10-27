package com.cncoderx.github.entites;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * @author cncoderx
 */
public class Repository {
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
    @SerializedName("archive_url")
    private String archiveUrl;
    @SerializedName("assignees_url")
    private String assigneesUrl;
    @SerializedName("blobs_url")
    private String blobsUrl;
    @SerializedName("branches_url")
    private String branchesUrl;
    @SerializedName("clone_url")
    private String cloneUrl;
    @SerializedName("collaborators_url")
    private String collaboratorsUrl;
    @SerializedName("comments_url")
    private String commentsUrl;
    @SerializedName("commits_url")
    private String commitsUrl;
    @SerializedName("compare_url")
    private String compareUrl;
    @SerializedName("contents_url")
    private String contentsUrl;
    @SerializedName("contributors_url")
    private String contributorsUrl;
    @SerializedName("deployments_url")
    private String deploymentsUrl;
    @SerializedName("downloads_url")
    private String downloadsUrl;
    @SerializedName("events_url")
    private String eventsUrl;
    @SerializedName("forks_url")
    private String forksUrl;
    @SerializedName("git_commits_url")
    private String git_commitsUrl;
    @SerializedName("git_refs_url")
    private String git_refsUrl;
    @SerializedName("git_tags_url")
    private String git_tagsUrl;
    @SerializedName("git_url")
    private String gitUrl;
    @SerializedName("hooks_url")
    private String hooksUrl;
    @SerializedName("issue_comment_url")
    private String issue_commentUrl;
    @SerializedName("issue_events_url")
    private String issue_eventsUrl;
    @SerializedName("issues_url")
    private String issuesUrl;
    @SerializedName("keys_url")
    private String keysUrl;
    @SerializedName("labels_url")
    private String labelsUrl;
    @SerializedName("languages_url")
    private String languagesUrl;
    @SerializedName("merges_url")
    private String mergesUrl;
    @SerializedName("milestones_url")
    private String milestonesUrl;
    @SerializedName("mirror_url")
    private String mirrorUrl;
    @SerializedName("notifications_url")
    private String notificationsUrl;
    @SerializedName("pulls_url")
    private String pullsUrl;
    @SerializedName("releases_url")
    private String releasesUrl;
    @SerializedName("ssh_url")
    private String sshUrl;
    @SerializedName("stargazers_url")
    private String stargazersUrl;
    @SerializedName("statuses_url")
    private String statusesUrl;
    @SerializedName("subscribers_url")
    private String subscribersUrl;
    @SerializedName("subscription_url")
    private String subscriptionUrl;
    @SerializedName("svn_url")
    private String svnUrl;
    @SerializedName("tags_url")
    private String tagsUrl;
    @SerializedName("teams_url")
    private String teamsUrl;
    @SerializedName("trees_url")
    private String treesUrl;
    private List<String> topics;
    @SerializedName("has_issues")
    private boolean hasIssues;
    @SerializedName("has_wiki")
    private boolean hasWiki;
    @SerializedName("has_pages")
    private boolean hasPages;
    @SerializedName("has_downloads")
    private boolean hasDownloads;
    private Permissions permissions;
    @SerializedName("allow_rebase_merge")
    private boolean allowRebaseMerge;
    @SerializedName("allow_squash_merge")
    private boolean allowSquashMerge;
    @SerializedName("allow_merge_commit")
    private boolean allowMergeCommit;
    @SerializedName("subscribers_count")
    private int subscribersCount;
    @SerializedName("network_count")
    private int networkCount;
    private Organization organization;
    private Repository parent;
    private Repository source;

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

    public boolean isFork() {
        return fork;
    }

    public String getArchiveUrl() {
        return archiveUrl;
    }

    public void setArchiveUrl(String archiveUrl) {
        this.archiveUrl = archiveUrl;
    }

    public String getAssigneesUrl() {
        return assigneesUrl;
    }

    public void setAssigneesUrl(String assigneesUrl) {
        this.assigneesUrl = assigneesUrl;
    }

    public String getBlobsUrl() {
        return blobsUrl;
    }

    public void setBlobsUrl(String blobsUrl) {
        this.blobsUrl = blobsUrl;
    }

    public String getBranchesUrl() {
        return branchesUrl;
    }

    public void setBranchesUrl(String branchesUrl) {
        this.branchesUrl = branchesUrl;
    }

    public String getCloneUrl() {
        return cloneUrl;
    }

    public void setCloneUrl(String cloneUrl) {
        this.cloneUrl = cloneUrl;
    }

    public String getCollaboratorsUrl() {
        return collaboratorsUrl;
    }

    public void setCollaboratorsUrl(String collaboratorsUrl) {
        this.collaboratorsUrl = collaboratorsUrl;
    }

    public String getCommentsUrl() {
        return commentsUrl;
    }

    public void setCommentsUrl(String commentsUrl) {
        this.commentsUrl = commentsUrl;
    }

    public String getCommitsUrl() {
        return commitsUrl;
    }

    public void setCommitsUrl(String commitsUrl) {
        this.commitsUrl = commitsUrl;
    }

    public String getCompareUrl() {
        return compareUrl;
    }

    public void setCompareUrl(String compareUrl) {
        this.compareUrl = compareUrl;
    }

    public String getContentsUrl() {
        return contentsUrl;
    }

    public void setContentsUrl(String contentsUrl) {
        this.contentsUrl = contentsUrl;
    }

    public String getContributorsUrl() {
        return contributorsUrl;
    }

    public void setContributorsUrl(String contributorsUrl) {
        this.contributorsUrl = contributorsUrl;
    }

    public String getDeploymentsUrl() {
        return deploymentsUrl;
    }

    public void setDeploymentsUrl(String deploymentsUrl) {
        this.deploymentsUrl = deploymentsUrl;
    }

    public String getDownloadsUrl() {
        return downloadsUrl;
    }

    public void setDownloadsUrl(String downloadsUrl) {
        this.downloadsUrl = downloadsUrl;
    }

    public String getEventsUrl() {
        return eventsUrl;
    }

    public void setEventsUrl(String eventsUrl) {
        this.eventsUrl = eventsUrl;
    }

    public String getForksUrl() {
        return forksUrl;
    }

    public void setForksUrl(String forksUrl) {
        this.forksUrl = forksUrl;
    }

    public String getGit_commitsUrl() {
        return git_commitsUrl;
    }

    public void setGit_commitsUrl(String git_commitsUrl) {
        this.git_commitsUrl = git_commitsUrl;
    }

    public String getGit_refsUrl() {
        return git_refsUrl;
    }

    public void setGit_refsUrl(String git_refsUrl) {
        this.git_refsUrl = git_refsUrl;
    }

    public String getGit_tagsUrl() {
        return git_tagsUrl;
    }

    public void setGit_tagsUrl(String git_tagsUrl) {
        this.git_tagsUrl = git_tagsUrl;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    public String getHooksUrl() {
        return hooksUrl;
    }

    public void setHooksUrl(String hooksUrl) {
        this.hooksUrl = hooksUrl;
    }

    public String getIssue_commentUrl() {
        return issue_commentUrl;
    }

    public void setIssue_commentUrl(String issue_commentUrl) {
        this.issue_commentUrl = issue_commentUrl;
    }

    public String getIssue_eventsUrl() {
        return issue_eventsUrl;
    }

    public void setIssue_eventsUrl(String issue_eventsUrl) {
        this.issue_eventsUrl = issue_eventsUrl;
    }

    public String getIssuesUrl() {
        return issuesUrl;
    }

    public void setIssuesUrl(String issuesUrl) {
        this.issuesUrl = issuesUrl;
    }

    public String getKeysUrl() {
        return keysUrl;
    }

    public void setKeysUrl(String keysUrl) {
        this.keysUrl = keysUrl;
    }

    public String getLabelsUrl() {
        return labelsUrl;
    }

    public void setLabelsUrl(String labelsUrl) {
        this.labelsUrl = labelsUrl;
    }

    public String getLanguagesUrl() {
        return languagesUrl;
    }

    public void setLanguagesUrl(String languagesUrl) {
        this.languagesUrl = languagesUrl;
    }

    public String getMergesUrl() {
        return mergesUrl;
    }

    public void setMergesUrl(String mergesUrl) {
        this.mergesUrl = mergesUrl;
    }

    public String getMilestonesUrl() {
        return milestonesUrl;
    }

    public void setMilestonesUrl(String milestonesUrl) {
        this.milestonesUrl = milestonesUrl;
    }

    public String getMirrorUrl() {
        return mirrorUrl;
    }

    public void setMirrorUrl(String mirrorUrl) {
        this.mirrorUrl = mirrorUrl;
    }

    public String getNotificationsUrl() {
        return notificationsUrl;
    }

    public void setNotificationsUrl(String notificationsUrl) {
        this.notificationsUrl = notificationsUrl;
    }

    public String getPullsUrl() {
        return pullsUrl;
    }

    public void setPullsUrl(String pullsUrl) {
        this.pullsUrl = pullsUrl;
    }

    public String getReleasesUrl() {
        return releasesUrl;
    }

    public void setReleasesUrl(String releasesUrl) {
        this.releasesUrl = releasesUrl;
    }

    public String getSshUrl() {
        return sshUrl;
    }

    public void setSshUrl(String sshUrl) {
        this.sshUrl = sshUrl;
    }

    public String getStargazersUrl() {
        return stargazersUrl;
    }

    public void setStargazersUrl(String stargazersUrl) {
        this.stargazersUrl = stargazersUrl;
    }

    public String getStatusesUrl() {
        return statusesUrl;
    }

    public void setStatusesUrl(String statusesUrl) {
        this.statusesUrl = statusesUrl;
    }

    public String getSubscribersUrl() {
        return subscribersUrl;
    }

    public void setSubscribersUrl(String subscribersUrl) {
        this.subscribersUrl = subscribersUrl;
    }

    public String getSubscriptionUrl() {
        return subscriptionUrl;
    }

    public void setSubscriptionUrl(String subscriptionUrl) {
        this.subscriptionUrl = subscriptionUrl;
    }

    public String getSvnUrl() {
        return svnUrl;
    }

    public void setSvnUrl(String svnUrl) {
        this.svnUrl = svnUrl;
    }

    public String getTagsUrl() {
        return tagsUrl;
    }

    public void setTagsUrl(String tagsUrl) {
        this.tagsUrl = tagsUrl;
    }

    public String getTeamsUrl() {
        return teamsUrl;
    }

    public void setTeamsUrl(String teamsUrl) {
        this.teamsUrl = teamsUrl;
    }

    public String getTreesUrl() {
        return treesUrl;
    }

    public void setTreesUrl(String treesUrl) {
        this.treesUrl = treesUrl;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public boolean isHasIssues() {
        return hasIssues;
    }

    public void setHasIssues(boolean hasIssues) {
        this.hasIssues = hasIssues;
    }

    public boolean isHasWiki() {
        return hasWiki;
    }

    public void setHasWiki(boolean hasWiki) {
        this.hasWiki = hasWiki;
    }

    public boolean isHasPages() {
        return hasPages;
    }

    public void setHasPages(boolean hasPages) {
        this.hasPages = hasPages;
    }

    public boolean isHasDownloads() {
        return hasDownloads;
    }

    public void setHasDownloads(boolean hasDownloads) {
        this.hasDownloads = hasDownloads;
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }

    public boolean isAllowRebaseMerge() {
        return allowRebaseMerge;
    }

    public void setAllowRebaseMerge(boolean allowRebaseMerge) {
        this.allowRebaseMerge = allowRebaseMerge;
    }

    public boolean isAllowSquashMerge() {
        return allowSquashMerge;
    }

    public void setAllowSquashMerge(boolean allowSquashMerge) {
        this.allowSquashMerge = allowSquashMerge;
    }

    public boolean isAllowMergeCommit() {
        return allowMergeCommit;
    }

    public void setAllowMergeCommit(boolean allowMergeCommit) {
        this.allowMergeCommit = allowMergeCommit;
    }

    public int getSubscribersCount() {
        return subscribersCount;
    }

    public void setSubscribersCount(int subscribersCount) {
        this.subscribersCount = subscribersCount;
    }

    public int getNetworkCount() {
        return networkCount;
    }

    public void setNetworkCount(int networkCount) {
        this.networkCount = networkCount;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Repository getParent() {
        return parent;
    }

    public void setParent(Repository parent) {
        this.parent = parent;
    }

    public Repository getSource() {
        return source;
    }

    public void setSource(Repository source) {
        this.source = source;
    }
}
