package com.cncoderx.github.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cncoderx.github.R;
import com.cncoderx.github.sdk.ServiceGenerator;
import com.cncoderx.github.sdk.model.Gist;
import com.cncoderx.github.sdk.service.IGistService;
import com.cncoderx.github.ui.adapter.GistListAdapter;
import com.cncoderx.github.utils.IntentExtra;
import com.cncoderx.github.utils.ListCallback;
import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;

import java.util.List;

import retrofit2.Call;

/**
 * @author cncoderx
 */
public class GistListActivity extends SwipeRecyclerViewActivity implements OnItemClickListener {
    private GistListAdapter mAdapter = new GistListAdapter();
    private ListCallback<Gist> mCallback = new ListCallback<>(mAdapter, false);
    private String mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = getIntent().getStringExtra(IntentExtra.KEY_USER);
        setAdapter(mAdapter);
        setOnItemClickListener(this);
        setEmptyText(getString(R.string.no_gists));
        mCallback.setEmptyView(getEmptyView());

        getSwipeRefreshLayout().setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        IGistService service = ServiceGenerator.create(IGistService.class);
        Call<List<Gist>> call = service.getGists(mUser);
        mCallback.setRefreshView(getSwipeRefreshLayout()).queueTo(call, true);
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
        Gist gist = mAdapter.get(position);
        Intent intent = new Intent(this, GistActivity.class);
        intent.putExtra(IntentExtra.KEY_ID, gist.id);
        startActivity(intent);
    }
}
