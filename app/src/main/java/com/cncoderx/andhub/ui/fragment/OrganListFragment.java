package com.cncoderx.andhub.ui.fragment;

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
import com.cncoderx.andhub.ui.activity.ProfileActivity;
import com.cncoderx.andhub.ui.adapter.BindingAdapter;
import com.cncoderx.andhub.utils.IntentExtra;
import com.cncoderx.andhub.utils.Pagination;
import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;

import java.util.List;

import io.reactivex.Single;

/**
 * @author cncoderx
 */
public class OrganListFragment extends RecyclerViewFragment implements OnItemClickListener {
    private BindingAdapter<User> mAdapter = new BindingAdapter<>(R.layout.item_user_list, BR.user);
    private Pagination<User> mPagination = new Pagination<>(mAdapter, new Pagination.Factory<User>() {
        IUserService mService = ServiceGenerator.create(IUserService.class);

        @Override
        public Single<List<User>> create(int pageIndex, int pageSize) {
            return mService.getOrgans(mUser, pageIndex, Integer.MAX_VALUE);
        }
    });
    private String mUser;

    public static OrganListFragment create(String user) {
        OrganListFragment fragment = new OrganListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IntentExtra.KEY_USER, user);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUser = getArguments().getString(IntentExtra.KEY_USER);
        setAdapter(mAdapter);
        setOnItemClickListener(this);
        setEmptyText(getString(R.string.no_organs));
        mPagination.bind(getEmptyView());
        mPagination.setTransformer(this.<List<User>>bindToLifecycle());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getRefreshLayout().setRefreshing(true);
        mPagination.bind(getRefreshLayout());
        mPagination.newPage();
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
        User organ = mAdapter.get(position);
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra(IntentExtra.KEY_USER, organ.login);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        mPagination.bind(getRefreshLayout());
        mPagination.newPage();
    }
}
