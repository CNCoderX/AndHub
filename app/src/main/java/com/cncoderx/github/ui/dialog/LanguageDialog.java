package com.cncoderx.github.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.cncoderx.github.AppContext;
import com.cncoderx.github.R;
import com.cncoderx.github.preference.SettingPreference;
import com.cncoderx.github.ui.activity.MainActivity;

import java.util.Locale;

/**
 * @author cncoderx
 */
public class LanguageDialog extends DialogFragment implements DialogInterface.OnClickListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int index = new SettingPreference(getContext()).getLanguage();
        return new AlertDialog.Builder(getContext()).setSingleChoiceItems(
                R.array.languages, index, this).create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case 0:
                new SettingPreference(getContext()).setLanguage(0).apply();
                AppContext.getInstance().setLocale(Locale.getDefault());
                restartApplication();
                break;
            case 1:
                new SettingPreference(getContext()).setLanguage(1).apply();
                AppContext.getInstance().setLocale(Locale.ENGLISH);
                restartApplication();
                break;
            case 2:
                new SettingPreference(getContext()).setLanguage(2).apply();
                AppContext.getInstance().setLocale(Locale.SIMPLIFIED_CHINESE);
                restartApplication();
                break;
            case 3:
                new SettingPreference(getContext()).setLanguage(3).apply();
                AppContext.getInstance().setLocale(Locale.TRADITIONAL_CHINESE);
                restartApplication();
                break;
        }
    }

    private void restartApplication() {
        dismissAllowingStateLoss();

        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(0);
    }
}
