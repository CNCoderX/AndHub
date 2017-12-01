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
import com.cncoderx.github.sdk.model.Issue;
import com.cncoderx.github.sdk.service.IIssuesService;
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
public class IssueCommentActivity extends RecyclerViewActivity {
    private CommentAdapter mAdapter = new CommentAdapter();
    private ListCallback<Comment> mCallback = new ListCallback<>(mAdapter, false);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String owner = intent.getStringExtra(IntentExtra.KEY_OWNER);
        String repo = intent.getStringExtra(IntentExtra.KEY_REPO);
        int number = intent.getIntExtra(IntentExtra.KEY_NUMBER, 0);

        setAdapter(mAdapter);

        callHeaderData(owner, repo, number);
        callIssueComments(owner, repo, number);
    }

    private void callHeaderData(String owner, String repo, int number) {
        IIssuesService service = ServiceGenerator.create(IIssuesService.class);
        Call<Issue> call = service.getIssue(owner, repo, Integer.toString(number));
        call.enqueue(new CallbackFinal<Issue>() {
            @Override
            public void onSuccess(Issue issue) {
                Comment comment = new Comment();
                comment.user = issue.user;
                comment.body = issue.body;
                comment.createdAt = issue.createdAt;
                comment.updatedAt = issue.updatedAt;
                mAdapter.add(0, comment);

                View headerView = new HeaderViewBuilder(IssueCommentActivity.this).build(issue);
                RecyclerViewHelper.addHeaderView(getRecyclerView(), headerView);
            }
        });
    }

    private void callIssueComments(String owner, String repo, int number) {
        IIssuesService service = ServiceGenerator.create(IIssuesService.class);
        Call<List<Comment>> call = service.getComments(owner, repo, Integer.toString(number));
        mCallback.queueTo(call);
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
            headerView = LayoutInflater.from(context).inflate(
                    R.layout.item_issues_header, getRecyclerView(), false);
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
