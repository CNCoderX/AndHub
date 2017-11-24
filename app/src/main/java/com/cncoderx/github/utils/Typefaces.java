package com.cncoderx.github.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;

import java.lang.reflect.Field;

/**
 * @author cncoderx
 */
public class Typefaces {

    public static void replaceSystemDefaultFont(@NonNull Context context, @NonNull String fontPath) {
        replaceTypefaceField("MONOSPACE", createTypeface(context, fontPath));
    }

    private static Typeface createTypeface(Context context, String fontPath) {
        return Typeface.createFromAsset(context.getAssets(), fontPath);
    }

    private static void replaceTypefaceField(String fieldName, Object value) {
        try {
            Field defaultField = Typeface.class.getDeclaredField(fieldName);
            defaultField.setAccessible(true);
            defaultField.set(null, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
