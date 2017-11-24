package com.cncoderx.github.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.cncoderx.github.R;
import com.cncoderx.github.sdk.model.PullRequest;
import com.cncoderx.github.utils.TimeFormatter;
import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;

import java.util.Collection;

/**
 * @author cncoderx
 */
public class PullsListAdapter extends ObjectAdapter<PullRequest> {

    public PullsListAdapter() {
        super(R.layout.item_pulls_list);
    }

    public PullsListAdapter(PullRequest... objects) {
        super(R.layout.item_pulls_list, objects);
    }

    public PullsListAdapter(Collection<? extends PullRequest> objects) {
        super(R.layout.item_pulls_list, objects);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, PullRequest pullRequest, int position) {
        holder.getView(R.id.tv_repo_detail_pulls_title, TextView.class).setText(pullRequest.title);
        holder.getView(R.id.tv_repo_detail_pulls_number, TextView.class).setText("#" + pullRequest.number);
        if (pullRequest.createdAt != null) {
            String created = TimeFormatter.format(holder.itemView.getContext(), pullRequest.createdAt);
            String opened = holder.itemView.getContext().getString(R.string.opened_on) + " " + created;
            holder.getView(R.id.tv_repo_detail_pulls_opened, TextView.class).setText(opened);
        }
        if (pullRequest.comments == 0) {
            holder.getView(R.id.ll_repo_detail_pulls_comment).setVisibility(View.INVISIBLE);
        } else {
            holder.getView(R.id.ll_repo_detail_pulls_comment).setVisibility(View.VISIBLE);
            holder.getView(R.id.tv_repo_detail_pulls_comment, TextView.class).setText(Integer.toString(pullRequest.comments));
        }
    }
}
