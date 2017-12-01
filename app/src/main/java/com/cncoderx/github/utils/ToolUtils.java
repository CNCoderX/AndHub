package com.cncoderx.github.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.Set;

/**
 * @author cncoderx
 */
public class ToolUtils {

    public static long getDirectorySize(File file) throws Exception {
        long size = 0;
        try {
            final File[] listFiles = file.listFiles();
            for (int i = 0; i < listFiles.length; i++) {
                if (listFiles[i].isDirectory()) {
                    size = size + getDirectorySize(listFiles[i]);
                } else {
                    size = size + listFiles[i].length();
                }
            }
        } catch (Exception e) {

        }
        return size;
    }

    @NonNull
    public static String getCacheDirPath(@NonNull Context context) {
        String cachePath = null;
        File cacheDir = context.getCacheDir();
        if (cacheDir != null) {
            cachePath = cacheDir.getPath();
        } else {
            cacheDir = context.getDir("/data/data/" + context.getPackageName() + "/cache/", Context.MODE_PRIVATE);
            if (cacheDir != null) {
                cachePath = cacheDir.getPath();
            }
        }
        if (cachePath == null) {
            throw new NullPointerException("Cannot Create Cache Dir");
        }
        return cachePath;
    }

    @NonNull
    public static String getFilesDirPath(@NonNull Context context) {
        String filesPath = null;
        File filesDir = context.getFilesDir();
        if (filesDir != null) {
            filesPath = filesDir.getPath();
        } else {
            filesDir = context.getDir("/data/data/" + context.getPackageName() + "/files/", Context.MODE_PRIVATE);
            if (filesDir != null) {
                filesPath = filesDir.getPath();
            }
        }
        if (filesPath == null) {
            throw new NullPointerException("Cannot Create Files Dir");
        }
        return filesPath;
    }

    public static void clearDir(final String dir) throws Exception {
        final File file = new File(dir);
        if (file.exists()) {
            final File[] listFiles = file.listFiles();
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                if (listFiles[i].isDirectory()) {
                    removeDir(listFiles[i].getAbsolutePath());
                } else if (listFiles[i].isFile()) {
                    listFiles[i].delete();
                }
            }
        }
    }

    public static void clearDir(final String dir, final Set<String> except) throws Exception {
        final File file = new File(dir);
        if (file.exists()) {
            final File[] listFiles = file.listFiles();
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                if (listFiles[i].isDirectory()) {
                    removeDir(listFiles[i].getAbsolutePath(), except);
                } else if (listFiles[i].isFile()) {
                    final String name = listFiles[i].getName();
                    if (except == null || !except.contains(name)) {
                        listFiles[i].delete();
                    }
                }
            }
        }
    }

    public static void removeDir(final String dir) throws Exception {
        final File file = new File(dir);
        if (file.exists() && file.isDirectory()) {
            final File[] listFiles = file.listFiles();
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                if (listFiles[i].isDirectory()) {
                    removeDir(listFiles[i].getAbsolutePath());
                } else {
                    listFiles[i].delete();
                }
            }
            file.delete();
        }
    }

    public static void removeDir(final String dir, final Set<String> except) throws Exception {
        final File file = new File(dir);
        if (file.exists() && file.isDirectory()) {
            final File[] listFiles = file.listFiles();
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                if (listFiles[i].isDirectory()) {
                    removeDir(listFiles[i].getAbsolutePath(), except);
                } else {
                    final String name = listFiles[i].getName();
                    if (except == null || !except.contains(name)) {
                        listFiles[i].delete();
                    }
                }
            }
        }
    }
}
