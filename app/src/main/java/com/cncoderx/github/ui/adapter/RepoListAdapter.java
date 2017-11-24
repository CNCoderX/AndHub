package com.cncoderx.github.ui.adapter;

import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.cncoderx.github.R;
import com.cncoderx.github.sdk.model.Repository;
import com.cncoderx.github.utils.NumberFormatter;
import com.cncoderx.github.utils.TimeFormatter;
import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;

import java.util.Collection;
import java.util.Date;

/**
 * @author cncoderx
 */
public class RepoListAdapter extends ObjectAdapter<Repository> {

    public RepoListAdapter() {
        super(R.layout.item_repo_list);
    }

    public RepoListAdapter(Repository... objects) {
        super(R.layout.item_repo_list, objects);
    }

    public RepoListAdapter(Collection<? extends Repository> objects) {
        super(R.layout.item_repo_list, objects);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, Repository repository, int position) {
        String name = repository.fullName;
        holder.getView(R.id.tv_repo_name, TextView.class).setText(name);

        String desc = repository.description;
        if (TextUtils.isEmpty(desc)) {
            holder.getView(R.id.tv_repo_desc, TextView.class).setText("");
        } else {
            holder.getView(R.id.tv_repo_desc, TextView.class).setText(toHtml(desc));
        }

        Date date = repository.pushedAt;
        if (date == null) date = repository.createdAt;
        if (date != null) {
            String updated = TimeFormatter.format(holder.itemView.getContext(), date/*repository.getUpdateDate()*/);
            holder.getView(R.id.tv_repo_updated, TextView.class).setText(
                    holder.itemView.getContext().getString(R.string.updated_on) + " " + updated);
        }

        String language = repository.language;
        if (TextUtils.isEmpty(language)) {
            holder.getView(R.id.tv_repo_language, TextView.class).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.tv_repo_language, TextView.class).setText(language);
            holder.getView(R.id.tv_repo_language, TextView.class).setVisibility(View.VISIBLE);
        }

        holder.getView(R.id.tv_repo_stars, TextView.class).setText(
                NumberFormatter.format(repository.starCount));

        holder.getView(R.id.tv_repo_forks, TextView.class).setText(
                NumberFormatter.format(repository.forkCount));
    }

    Spanned toHtml(String str) {
        if (TextUtils.isEmpty(str))
            return new SpannableString("");
        return Html.fromHtml(str);
    }
}
