package com.cncoderx.andhub.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cncoderx.andhub.R;
import com.cncoderx.andhub.databinding.AboutActivityBinding;
import com.cncoderx.andhub.model.About;

/**
 * @author cncoderx
 */
public class AboutActivity extends BaseActivity {
    private AboutActivityBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.about_activity);
        mBinding.setAbout(new About());
    }
}
