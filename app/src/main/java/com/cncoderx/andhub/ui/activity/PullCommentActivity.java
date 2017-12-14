package com.cncoderx.andhub.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.cncoderx.andhub.BR;
import com.cncoderx.andhub.R;
import com.cncoderx.andhub.databinding.ItemPullHeaderBinding;
import com.cncoderx.andhub.model.Comment;
import com.cncoderx.andhub.model.PullRequest;
import com.cncoderx.andhub.okhttp.ServiceGenerator;
import com.cncoderx.andhub.okhttp.service.IPullsService;
import com.cncoderx.andhub.ui.adapter.BindingAdapter;
import com.cncoderx.andhub.utils.IntentExtra;
import com.cncoderx.andhub.utils.Pagination;
import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;
import com.cncoderx.recyclerviewhelper.listener.OnLoadMoreListener;
import com.cncoderx.recyclerviewhelper.utils.ILoadingView;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author cncoderx
 */
public class PullCommentActivity extends RecyclerViewActivity implements OnLoadMoreListener {
    private ItemPullHeaderBinding mBinding;
    private BindingAdapter<Comment> mAdapter = new BindingAdapter<>(R.layout.item_comment_list, BR.comment);
    private Pagination<Comment> mPagination = new Pagination<>(mAdapter, new Pagination.Factory<Comment>() {
        IPullsService mService = ServiceGenerator.create(IPullsService.class);

        @Override
        public Single<List<Comment>> create(int pageIndex, int pageSize) {
            return mService.getComments(mOwner, mRepo, Integer.toString(mNumber), pageIndex, pageSize);
        }
    });

    private String mOwner, mRepo;
    private int mNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mOwner = intent.getStringExtra(IntentExtra.KEY_OWNER);
        mRepo = intent.getStringExtra(IntentExtra.KEY_REPO);
        mNumber = intent.getIntExtra(IntentExtra.KEY_NUMBER, 0);

        setAdapter(mAdapter);
        setOnLoadMoreListener(this);

        mPagination.setTransformer(this.<List<Comment>>bindToLifecycle());
        newServiceCall();
    }

    private void newServiceCall() {
        ServiceGenerator.create(IPullsService.class)
                .getPullRequest(mOwner, mRepo, Integer.toString(mNumber))
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<PullRequest, Comment>() {
                    @Override
                    public Comment apply(@NonNull PullRequest pullRequest) throws Exception {
                        mBinding = DataBindingUtil.inflate(
                                LayoutInflater.from(PullCommentActivity.this),
                                R.layout.item_pull_header, getRecyclerView(), false);
                        mBinding.setPullRequest(pullRequest);
                        RecyclerViewHelper.addHeaderView(getRecyclerView(), mBinding.getRoot());

                        Comment comment = new Comment();
                        comment.user = pullRequest.user;
                        comment.body = pullRequest.body;
                        comment.createdAt = pullRequest.createdAt;
                        comment.updatedAt = pullRequest.updatedAt;
                        return comment;
                    }
                })
                .compose(this.<Comment>bindToLifecycle())
                .subscribe(new Consumer<Comment>() {
                    @Override
                    public void accept(Comment comment) throws Exception {
                        mAdapter.add(0, comment);
                        mPagination.newPage();
                    }
                });
    }

    @Override
    public void load(RecyclerView recyclerView, ILoadingView view) {
        mPagination.bind(view);
        mPagination.nextPage();
    }
}