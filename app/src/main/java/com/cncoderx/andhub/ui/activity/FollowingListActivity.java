package com.cncoderx.andhub.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.cncoderx.andhub.BR;
import com.cncoderx.andhub.R;
import com.cncoderx.andhub.model.User;
import com.cncoderx.andhub.okhttp.ServiceGenerator;
import com.cncoderx.andhub.okhttp.service.IUserService;
import com.cncoderx.andhub.ui.adapter.BindingAdapter;
import com.cncoderx.andhub.utils.IntentExtra;
import com.cncoderx.andhub.utils.Pagination;
import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;
import com.cncoderx.recyclerviewhelper.listener.OnLoadMoreListener;
import com.cncoderx.recyclerviewhelper.utils.ILoadingView;

import java.util.List;

import io.reactivex.Single;

/**
 * @author cncoderx
 */
public class FollowingListActivity extends RecyclerViewActivity
        implements OnItemClickListener, OnLoadMoreListener {
    private BindingAdapter<User> mAdapter = new BindingAdapter<>(R.layout.item_user_list, BR.user);
    private Pagination<User> mPagination = new Pagination<>(mAdapter, new PaginationFactory());
    private String mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAdapter(mAdapter);
        setOnItemClickListener(this);
        setOnLoadMoreListener(this);

        mUser = getIntent().getStringExtra(IntentExtra.KEY_USER);
        mPagination.setTransformer(this.<List<User>>bindToLifecycle());
        mPagination.newPage();
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
        mPagination.bind(loadingView);
        mPagination.nextPage();
    }

    private class PaginationFactory implements Pagination.Factory<User> {
        IUserService mService = ServiceGenerator.create(IUserService.class);

        @Override
        public Single<List<User>> create(int pageIndex, int pageSize) {
            final String user = mUser;
            if (TextUtils.isEmpty(user)) {
                return mService.getFollowing(pageIndex, pageSize);
            } else {
                return mService.getFollowing(user, pageIndex, pageSize);
            }
        }
    }
}
