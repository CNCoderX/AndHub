package com.cncoderx.github.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.cncoderx.github.R;
import com.cncoderx.github.preference.SettingPreference;
import com.cncoderx.github.ui.dialog.LanguageDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author cncoderx
 */
public class SettingActivity extends BaseActivity {
    @BindView(R.id.tv_setting_language_value)
    TextView tvLanguage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        SettingPreference preference = new SettingPreference(this);
        int lang = preference.getLanguage();
        String language = getResources().getStringArray(R.array.languages)[lang];
        tvLanguage.setText(language);
    }

    @OnClick(R.id.ll_setting_language)
    public void onLanguageClick(View v) {
        new LanguageDialog().show(getSupportFragmentManager(), "");
    }
}
