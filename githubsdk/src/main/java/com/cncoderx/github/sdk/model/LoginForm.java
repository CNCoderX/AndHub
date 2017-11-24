package com.cncoderx.github.sdk.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cncoderx
 */

public class LoginForm {
    @SerializedName("client_id")
    public String clientId = "";

    @SerializedName("client_secret")
    public String clientSecret = "";

    public String note = "";

    @SerializedName("note_url")
    public String noteUrl = "";

    public String fingerprint = "";

    public List<String> scopes = new ArrayList<>();
}
