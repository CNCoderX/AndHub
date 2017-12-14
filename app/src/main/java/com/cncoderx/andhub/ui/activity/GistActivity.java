package com.cncoderx.andhub.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.cncoderx.andhub.BR;
import com.cncoderx.andhub.R;
import com.cncoderx.andhub.databinding.GistActivityBinding;
import com.cncoderx.andhub.model.Gist;
import com.cncoderx.andhub.okhttp.ServiceGenerator;
import com.cncoderx.andhub.okhttp.service.IGistService;
import com.cncoderx.andhub.ui.adapter.BindingAdapter;
import com.cncoderx.andhub.utils.IntentExtra;
import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;
import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * @author cncoderx
 */
public class GistActivity extends RecyclerViewActivity implements OnItemClickListener {
    private BindingAdapter<Map.Entry<String, Gist.File>> mAdapter =
            new BindingAdapter<>(R.layout.item_gist_file_list, BR.file);

    private String id;
    private GistActivityBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getIntent().getStringExtra(IntentExtra.KEY_ID);
        setAdapter(mAdapter);
        setOnItemClickListener(this);
        newServiceCall();
    }

    private void newServiceCall() {
        ServiceGenerator.create(IGistService.class)
                .getGist(id)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Gist>bindToLifecycle())
                .subscribe(new Consumer<Gist>() {
                    @Override
                    public void accept(Gist gist) throws Exception {
                        mBinding = DataBindingUtil.inflate(LayoutInflater.from(GistActivity.this),
                                R.layout.gist_activity, getRecyclerView(), false);
                        RecyclerViewHelper.addHeaderView(getRecyclerView(), mBinding.getRoot());
                        mBinding.setGist(gist);

                        int files = gist.files.size();
                        if (files > 0) {
                            mAdapter.addAll(gist.files.entrySet());
                        }
                    }
                });
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
        Map.Entry<String, Gist.File> file = mAdapter.get(position);
        Intent intent = new Intent(this, CodeReviewActivity.class);
        intent.putExtra(IntentExtra.KEY_NAME, file.getKey());
        intent.putExtra(IntentExtra.KEY_PATH, file.getValue().url);
        startActivity(intent);
    }

    public void onComment(View v) {
        Intent intent = new Intent(this, GistCommentActivity.class);
        intent.putExtra(IntentExtra.KEY_ID, id);
        startActivity(intent);
    }
}
