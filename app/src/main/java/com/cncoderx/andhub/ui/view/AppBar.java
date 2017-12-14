package com.cncoderx.andhub.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cncoderx.andhub.R;

/**
 * @author cncoderx
 */
public class AppBar extends LinearLayout {
    private CharSequence mTitle;

    IconView ivNavIcon;
    TextView tvTitle;

    public AppBar(Context context) {
        super(context);
        init(context);
    }

    public AppBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AppBar);
        mTitle = a.getString(R.styleable.AppBar_title);
        a.recycle();

        init(context);
        setTitle(mTitle);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.app_bar_layout, this, true);
        ivNavIcon = (IconView) findViewById(R.id.iv_app_bar_nav_icon);
        tvTitle = (TextView) findViewById(R.id.tv_app_bar_title);
        int hPadding = getResources().getDimensionPixelOffset(R.dimen.box_horizontal_padding);
        int bgColor = getResources().getColor(R.color.theme_black);
        setPadding(hPadding, 0, hPadding, 0);
        setGravity(Gravity.CENTER);
        setBackgroundColor(bgColor);
        setOrientation(HORIZONTAL);

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.getComponentName();
            PackageManager pm = context.getPackageManager();
            try {
                ActivityInfo info = pm.getActivityInfo(activity.getComponentName(), PackageManager.GET_META_DATA);
                if (info.labelRes > 0) {
                    CharSequence title = context.getString(info.labelRes);
                    setTitle(title);
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        ivNavIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() instanceof Activity) {
                    Activity activity = (Activity) getContext();
                    if (activity != null) {
                        activity.finish();
                    }
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightSpecMode == MeasureSpec.AT_MOST) {
            int heightSpecHeight = getResources().getDimensionPixelOffset(R.dimen.app_bar_height);
            setMeasuredDimension(widthSpecSize, heightSpecHeight);
        }
    }

    public CharSequence getTitle() {
        return mTitle;
    }

    public void setTitle(CharSequence title) {
        mTitle = title;
        tvTitle.setText(title);
    }

}
