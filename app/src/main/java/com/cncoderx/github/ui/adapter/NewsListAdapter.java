package com.cncoderx.github.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cncoderx.github.R;
import com.cncoderx.github.sdk.model.Event;
import com.cncoderx.github.sdk.model.EventType;
import com.cncoderx.github.utils.EventMessageFactoryImpl;
import com.cncoderx.github.utils.TimeFormatter;
import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;

import java.util.Collection;

/**
 * @author cncoderx
 */
public class NewsListAdapter extends ObjectAdapter<Event> {

    public NewsListAdapter() {
        super(R.layout.item_news_list);
    }

    public NewsListAdapter(@NonNull Event... objects) {
        super(R.layout.item_news_list, objects);
    }

    public NewsListAdapter(@NonNull Collection<? extends Event> objects) {
        super(R.layout.item_news_list, objects);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder viewHolder, Event event, int position) {
        Context context = viewHolder.itemView.getContext();
        TextView tvTitle = viewHolder.getView(R.id.tv_news_title, TextView.class);
        EventType type = event.type;
        if (type != null) {
            tvTitle.setText(type.getMessage(context, event, new EventMessageFactoryImpl()));
            tvTitle.setMovementMethod(LinkMovementMethod.getInstance());
            tvTitle.setHighlightColor(Color.TRANSPARENT);
        } else {
            tvTitle.setText("");
        }
        viewHolder.getView(R.id.tv_news_created, TextView.class).setText(
                TimeFormatter.format(viewHolder.itemView.getContext(), event.createdAt));
        Glide.with(context).load(event.actor.avatarUrl).into(viewHolder.getView(R.id.iv_news_avatar, ImageView.class));
    }
}
