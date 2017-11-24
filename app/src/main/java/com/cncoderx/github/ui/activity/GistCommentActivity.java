package com.cncoderx.github.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cncoderx.github.R;
import com.cncoderx.github.sdk.ServiceGenerator;
import com.cncoderx.github.sdk.model.Comment;
import com.cncoderx.github.sdk.service.IGistService;
import com.cncoderx.github.switcher.ButtonSwitcher;
import com.cncoderx.github.switcher.GistStarSwitcher;
import com.cncoderx.github.ui.adapter.CommentAdapter;
import com.cncoderx.github.utils.IntentExtra;
import com.cncoderx.github.utils.ListCallback;
import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;

import java.util.List;

import retrofit2.Call;

/**
 * @author cncoderx
 */
public class GistCommentActivity extends RecyclerViewActivity implements View.OnClickListener {
    private CommentAdapter mAdapter = new CommentAdapter();
    private ListCallback<Comment> mCallback = new ListCallback<>(mAdapter);
    private String id;

    View mHeader;
    TextView tvCreated;
    TextView tvDesc;
    TextView tvStar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getIntent().getStringExtra(IntentExtra.KEY_ID);
        setAdapter(mAdapter);
        createHeaderView();
        createStarSwitcher();
        newServiceCall();

        String created = getIntent().getStringExtra(IntentExtra.KEY_DATE);
        String desc = getIntent().getStringExtra(IntentExtra.KEY_DESC);
        tvCreated.setText(created);
        tvDesc.setText(desc);
    }

    private void createHeaderView() {
        mHeader = LayoutInflater.from(this).inflate(R.layout.item_gist_file_header, getRecyclerView(), false);
        tvCreated = (TextView) mHeader.findViewById(R.id.tv_gist_created);
        tvDesc = (TextView) mHeader.findViewById(R.id.tv_gist_desc);
        tvStar = (TextView) mHeader.findViewById(R.id.tv_gist_star);
        mHeader.findViewById(R.id.fl_gist_comments).setVisibility(View.GONE);
        mHeader.findViewById(R.id.tv_gist_share).setOnClickListener(this);
        RecyclerViewHelper.addHeaderView(getRecyclerView(), mHeader);
    }

    private void createStarSwitcher() {
        GistStarSwitcher switcher = new GistStarSwitcher(tvStar, id);
        switcher.setOnSwitchListener(new ButtonSwitcher.OnSwitchListener() {
            @Override
            public void switchOn(View button) {
                tvStar.setSelected(true);
                tvStar.setText(R.string.unstar);
            }

            @Override
            public void switchOff(View button) {
                tvStar.setSelected(false);
                tvStar.setText(R.string.star);
            }
        });
        switcher.begin();
    }

    private void newServiceCall() {
        IGistService service = ServiceGenerator.create(IGistService.class);
        Call<List<Comment>> call = service.getComments(id);
        mCallback.queueTo(call, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_gist_share:
                break;
        }
    }
}
