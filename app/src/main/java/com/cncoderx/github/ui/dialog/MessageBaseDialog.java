package com.cncoderx.github.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * @author cncoderx
 */
public class MessageBaseDialog extends DialogFragment {
    private CharSequence message;
    private CharSequence button;
    private DialogInterface.OnClickListener listener;

    public static MessageBaseDialog newInstance(CharSequence message,
                                                CharSequence button,
                                                DialogInterface.OnClickListener listener) {
        MessageBaseDialog dialog = new MessageBaseDialog();
        dialog.message = message;
        dialog.button = button;
        dialog.listener = listener;
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton(button, listener)
                .create();
    }
}
