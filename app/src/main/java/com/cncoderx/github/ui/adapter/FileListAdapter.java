package com.cncoderx.github.ui.adapter;

import android.text.format.Formatter;
import android.widget.TextView;

import com.cncoderx.github.R;
import com.cncoderx.github.sdk.model.Contents;
import com.cncoderx.github.ui.view.IconView;
import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;

import java.util.Collection;

/**
 * @author cncoderx
 */
public class FileListAdapter extends ObjectAdapter<Contents> {

    public FileListAdapter() {
        super(R.layout.item_file_list);
    }

    public FileListAdapter(Contents... data) {
        super(R.layout.item_file_list, data);
    }

    public FileListAdapter(Collection<? extends Contents> data) {
        super(R.layout.item_file_list, data);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, Contents contents, int position) {
        if (contents.type != null && contents.type.equals(Contents.TYPE_DIRECTION)) {
            holder.getView(R.id.iv_file_icon, IconView.class).setText(R.string.ic_dir);
            holder.getView(R.id.tv_file_name, TextView.class).setText(contents.name);
            holder.getView(R.id.tv_file_size, TextView.class).setText("");
        } else {
            holder.getView(R.id.iv_file_icon, IconView.class).setText(R.string.ic_file);
            holder.getView(R.id.tv_file_name, TextView.class).setText(contents.name);
            holder.getView(R.id.tv_file_size, TextView.class).setText(
                    Formatter.formatShortFileSize(holder.itemView.getContext(), contents.size));
        }
    }
}
