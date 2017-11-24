package com.cncoderx.github.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cncoderx.github.R;
import com.cncoderx.github.sdk.ServiceGenerator;
import com.cncoderx.github.sdk.model.Comment;
import com.cncoderx.github.sdk.model.PullRequest;
import com.cncoderx.github.sdk.service.IPullsService;
import com.cncoderx.github.ui.adapter.CommentAdapter;
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
public class PullCommentActivity extends BaseActivity {
    @BindView(R.id.rv_pull_comment_list)
    RecyclerView rvComment;

    private ListCallback<Comment> mListCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_comment);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String owner = intent.getStringExtra(IntentExtra.KEY_OWNER);
        String repo = intent.getStringExtra(IntentExtra.KEY_REPO);
        int number = intent.getIntExtra("number", 0);
        PullRequest pull = intent.getParcelableExtra("pull_request");

        Comment comment = new Comment();
        comment.user = pull.user;
        comment.body = pull.body;
        comment.createdAt = pull.createdAt;
        comment.updatedAt = pull.updatedAt;
        CommentAdapter adapter = new CommentAdapter(comment);

        RecyclerViewHelper.setLinearLayout(rvComment);
        RecyclerViewHelper.setAdapter(rvComment, adapter);
        RecyclerViewHelper.setDivider(rvComment,
                ContextCompat.getDrawable(this, R.drawable.list_divider),
                getResources().getDimensionPixelOffset(R.dimen.divider_height));

        View headerView = new HeaderViewBuilder(this).build(pull);
        RecyclerViewHelper.addHeaderView(rvComment, headerView);

        mListCallback = new ListCallback<>(adapter, false);

        IPullsService service = ServiceGenerator.create(IPullsService.class);
        Call<List<Comment>> call = service.getPullsComments(owner, repo, Integer.toString(number));
        mListCallback.queueTo(call);
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
            headerView = LayoutInflater.from(context).inflate(R.layout.item_pulls_header, rvComment, false);
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