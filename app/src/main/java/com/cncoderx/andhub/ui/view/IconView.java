package com.cncoderx.andhub.ui.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * @author cncoderx
 */
public class IconView extends android.support.v7.widget.AppCompatTextView {

    public IconView(Context context) {
        super(context);
        setTypeface(Typeface.createFromAsset(
                getResources().getAssets(), "icon.ttf"));
    }

    public IconView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Typeface.createFromAsset(
                getResources().getAssets(), "icon.ttf"));
    }
}
