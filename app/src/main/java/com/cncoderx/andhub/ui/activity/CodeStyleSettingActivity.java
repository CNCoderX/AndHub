package com.cncoderx.andhub.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.cncoderx.andhub.R;
import com.cncoderx.andhub.databinding.CodeStyleSettingActivityBinding;
import com.cncoderx.andhub.preference.SettingPreference;
import com.cncoderx.andhub.ui.dialog.CodeFontSizeSettingDialog;
import com.cncoderx.andhub.ui.dialog.CodeThemeSettingDialog;

/**
 * @author cncoderx
 */
public class CodeStyleSettingActivity extends BaseActivity {
    private CodeThemeSettingDialog mCodeThemeSettingDialog;
    private CodeFontSizeSettingDialog mCodeFontSizeSettingDialog;
    private CodeStyleSettingActivityBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.code_style_setting_activity);
        mBinding.setSetting(new SettingPreference(this));

        mCodeThemeSettingDialog = new CodeThemeSettingDialog();
        mCodeFontSizeSettingDialog = new CodeFontSizeSettingDialog();
    }

    public void onCodeTheme(View v) {
        mCodeThemeSettingDialog.show(getSupportFragmentManager(), "");
    }

    public void onCodeFontSize(View v) {
        mCodeFontSizeSettingDialog.show(getSupportFragmentManager(), "");
    }

    public void invalidateAll() {
        mBinding.invalidateAll();
    }
}
