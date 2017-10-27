package com.cncoderx.github.ui.adapter;

import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.cncoderx.github.R;
import com.cncoderx.github.entites.Repositories;
import com.cncoderx.github.utils.TimeUtils;
import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;

import java.util.Collection;
import java.util.Date;
import java.util.Locale;

/**
 * @author cncoderx
 */
public class RepositoryAdapter extends ObjectAdapter<Repositories.Item> {

    public RepositoryAdapter() {
        super(R.layout.item_repository_layout);
    }

    public RepositoryAdapter(Repositories.Item... data) {
        super(R.layout.item_repository_layout, data);
    }

    public RepositoryAdapter(Collection<? extends Repositories.Item> data) {
        super(R.layout.item_repository_layout, data);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, Repositories.Item repository, int position) {
        String name = repository.getFullName();
        holder.getView(R.id.tv_repository_name, TextView.class).setText(name);

        String desc = repository.getDescription();
        if (TextUtils.isEmpty(desc)) {
            holder.getView(R.id.tv_repository_desc, TextView.class).setText("");
        } else {
            holder.getView(R.id.tv_repository_desc, TextView.class).setText(toHtml(desc));
        }

        Date date = repository.getPushDate();
        if (date == null) date = repository.getCreateDate();
        if (date != null) {
            String updated = TimeUtils.formatDate(date/*repository.getUpdateDate()*/);
            holder.getView(R.id.tv_repository_updated, TextView.class).setText("Updated " + updated);
        }

        String language = repository.getLanguage();
        if (TextUtils.isEmpty(language)) {
            holder.getView(R.id.tv_repository_language, TextView.class).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.tv_repository_language, TextView.class).setText(language);
            holder.getView(R.id.tv_repository_language, TextView.class).setVisibility(View.VISIBLE);
        }

        String stars = formatCount(repository.getStarCount());
        holder.getView(R.id.tv_repository_stars, TextView.class).setText(stars);
    }

    Spanned toHtml(String str) {
        if (TextUtils.isEmpty(str))
            return new SpannableString("");
        return Html.fromHtml(str);
    }

    String formatCount(int count) {
        if (count > 1000) {
            float f = count / 1000f;
            if (f - count / 1000 == 0) {
                return String.format(Locale.US, "%dk", (int) f);
            } else {
                return String.format(Locale.US, "%.1fk", f);
            }
        }
        return Integer.toString(count);
    }
}
