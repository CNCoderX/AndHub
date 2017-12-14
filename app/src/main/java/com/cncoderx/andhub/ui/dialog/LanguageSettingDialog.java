package com.cncoderx.andhub.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.cncoderx.andhub.AppContext;
import com.cncoderx.andhub.R;
import com.cncoderx.andhub.preference.SettingPreference;
import com.cncoderx.andhub.ui.activity.MainActivity;
import com.cncoderx.andhub.ui.activity.SettingActivity;

/**
 * @author cncoderx
 */
public class LanguageSettingDialog extends DialogFragment implements DialogInterface.OnClickListener {
    private SettingPreference mPreference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreference = new SettingPreference(getContext());
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
        SettingActivity activity = (SettingActivity) getActivity();
        if (activity != null) {
            activity.invalidateAll();
        }
        dismiss();
        restartApplication();
    }

    private void restartApplication() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(0);
    }

}
