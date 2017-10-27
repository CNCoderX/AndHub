package com.cncoderx.github.entites;

import com.google.gson.annotations.SerializedName;

/**
 * @author cncoderx
 */
public class Owner {
    private String login;
    private int id;
    @SerializedName("avatar_url")
    private String avatarUrl;
    @SerializedName("gravatar_id")
    private String avatarId;
    private String url;
    @SerializedName("received_events_url")
    private String recvEventsUrl;
    private String type;

    public void setLogin(String login) {
        this.login = login;
    }
    public String getLogin() {
        return login;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarId(String avatarId) {
        this.avatarId = avatarId;
    }
    public String getAvatarId() {
        return avatarId;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }

    public void setRecvEventsUrl(String recvEventsUrl) {
        this.recvEventsUrl = recvEventsUrl;
    }
    public String getRecvEventsUrl() {
        return recvEventsUrl;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
}
