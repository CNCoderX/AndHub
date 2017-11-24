package com.cncoderx.github.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.cncoderx.github.R;
import com.cncoderx.github.sdk.ServiceGenerator;
import com.cncoderx.github.sdk.model.User;
import com.cncoderx.github.sdk.service.IUserService;
import com.cncoderx.github.ui.adapter.UserListAdapter;
import com.cncoderx.github.utils.IntentExtra;
import com.cncoderx.github.utils.ListCallback;
import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;
import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;
import com.cncoderx.recyclerviewhelper.listener.OnLoadMoreListener;
import com.cncoderx.recyclerviewhelper.utils.ILoadingView;

import java.util.List;

import retrofit2.Call;

/**
 * @author cncoderx
 */
public class FollowingListActivity extends RecyclerViewActivity
        implements OnItemClickListener, OnLoadMoreListener {
    private UserListAdapter mAdapter = new UserListAdapter();
    private ListCallback<User> mCallback = new ListCallback<>(mAdapter);
    private String user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAdapter(mAdapter);
        setOnItemClickListener(this);
        RecyclerViewHelper.setLoadMoreListener(
                getRecyclerView(), R.layout.item_loading_more, this);

        user = getIntent().getStringExtra(IntentExtra.KEY_USER);
        if (TextUtils.isEmpty(user)) {
            IUserService service = ServiceGenerator.create(IUserService.class);
            Call<List<User>> call = service.getFollowing();
            mCallback.queueTo(call, true);
        } else {
            IUserService service = ServiceGenerator.create(IUserService.class);
            Call<List<User>> call = service.getFollowing(user);
            mCallback.queueTo(call, true);
        }
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
        User user = mAdapter.get(position);
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(IntentExtra.KEY_USER, user.login);
        startActivity(intent);
    }

    @Override
    public void load(RecyclerView recyclerView, ILoadingView loadingView) {
        if (TextUtils.isEmpty(user)) {
            IUserService service = ServiceGenerator.create(IUserService.class);
            Call<List<User>> call = service.getFollowing();
            mCallback.setLoadingView(loadingView).queueTo(call, false);
        } else {
            IUserService service = ServiceGenerator.create(IUserService.class);
            Call<List<User>> call = service.getFollowing(user);
            mCallback.setLoadingView(loadingView).queueTo(call, false);
        }
    }
}
