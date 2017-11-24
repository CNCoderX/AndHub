package com.cncoderx.github.utils;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author cncoderx
 */
public class AESUtils {

    public static String encrypt(String key, String clear) {
        if (TextUtils.isEmpty(clear))
            return "";

        try {
            byte[] rawKey = getRawKey(key.getBytes());
            byte[] encrypted = encrypt(rawKey, clear.getBytes());
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    public static String decrypt(String key, String encrypted) {
        if (TextUtils.isEmpty(encrypted))
            return "";

        try {
            byte[] _encrypted = Base64.decode(encrypted, Base64.DEFAULT);
            byte[] rawKey = getRawKey(key.getBytes());
            byte[] decrypted = decrypt(rawKey, _encrypted);
            return new String(decrypted);
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    private static byte[] encrypt(byte[] key, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decrypt(byte[] key, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    @SuppressLint("TrulyRandom")
    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        SecureRandom sr;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        } else {
            sr = SecureRandom.getInstance("SHA1PRNG");
        }
        sr.setSeed(seed);
        keyGen.init(256, sr); // 256 bits or 128 bits,192bits
        SecretKey sKey = keyGen.generateKey();
        byte[] raw = sKey.getEncoded();
        return raw;
    }

}
