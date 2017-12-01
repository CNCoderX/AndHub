package com.cncoderx.github.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.cncoderx.github.AppContext;
import com.cncoderx.github.R;
import com.cncoderx.github.preference.SettingPreference;

/**
 * @author cncoderx
 */
public class LanguageSettingDialog extends DialogFragment implements DialogInterface.OnClickListener {
    private SettingPreference mPreference;
    private DialogInterface.OnClickListener mOnClickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreference = new SettingPreference(getContext());
    }

    public void setOnClickListener(DialogInterface.OnClickListener listener) {
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int which = mPreference.getLanguage();
        return new AlertDialog.Builder(getContext()).setSingleChoiceItems(
                R.array.languages, which, this).create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        mPreference.setLanguage(which).apply();
        AppContext.getInstance().setLocale(mPreference.getLocale());
        if (mOnClickListener != null)
            mOnClickListener.onClick(dialog, which);
    }
}
