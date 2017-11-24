package com.cncoderx.github.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * @author cncoderx
 */
public class AuthUtils {
    public static String createBasicAuth(String username, String password) {
        String credentials = username + ":" + password;
        String basic;
        try {
            basic = Base64.encodeToString(credentials.getBytes("US-ASCII"), Base64.NO_WRAP);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
        return "Basic " + basic;
    }
}
