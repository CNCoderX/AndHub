package com.cncoderx.andhub.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
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

import java.util.List;

import io.reactivex.Single;

/**
 * @author cncoderx
 */
public class MemberListActivity extends RecyclerViewActivity implements OnItemClickListener {
    private BindingAdapter<User> mAdapter = new BindingAdapter<>(R.layout.item_user_list, BR.user);
    private Pagination<User> mPagination = new Pagination<>(mAdapter, new Pagination.Factory<User>() {
        IUserService mService = ServiceGenerator.create(IUserService.class);

        @Override
        public Single<List<User>> create(int pageIndex, int pageSize) {
            return mService.getOrganMembers(mOrg, pageIndex, pageSize);
        }
    });
    private String mOrg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOrg = getIntent().getStringExtra(IntentExtra.KEY_USER);
        setAdapter(mAdapter);
        setOnItemClickListener(this);
        setEmptyText(getString(R.string.no_members));
        mPagination.bind(getEmptyView());
        mPagination.bind(getRefreshLayout());
        mPagination.setTransformer(this.<List<User>>bindToLifecycle());
        mPagination.newPage();
        getRefreshLayout().setRefreshing(true);
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
        User user = mAdapter.get(position);
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(IntentExtra.KEY_USER, user.login);
        startActivity(intent);
    }
}
