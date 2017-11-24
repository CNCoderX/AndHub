package com.cncoderx.github.ui.adapter;

import android.text.format.Formatter;
import android.widget.TextView;

import com.cncoderx.github.R;
import com.cncoderx.github.sdk.model.Gist;
import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;

import java.util.Collection;
import java.util.Map;

/**
 * @author cncoderx
 */
public class GistFileListAdapter extends ObjectAdapter<Map.Entry<String, Gist.File>> {

    public GistFileListAdapter() {
        super(R.layout.item_file_list);
    }

    public GistFileListAdapter(Map.Entry<String, Gist.File>... data) {
        super(R.layout.item_file_list, data);
    }

    public GistFileListAdapter(Collection<? extends Map.Entry<String, Gist.File>> data) {
        super(R.layout.item_file_list, data);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, Map.Entry<String, Gist.File> file, int position) {
        holder.getView(R.id.tv_file_name, TextView.class).setText(file.getKey());
        holder.getView(R.id.tv_file_size, TextView.class).setText(
                Formatter.formatShortFileSize(holder.itemView.getContext(), file.getValue().size));
    }
}
