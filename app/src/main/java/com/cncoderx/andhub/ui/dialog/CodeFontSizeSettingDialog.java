package com.cncoderx.andhub.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.cncoderx.andhub.R;
import com.cncoderx.andhub.preference.SettingPreference;
import com.cncoderx.andhub.ui.activity.CodeStyleSettingActivity;

/**
 * @author cncoderx
 */
public class CodeFontSizeSettingDialog extends DialogFragment implements DialogInterface.OnClickListener {
    private SettingPreference mPreference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreference = new SettingPreference(getContext());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int which = mPreference.getCodeFontSize();
        return new AlertDialog.Builder(getContext()).setSingleChoiceItems(
                R.array.code_font_sizes, which, this).create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        mPreference.setCodeFontSize(which).apply();
        CodeStyleSettingActivity activity = (CodeStyleSettingActivity) getActivity();
        if (activity != null) {
            activity.invalidateAll();
        }
        dismiss();
    }
}
