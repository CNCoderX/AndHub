package com.cncoderx.github.ui.adapter;

import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cncoderx.github.R;
import com.cncoderx.github.sdk.model.User;
import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;

import java.util.Collection;

/**
 * @author cncoderx
 */
public class UserListAdapter extends ObjectAdapter<User> {

    public UserListAdapter() {
        super(R.layout.item_user_list);
    }

    public UserListAdapter(@NonNull User... objects) {
        super(R.layout.item_user_list, objects);
    }

    public UserListAdapter(@NonNull Collection<? extends User> objects) {
        super(R.layout.item_user_list, objects);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder viewHolder, User user, int position) {
        Glide.with(viewHolder.itemView.getContext()).load(user.avatarUrl).into(
                viewHolder.getView(R.id.iv_user_avatar, ImageView.class));
        viewHolder.getView(R.id.tv_user_name, TextView.class).setText(user.login);
    }
}
