package com.cncoderx.github.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.cncoderx.github.R;
import com.cncoderx.github.sdk.model.Issue;
import com.cncoderx.github.utils.TimeFormatter;
import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;

import java.util.Collection;

/**
 * @author cncoderx
 */
public class IssuesListAdapter extends ObjectAdapter<Issue> {

    public IssuesListAdapter() {
        super(R.layout.item_issues_list);
    }

    public IssuesListAdapter(Issue... objects) {
        super(R.layout.item_issues_list, objects);
    }

    public IssuesListAdapter(Collection<? extends Issue> objects) {
        super(R.layout.item_issues_list, objects);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, Issue issue, int position) {
        holder.getView(R.id.tv_repo_detail_issues_title, TextView.class).setText(issue.title);
        holder.getView(R.id.tv_repo_detail_issues_number, TextView.class).setText("#" + issue.number);
        if (issue.createdAt != null) {
            String created = TimeFormatter.format(holder.itemView.getContext(), issue.createdAt);
            String opened = holder.itemView.getContext().getString(R.string.opened_on) + " " + created;
            holder.getView(R.id.tv_repo_detail_issues_opened, TextView.class).setText(opened);
        }
        if (issue.comments == 0) {
            holder.getView(R.id.ll_repo_detail_issues_comment).setVisibility(View.INVISIBLE);
        } else {
            holder.getView(R.id.ll_repo_detail_issues_comment).setVisibility(View.VISIBLE);
            holder.getView(R.id.tv_repo_detail_issues_comment, TextView.class).setText(Integer.toString(issue.comments));
        }
    }
}
