package com.cncoderx.andhub.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.cncoderx.andhub.BR;
import com.cncoderx.andhub.R;
import com.cncoderx.andhub.databinding.GistActivityBinding;
import com.cncoderx.andhub.model.Comment;
import com.cncoderx.andhub.model.Gist;
import com.cncoderx.andhub.okhttp.ServiceGenerator;
import com.cncoderx.andhub.okhttp.service.IGistService;
import com.cncoderx.andhub.ui.adapter.BindingAdapter;
import com.cncoderx.andhub.utils.IntentExtra;
import com.cncoderx.andhub.utils.Pagination;
import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * @author cncoderx
 */
public class GistCommentActivity extends RecyclerViewActivity {
    private GistActivityBinding mBinding;
    private BindingAdapter<Comment> mAdapter = new BindingAdapter<>(R.layout.item_comment_list, BR.comment);
    private Pagination<Comment> mPagination = new Pagination<>(mAdapter, new Pagination.Factory<Comment>() {
        IGistService mService = ServiceGenerator.create(IGistService.class);

        @Override
        public Single<List<Comment>> create(int pageIndex, int pageSize) {
            return mService.getComments(mId, pageIndex, pageSize);
        }
    });
    private String mId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mId = getIntent().getStringExtra(IntentExtra.KEY_ID);
        setAdapter(mAdapter);
        newServiceCall();
        mPagination.setTransformer(this.<List<Comment>>bindToLifecycle());
    }

    private void newServiceCall() {
        ServiceGenerator.create(IGistService.class)
                .getGist(mId)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Gist>bindToLifecycle())
                .subscribe(new Consumer<Gist>() {
                    @Override
                    public void accept(Gist gist) throws Exception {
                        mBinding = DataBindingUtil.inflate(LayoutInflater.from(GistCommentActivity.this),
                                R.layout.gist_activity, getRecyclerView(), false);
                        RecyclerViewHelper.addHeaderView(getRecyclerView(), mBinding.getRoot());
                        mBinding.setGist(gist);
                        mPagination.newPage();
                    }
                });
    }

    public void onComment(View v) {
    }
}
