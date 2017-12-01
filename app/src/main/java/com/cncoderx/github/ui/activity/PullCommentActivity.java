package com.cncoderx.github.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cncoderx.github.R;
import com.cncoderx.github.sdk.ServiceGenerator;
import com.cncoderx.github.sdk.model.Comment;
import com.cncoderx.github.sdk.model.PullRequest;
import com.cncoderx.github.sdk.service.IPullsService;
import com.cncoderx.github.ui.adapter.CommentAdapter;
import com.cncoderx.github.utils.CallbackFinal;
import com.cncoderx.github.utils.IntentExtra;
import com.cncoderx.github.utils.ListCallback;
import com.cncoderx.github.utils.TimeFormatter;
import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * @author cncoderx
 */
public class PullCommentActivity extends RecyclerViewActivity {
    private CommentAdapter mAdapter = new CommentAdapter();
    private ListCallback<Comment> mCallback = new ListCallback<>(mAdapter);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String owner = intent.getStringExtra(IntentExtra.KEY_OWNER);
        String repo = intent.getStringExtra(IntentExtra.KEY_REPO);
        int number = intent.getIntExtra(IntentExtra.KEY_NUMBER, 0);

        setAdapter(mAdapter);

        callHeaderData(owner, repo, number);
        callPullComments(owner, repo, number);
    }

    private void callHeaderData(String owner, String repo, int number) {
        IPullsService service = ServiceGenerator.create(IPullsService.class);
        Call<PullRequest> call = service.getPullRequest(owner, repo, Integer.toString(number));
        call.enqueue(new CallbackFinal<PullRequest>() {
            @Override
            public void onSuccess(PullRequest pull) {
                Comment comment = new Comment();
                comment.user = pull.user;
                comment.body = pull.body;
                comment.createdAt = pull.createdAt;
                comment.updatedAt = pull.updatedAt;
                mAdapter.add(0, comment);

                View headerView = new PullCommentActivity.HeaderViewBuilder(PullCommentActivity.this).build(pull);
                RecyclerViewHelper.addHeaderView(getRecyclerView(), headerView);
            }
        });
    }

    private void callPullComments(String owner, String repo, int number) {
        IPullsService service = ServiceGenerator.create(IPullsService.class);
        Call<List<Comment>> call = service.getComments(owner, repo, Integer.toString(number));
        mCallback.queueTo(call);
    }

    class HeaderViewBuilder {
        @BindView(R.id.tv_repo_detail_pulls_title)
        TextView tvTitle;

        @BindView(R.id.tv_repo_detail_pulls_number)
        TextView tvNumber;

        @BindView(R.id.tv_repo_detail_pulls_opened)
        TextView tvOpened;

        @BindView(R.id.ll_repo_detail_pulls_comment)
        View llComment;

        @BindView(R.id.tv_repo_detail_pulls_comment)
        TextView tvComment;

        View headerView;

        public HeaderViewBuilder(Context context) {
            headerView = LayoutInflater.from(context).inflate(
                    R.layout.item_pulls_header, getRecyclerView(), false);
            ButterKnife.bind(this, headerView);
        }

        public View build(@NonNull PullRequest pull) {
            tvTitle.setText(pull.title);
            tvNumber.setText("#" + pull.number);
            if (pull.createdAt != null) {
                String created = TimeFormatter.format(getApplicationContext(), pull.createdAt);
                String opened = PullCommentActivity.this.getString(R.string.opened_on) + " " + created;
                tvOpened.setText(opened);
            }
            if (pull.comments == 0) {
                llComment.setVisibility(View.INVISIBLE);
            } else {
                llComment.setVisibility(View.VISIBLE);
                tvComment.setText(Integer.toString(pull.comments));
            }
            return headerView;
        }
    }
}