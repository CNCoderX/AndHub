package com.cncoderx.github.ui.adapter;

import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cncoderx.github.R;
import com.cncoderx.github.sdk.model.Comment;
import com.cncoderx.github.utils.TimeFormatter;
import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;

import java.util.Collection;

/**
 * @author cncoderx
 */
public class CommentAdapter extends ObjectAdapter<Comment> {

    public CommentAdapter() {
        super(R.layout.item_comment_list);
    }

    public CommentAdapter(@NonNull Comment... objects) {
        super(R.layout.item_comment_list, objects);
    }

    public CommentAdapter(@NonNull Collection<? extends Comment> objects) {
        super(R.layout.item_comment_list, objects);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder viewHolder, Comment comment, int position) {
        Glide.with(viewHolder.itemView.getContext())
                .load(comment.user.avatarUrl)
                .into(viewHolder.getView(R.id.iv_comment_user_avatar, ImageView.class));

        viewHolder.getView(R.id.tv_comment_user_name, TextView.class).setText(comment.user.login);
        viewHolder.getView(R.id.tv_comment_body, TextView.class).setText(comment.body);
        viewHolder.getView(R.id.tv_comment_updated, TextView.class).setText(
                TimeFormatter.format(viewHolder.itemView.getContext(), comment.createdAt));
    }
}
