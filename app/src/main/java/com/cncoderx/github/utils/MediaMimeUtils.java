package com.cncoderx.github.utils;

import android.text.TextUtils;

import java.util.HashMap;

/**
 * @author cncoderx
 */
public class MediaMimeUtils {

    private static HashMap<String, String> imageMimeTypes = new HashMap<>();
    static {
        imageMimeTypes.put("JPG", "image/jpeg");
        imageMimeTypes.put("JPEG", "image/jpeg");
        imageMimeTypes.put("GIF", "image/gif");
        imageMimeTypes.put("PNG", "image/png");
        imageMimeTypes.put("BMP", "image/x-ms-bmp");
        imageMimeTypes.put("WBMP", "image/vnd.wap.wbmp");
        imageMimeTypes.put("WPL", "application/vnd.ms-wpl");
    }

    private static HashMap<String, String> audioMimeTypes = new HashMap<>();
    static {
        audioMimeTypes.put("MP3", "audio/mpeg");
        audioMimeTypes.put("M4A", "audio/mp4");
        audioMimeTypes.put("WAV", "audio/x-wav");
        audioMimeTypes.put("AMR", "audio/amr");
        audioMimeTypes.put("AWB", "audio/amr-wb");
        audioMimeTypes.put("WMA", "audio/x-ms-wma");
        audioMimeTypes.put("OGG", "application/ogg");
        audioMimeTypes.put("MID", "audio/midi");
        audioMimeTypes.put("XMF", "audio/midi");
        audioMimeTypes.put("RTTTL", "audio/midi");
        audioMimeTypes.put("SMF", "audio/sp-midi");
        audioMimeTypes.put("IMY", "audio/imelody");
        audioMimeTypes.put("M3U", "audio/x-mpegurl");
        audioMimeTypes.put("PLS", "audio/x-scpls");
    }

    private static HashMap<String, String> videoMimeTypes = new HashMap<>();
    static {
        videoMimeTypes.put("MP4", "video/mp4");
        videoMimeTypes.put("M4V", "video/mp4");
        videoMimeTypes.put("3GP", "video/3gpp");
        videoMimeTypes.put("3GPP", "video/3gpp");
        videoMimeTypes.put("3G2", "video/3gpp2");
        videoMimeTypes.put("3GPP2", "video/3gpp2");
        videoMimeTypes.put("WMV", "video/x-ms-wmv");
    }

    public static String getMimeType(String suffix) {
        if (TextUtils.isEmpty(suffix))
            return null;

        if (suffix.charAt(0) == '.')
            suffix = suffix.substring(1);

        suffix = suffix.toUpperCase();
        String mimeType = imageMimeTypes.get(suffix);
        if (mimeType == null) {
            mimeType = audioMimeTypes.get(suffix);
        }
        if (mimeType == null) {
            mimeType = videoMimeTypes.get(suffix);
        }
        return mimeType;
    }

    public static boolean isImage(String suffix) {
        if (TextUtils.isEmpty(suffix))
            return false;

        if (suffix.charAt(0) == '.')
            suffix = suffix.substring(1);

        return imageMimeTypes.containsKey(suffix.toUpperCase());
    }

    public static boolean isAudio(String suffix) {
        if (TextUtils.isEmpty(suffix))
            return false;

        if (suffix.charAt(0) == '.')
            suffix = suffix.substring(1);

        return audioMimeTypes.containsKey(suffix.toUpperCase());
    }

    public static boolean isVideo(String suffix) {
        if (TextUtils.isEmpty(suffix))
            return false;

        if (suffix.charAt(0) == '.')
            suffix = suffix.substring(1);

        return videoMimeTypes.containsKey(suffix.toUpperCase());
    }
}

