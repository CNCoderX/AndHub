package com.cncoderx.github.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;

import com.cncoderx.github.R;

/**
 * @author cncoderx
 */
public class LoadingDialog extends Dialog {
    private static LoadingDialog loadingDialog;

    public static void show(Context context) {
        close();

        LoadingDialog dialog = new LoadingDialog(context);
        dialog.setCancelable(false);
        dialog.show();

        loadingDialog = dialog;
    }

    public static void close() {
        if (loadingDialog != null) {
            if (loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
            loadingDialog = null;
        }
    }

    private LoadingDialog(Context context) {
        super(context, R.style.loading_dialog);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        setContentView(R.layout.dialog_loading);
    }
}
