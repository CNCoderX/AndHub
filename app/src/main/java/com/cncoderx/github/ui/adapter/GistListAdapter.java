package com.cncoderx.github.ui.adapter;

import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cncoderx.github.R;
import com.cncoderx.github.sdk.model.Gist;
import com.cncoderx.github.utils.TimeFormatter;
import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;

import java.util.Collection;

/**
 * @author cncoderx
 */
public class GistListAdapter extends ObjectAdapter<Gist> {

    public GistListAdapter() {
        super(R.layout.item_gist_list);
    }

    public GistListAdapter(@NonNull Gist... objects) {
        super(R.layout.item_gist_list, objects);
    }

    public GistListAdapter(@NonNull Collection<? extends Gist> objects) {
        super(R.layout.item_gist_list, objects);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder viewHolder, Gist gist, int position) {
//        viewHolder.getView(R.id.tv_gist_file, TextView.class).setText(getFirstFile(gist));
        viewHolder.getView(R.id.tv_gist_desc, TextView.class).setText(gist.description);
        viewHolder.getView(R.id.tv_gist_created, TextView.class).setText(
                viewHolder.itemView.getContext().getString(R.string.created_on) + " " +
                TimeFormatter.format(viewHolder.itemView.getContext(), gist.createdAt));
        if (gist.owner == null) {
            viewHolder.getView(R.id.iv_gist_avatar, ImageView.class).setImageResource(R.mipmap.identicon);
        } else {
            Glide.with(viewHolder.itemView.getContext()).load(gist.owner.avatarUrl).into(
                    viewHolder.getView(R.id.iv_gist_avatar, ImageView.class));
        }

        viewHolder.getView(R.id.tv_gist_files, TextView.class).setText(
                Integer.toString(gist.files == null ? 0 : gist.files.size()));
//        viewHolder.getView(R.id.tv_gist_forks, TextView.class).setText(
//                Integer.toString(gist.forks == null ? 0 : gist.forks.length));
        viewHolder.getView(R.id.tv_gist_comments, TextView.class).setText(
                Integer.toString(gist.comments));
//        viewHolder.getView(R.id.tv_gist_stars, TextView.class).setText(
//                Integer.toString(gist.files == null ? 0 : gist.files.size()));
    }

//    private String getFirstFile(Gist gist) {
//        if (gist.files == null || gist.files.size() == 0)
//            return null;
//
//        String file = null;
//        Iterator<String> iterator = gist.files.keySet().iterator();
//        if (iterator.hasNext()) {
//            file = iterator.next();
//        }
//        return file;
//    }
}
