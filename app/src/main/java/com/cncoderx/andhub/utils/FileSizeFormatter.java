package com.cncoderx.andhub.utils;

import android.databinding.BindingAdapter;
import android.text.format.Formatter;
import android.widget.TextView;

/**
 * @author cncoderx
 */
public class FileSizeFormatter {

    @BindingAdapter("fileSize")
    public static void setFileSize(TextView view, long size) {
        view.setText(Formatter.formatShortFileSize(view.getContext(), size));
    }
}
