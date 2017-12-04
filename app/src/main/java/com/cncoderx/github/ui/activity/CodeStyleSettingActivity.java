package com.cncoderx.github.ui.activity;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.cncoderx.github.R;
import com.cncoderx.github.preference.SettingPreference;
import com.cncoderx.github.ui.dialog.CodeFontSizeSettingDialog;
import com.cncoderx.github.ui.dialog.CodeThemeSettingDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * @author cncoderx
 */
public class CodeStyleSettingActivity extends BaseActivity {
    @BindView(R.id.tv_code_style_setting_theme)
    TextView tvTheme;

    @BindView(R.id.tv_code_style_setting_font)
    TextView tvFontSize;

    @BindView(R.id.swt_code_style_setting_wrapping)
    Switch swtWrapping;

    @BindView(R.id.swt_code_style_setting_numbers)
    Switch swtNumbers;

    private CodeThemeSettingDialog mCodeThemeSettingDialog;
    private CodeFontSizeSettingDialog mCodeFontSizeSettingDialog;

    private SettingPreference preference;
    private boolean postInited = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_style_setting);
        ButterKnife.bind(this);
        mCodeThemeSettingDialog = new CodeThemeSettingDialog();
        mCodeThemeSettingDialog.setOnClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String[] themes = getResources().getStringArray(R.array.code_themes);
                tvTheme.setText(themes[which]);
            }
        });
        mCodeFontSizeSettingDialog = new CodeFontSizeSettingDialog();
        mCodeFontSizeSettingDialog.setOnClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String[] fontSizes = getResources().getStringArray(R.array.code_font_sizes);
                tvFontSize.setText(fontSizes[which]);
            }
        });
        init();
    }

    private void init() {
        preference = new SettingPreference(this);
        int theme = preference.getCodeThemes();
        int font = preference.getCodeFontSize();
        boolean lineWrapping = preference.isLineWrapping();
        boolean lineNumbers = preference.isLineNumbers();
        Resources resources = getResources();
        String codeTheme = resources.getStringArray(R.array.code_themes)[theme];
        String fontSize = resources.getStringArray(R.array.code_font_sizes)[font];
        tvTheme.setText(codeTheme);
        tvFontSize.setText(fontSize);
        swtWrapping.setChecked(lineWrapping);
        swtNumbers.setChecked(lineNumbers);
        postInited = true;
    }

    @OnClick(R.id.ll_code_style_setting_theme)
    public void onCodeStyleClick(View v) {
        mCodeThemeSettingDialog.show(getSupportFragmentManager(), getString(R.string.theme));
    }

    @OnClick(R.id.ll_code_style_setting_font)
    public void onCodeFontSizeClick(View v) {
        mCodeFontSizeSettingDialog.show(getSupportFragmentManager(), getString(R.string.font_size));
    }

    @OnCheckedChanged(R.id.swt_code_style_setting_wrapping)
    public void onWrappingChanged(CompoundButton button, boolean isChecked) {
        if (postInited)
            preference.setLineWrapping(isChecked).apply();
    }

    @OnCheckedChanged(R.id.swt_code_style_setting_numbers)
    public void onLineNumsChanged(CompoundButton button, boolean isChecked) {
        if (postInited)
            preference.setLineNumbers(isChecked).apply();
    }
}
