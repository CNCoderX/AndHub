package com.cncoderx.github.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cncoderx.github.R;
import com.cncoderx.github.sdk.ServiceGenerator;
import com.cncoderx.github.sdk.model.Gist;
import com.cncoderx.github.sdk.service.IGistService;
import com.cncoderx.github.switcher.ButtonSwitcher;
import com.cncoderx.github.switcher.GistStarSwitcher;
import com.cncoderx.github.ui.adapter.GistFileListAdapter;
import com.cncoderx.github.ui.view.IconView;
import com.cncoderx.github.utils.CallbackFinal;
import com.cncoderx.github.utils.IntentExtra;
import com.cncoderx.github.utils.TimeFormatter;
import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;
import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;

import java.util.Map;

import retrofit2.Call;

/**
 * @author cncoderx
 */
public class GistActivity extends RecyclerViewActivity implements View.OnClickListener, OnItemClickListener {
    private GistFileListAdapter mAdapter = new GistFileListAdapter();
    View mHeader;
    TextView tvCreated;
    TextView tvDesc;
    TextView tvStar;
    IconView ivStar;

    private String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getIntent().getStringExtra(IntentExtra.KEY_ID);
        setAdapter(mAdapter);
        setOnItemClickListener(this);
        createHeaderView();
        createStarSwitcher();
        newServiceCall();
    }

    private void createHeaderView() {
        mHeader = LayoutInflater.from(this).inflate(R.layout.item_gist_file_header, getRecyclerView(), false);
        tvCreated = (TextView) mHeader.findViewById(R.id.tv_gist_created);
        tvDesc = (TextView) mHeader.findViewById(R.id.tv_gist_desc);
        tvStar = (TextView) mHeader.findViewById(R.id.tv_gist_star);
        mHeader.findViewById(R.id.tv_gist_comments).setOnClickListener(this);
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
        Call<Gist> call = service.getGist(id);
        call.enqueue(new CallbackFinal<Gist>() {
            @Override
            public void onSuccess(Gist gist) {
                tvDesc.setText(gist.description);
                tvCreated.setText(TimeFormatter.format(getApplicationContext(), gist.createdAt));

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
        Intent intent = new Intent(this, TextFileReviewActivity.class);
        intent.putExtra(IntentExtra.KEY_NAME, file.getKey());
        intent.putExtra(IntentExtra.KEY_PATH, file.getValue().url);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_gist_comments:
                Intent intent = new Intent(this, GistCommentActivity.class);
                intent.putExtra(IntentExtra.KEY_ID, id);
                intent.putExtra(IntentExtra.KEY_DESC, tvDesc.getText());
                intent.putExtra(IntentExtra.KEY_DATE, tvCreated.getText());
                startActivity(intent);
                break;
            case R.id.tv_gist_share:
                break;
        }
    }
}
