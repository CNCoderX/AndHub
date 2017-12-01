package com.cncoderx.github.ui.adapter;

import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cncoderx.github.R;
import com.cncoderx.github.accounts.GitAccount;
import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;

import java.util.Collection;

/**
 * @author cncoderx
 */
public class AccountDropListAdapter extends ObjectAdapter<GitAccount> {

    public AccountDropListAdapter() {
        super(R.layout.item_account_drop_list);
    }

    public AccountDropListAdapter(@NonNull GitAccount... objects) {
        super(R.layout.item_account_drop_list, objects);
    }

    public AccountDropListAdapter(@NonNull Collection<? extends GitAccount> objects) {
        super(R.layout.item_account_drop_list, objects);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, GitAccount account, int position) {
        holder.getView(R.id.tv_account_name, TextView.class).setText(account.getName());
        Glide.with(holder.itemView.getContext()).load(account.getAvatar()).into(
                holder.getView(R.id.iv_account_avatar, ImageView.class));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
