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
import com.cncoderx.github.sdk.model.Issue;
import com.cncoderx.github.sdk.service.IIssuesService;
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
public class IssueCommentActivity extends BaseActivity {
    @BindView(R.id.rv_issue_comment_list)
    RecyclerView rvComment;

    private ListCallback<Comment> mListCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_comment);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String owner = intent.getStringExtra(IntentExtra.KEY_OWNER);
        String repo = intent.getStringExtra(IntentExtra.KEY_REPO);
        int number = intent.getIntExtra("number", 0);
        Issue issue = intent.getParcelableExtra("issue");

        Comment comment = new Comment();
        comment.user = issue.user;
        comment.body = issue.body;
        comment.createdAt = issue.createdAt;
        comment.updatedAt = issue.updatedAt;
        CommentAdapter adapter = new CommentAdapter(comment);

        RecyclerViewHelper.setLinearLayout(rvComment);
        RecyclerViewHelper.setAdapter(rvComment, adapter);
        RecyclerViewHelper.setDivider(rvComment,
                ContextCompat.getDrawable(this, R.drawable.list_divider),
                getResources().getDimensionPixelOffset(R.dimen.divider_height));

        View headerView = new HeaderViewBuilder(this).build(issue);
        RecyclerViewHelper.addHeaderView(rvComment, headerView);

        mListCallback = new ListCallback<>(adapter, false);

        IIssuesService service = ServiceGenerator.create(IIssuesService.class);
        Call<List<Comment>> call = service.getIssuesComments(owner, repo, Integer.toString(number));
        mListCallback.queueTo(call);
    }

    class HeaderViewBuilder {
        @BindView(R.id.tv_repo_detail_issues_title)
        TextView tvTitle;

        @BindView(R.id.tv_repo_detail_issues_number)
        TextView tvNumber;

        @BindView(R.id.tv_repo_detail_issues_opened)
        TextView tvOpened;

        @BindView(R.id.ll_repo_detail_issues_comment)
        View llComment;

        @BindView(R.id.tv_repo_detail_issues_comment)
        TextView tvComment;

        View headerView;

        public HeaderViewBuilder(Context context) {
            headerView = LayoutInflater.from(context).inflate(R.layout.item_issues_header, rvComment, false);
            ButterKnife.bind(this, headerView);
        }

        public View build(@NonNull Issue issue) {
            tvTitle.setText(issue.title);
            tvNumber.setText("#" + issue.number);
            if (issue.createdAt != null) {
                String created = TimeFormatter.format(getApplicationContext(), issue.createdAt);
                String opened = IssueCommentActivity.this.getString(R.string.opened_on) + " " + created;
                tvOpened.setText(opened);
            }
            if (issue.comments == 0) {
                llComment.setVisibility(View.INVISIBLE);
            } else {
                llComment.setVisibility(View.VISIBLE);
                tvComment.setText(Integer.toString(issue.comments));
            }
            return headerView;
        }
    }
}