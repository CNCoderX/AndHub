package com.cncoderx.github.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.view.View;

import com.cncoderx.github.R;
import com.cncoderx.github.sdk.model.Event;
import com.cncoderx.github.sdk.model.EventType;
import com.cncoderx.github.ui.activity.GistActivity;
import com.cncoderx.github.ui.activity.IssueCommentActivity;
import com.cncoderx.github.ui.activity.ProfileActivity;
import com.cncoderx.github.ui.activity.PullCommentActivity;
import com.cncoderx.github.ui.activity.RepositoryActivity;

import java.util.Map;

/**
 * @author cncoderx
 */
public class EventMessageFactoryImpl implements EventType.EventMessageFactory {

    public CharSequence getCommitCommentMessage(Context context, @NonNull Event event) {
        return new SpannableStringBuilder()
                .append(getLinkText(context, event.actor.login, new UserPendingIntent(context)))
                .append(" ").append(context.getString(R.string.event_msg_commented_on)).append(" ")
                .append(getLinkText(context, event.repo.name, new RepoPendingIntent(context)));
    }

    public CharSequence getCreateMessage(Context context, @NonNull Event event) {
        String ref = (String) event.payload.get("ref");
        String refType = (String) event.payload.get("ref_type");
        SpannableStringBuilder builder = new SpannableStringBuilder()
                .append(getLinkText(context, event.actor.login, new UserPendingIntent(context)))
                .append(" ").append(context.getString(R.string.event_msg_created)).append(" ")
                .append(getItalicText(refType)).append(" ");
        if ("repository".equals(refType)) {
            builder.append(getLinkText(context, event.repo.name, new RepoPendingIntent(context)));
        } else {
            builder.append(getItalicText(ref)).append(" ")
                    .append(getLinkText(context, event.repo.name, new RepoPendingIntent(context)));
        }
        return builder;
    }

    public CharSequence getDeleteMessage(Context context, @NonNull Event event) {
        String ref = (String) event.payload.get("ref");
        String refType = (String) event.payload.get("ref_type");
        return new SpannableStringBuilder()
                .append(getLinkText(context, event.actor.login, new UserPendingIntent(context)))
                .append(" ").append(context.getString(R.string.event_msg_deleted)).append(" ")
                .append(getItalicText(refType)).append(" ")
                .append(getItalicText(ref)).append(" ")
                .append(getLinkText(context, event.repo.name, new RepoPendingIntent(context)));
    }

    public CharSequence getDownloadMessage(Context context, @NonNull Event event) {
        return new SpannableStringBuilder()
                .append(getLinkText(context, event.actor.login, new UserPendingIntent(context)))
                .append(" ").append(context.getString(R.string.event_msg_uploaded_file)).append(" ")
                .append(getLinkText(context, event.repo.name, new RepoPendingIntent(context)));
    }

    public CharSequence getReleaseMessage(Context context, @NonNull Event event) {
        return new SpannableStringBuilder()
                .append(getLinkText(context, event.actor.login, new UserPendingIntent(context)))
                .append(" ").append(context.getString(R.string.event_msg_uploaded_file)).append(" ")
                .append(getLinkText(context, event.repo.name, new RepoPendingIntent(context)));
    }

    public CharSequence getFollowMessage(Context context, @NonNull Event event) {
        String target = (String) ((Map) event.payload.get("target")).get("login");
        return new SpannableStringBuilder()
                .append(getLinkText(context, event.actor.login, new UserPendingIntent(context)))
                .append(" ").append(context.getString(R.string.event_msg_started_following)).append(" ")
                .append(getLinkText(context, target, new UserPendingIntent(context)));
    }

    public CharSequence getForkMessage(Context context, @NonNull Event event) {
        return new SpannableStringBuilder()
                .append(getLinkText(context, event.actor.login, new UserPendingIntent(context)))
                .append(" ").append(context.getString(R.string.event_msg_forked_repo)).append(" ")
                .append(getLinkText(context, event.repo.name, new RepoPendingIntent(context)));
    }

    public CharSequence getGistMessage(Context context, @NonNull Event event) {
        String action = (String) event.payload.get("action");
        String id = (String) ((Map) event.payload.get("gist")).get("id");
        return new SpannableStringBuilder()
                .append(getLinkText(context, event.actor.login, new UserPendingIntent(context)))
                .append(" ").append(action).append(" ").append(context.getString(R.string.gist)).append(" ")
                .append(getLinkText(context, id, new GistPendingIntent(context)));
    }

    public CharSequence getGollumMessage(Context context, @NonNull Event event) {
        return new SpannableStringBuilder()
                .append(getLinkText(context, event.actor.login, new UserPendingIntent(context)))
                .append(" ").append(context.getString(R.string.event_msg_updated_wiki)).append(" ")
                .append(getLinkText(context, event.repo.name, new RepoPendingIntent(context)));
    }

    public CharSequence getIssueCommentMessage(Context context, @NonNull Event event) {
        Map issue = (Map) event.payload.get("issue");
        Map repo = (Map) issue.get("repository");
        String number = issue.get("number").toString();
        String repoFullname = repo.get("full_name").toString();
        String[] args = repoFullname.split("/");
        return new SpannableStringBuilder()
                .append(getLinkText(context, event.actor.login, new UserPendingIntent(context)))
                .append(" ").append(context.getString(R.string.event_msg_commented_issue)).append(" ")
                .append(getLinkText(context, number, new IssuePendingIntent(context, args[0], args[1])))
                .append(" ").append(context.getString(R.string.event_msg_on)).append(" ")
                .append(getLinkText(context, event.repo.name, new RepoPendingIntent(context)));
    }

    public CharSequence getIssuesMessage(Context context, @NonNull Event event) {
        String action = (String) event.payload.get("action");
        Map issue = (Map) event.payload.get("issue");
        Map repo = (Map) issue.get("repository");
        String number = issue.get("number").toString();
        String repoFullname = repo.get("full_name").toString();
        String[] args = repoFullname.split("/");
        return new SpannableStringBuilder()
                .append(getLinkText(context, event.actor.login, new UserPendingIntent(context)))
                .append(" ").append(action).append(" ").append(context.getString(R.string.issue)).append(" ")
                .append(getLinkText(context, number, new IssuePendingIntent(context, args[0], args[1])))
                .append(" ").append(context.getString(R.string.event_msg_on)).append(" ")
                .append(getLinkText(context, event.repo.name, new RepoPendingIntent(context)));
    }

    public CharSequence getMemberMessage(Context context, @NonNull Event event) {
        String member = (String) ((Map) event.payload.get("member")).get("login");
        return new SpannableStringBuilder()
                .append(getLinkText(context, event.actor.login, new UserPendingIntent(context)))
                .append(" ").append(context.getString(R.string.event_msg_added)).append(" ")
                .append(getLinkText(context, member, new UserPendingIntent(context)))
                .append(" ").append(context.getString(R.string.event_msg_as_collaborator)).append(" ")
                .append(getLinkText(context, event.repo.name, new RepoPendingIntent(context)));
    }

    public CharSequence getPublicMessage(Context context, @NonNull Event event) {
        return new SpannableStringBuilder()
                .append(getLinkText(context, event.actor.login, new UserPendingIntent(context)))
                .append(" ").append(context.getString(R.string.event_msg_open_sourced)).append(" ")
                .append(getLinkText(context, event.repo.name, new RepoPendingIntent(context)));
    }

    public CharSequence getPullRequestMessage(Context context, @NonNull Event event) {
        String action = (String) event.payload.get("action");
        Map request = (Map) event.payload.get("pull_request");
        Map repo = (Map) request.get("repository");
        String number = request.get("number").toString();
        String repoFullname = repo.get("full_name").toString();
        String[] args = repoFullname.split("/");
        return new SpannableStringBuilder()
                .append(getLinkText(context, event.actor.login, new UserPendingIntent(context)))
                .append(" ").append(action).append(" ").append(context.getString(R.string.pull_request)).append(" ")
                .append(getLinkText(context, number, new IssuePendingIntent(context, args[0], args[1])))
                .append(" ").append(context.getString(R.string.event_msg_on)).append(" ")
                .append(getLinkText(context, event.repo.name, new RepoPendingIntent(context)));
    }

    public CharSequence getReviewCommentMessage(Context context, @NonNull Event event) {
        return new SpannableStringBuilder()
                .append(getLinkText(context, event.actor.login, new UserPendingIntent(context)))
                .append(" ").append(context.getString(R.string.event_msg_commented_on)).append(" ")
                .append(getLinkText(context, event.repo.name, new RepoPendingIntent(context)));
    }

    public CharSequence getPushMessage(Context context, @NonNull Event event) {
        String ref = (String) event.payload.get("ref");
        if (ref.startsWith("refs/heads/")) {
            ref = ref.substring(11);
        }
        return new SpannableStringBuilder()
                .append(getLinkText(context, event.actor.login, new UserPendingIntent(context)))
                .append(" ").append(context.getString(R.string.event_msg_pushed_to)).append(" ")
                .append(getItalicText(ref)).append(" ").append(context.getString(R.string.event_msg_at)).append(" ")
                .append(getLinkText(context, event.repo.name, new RepoPendingIntent(context)));
    }

    public CharSequence getTeamAddMessage(Context context, @NonNull Event event) {
        String team = (String) ((Map) event.payload.get("team")).get("name");
        return new SpannableStringBuilder()
                .append(getLinkText(context, event.actor.login, new UserPendingIntent(context)))
                .append(" ").append(context.getString(R.string.event_msg_added)).append(" ")
                .append(getLinkText(context, event.repo.name, new RepoPendingIntent(context)))
                .append(" ").append(context.getString(R.string.event_msg_to_team)).append(" ")
                .append(getLinkText(context, team, new OrganPendingIntent(context)));
    }

    public CharSequence getWatchMessage(Context context, @NonNull Event event) {
        return new SpannableStringBuilder()
                .append(getLinkText(context, event.actor.login, new UserPendingIntent(context)))
                .append(" ").append(context.getString(R.string.event_msg_starred)).append(" ")
                .append(getLinkText(context, event.repo.name, new RepoPendingIntent(context)));
    }

    private CharSequence getLinkText(Context context, CharSequence source, PendingIntent intent) {
        return getLinkText(context, source, 0, source.length(), intent);
    }

    private CharSequence getLinkText(Context context, CharSequence source, int offset, int length, PendingIntent intent) {
        SpannableString spanStr = new SpannableString(source);
        spanStr.setSpan(new LinkSpan(context, intent, source),
                offset, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    private CharSequence getItalicText(CharSequence source) {
        return getItalicText(source, 0, source.length());
    }

    private CharSequence getItalicText(CharSequence source, int offset, int length) {
        SpannableString spanStr = new SpannableString(source);
        spanStr.setSpan(new StyleSpan(Typeface.ITALIC),
                offset, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    static class LinkSpan extends ClickableSpan {
        Context mContext;
        PendingIntent mPendingIntent;
        Object mData;

        public LinkSpan(Context context, PendingIntent pendingIntent, Object data) {
            mContext = context;
            mPendingIntent = pendingIntent;
            mData = data;
        }

        @Override
        public void onClick(View widget) {
            if (mPendingIntent != null) {
                Intent intent = mPendingIntent.getIntent(mData);
                if (intent != null) {
                    mContext.startActivity(intent);
                }
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(mContext.getResources().getColor(R.color.theme_blue));
            ds.setUnderlineText(false);
        }
    }

    static abstract class PendingIntent {
        Context mContext;

        public PendingIntent(Context context) {
            mContext = context;
        }

        public abstract Intent getIntent(Object data);
    }

    static class UserPendingIntent extends PendingIntent {

        public UserPendingIntent(Context context) {
            super(context);
        }

        @Override
        public Intent getIntent(Object data) {
            Intent intent = new Intent(mContext, ProfileActivity.class);
            intent.putExtra(IntentExtra.KEY_USER, (String) data);
            return intent;
        }
    }

    static class RepoPendingIntent extends PendingIntent {

        public RepoPendingIntent(Context context) {
            super(context);
        }

        @Override
        public Intent getIntent(Object data) {
            String[] args = ((String) data).split("/");
            Intent intent = new Intent(mContext, RepositoryActivity.class);
            intent.putExtra(IntentExtra.KEY_OWNER, args[0]);
            intent.putExtra(IntentExtra.KEY_REPO, args[1]);
            return intent;
        }
    }

    static class OrganPendingIntent extends PendingIntent {

        public OrganPendingIntent(Context context) {
            super(context);
        }

        @Override
        public Intent getIntent(Object data) {
            Intent intent = new Intent(mContext, ProfileActivity.class);
            intent.putExtra(IntentExtra.KEY_USER, (String) data);
            return intent;
        }
    }

    static class GistPendingIntent extends PendingIntent {

        public GistPendingIntent(Context context) {
            super(context);
        }

        @Override
        public Intent getIntent(Object data) {
            Intent intent = new Intent(mContext, GistActivity.class);
            intent.putExtra(IntentExtra.KEY_ID, (String) data);
            return intent;
        }
    }

    static class IssuePendingIntent extends PendingIntent {
        private String mOwner;
        private String mRepo;

        public IssuePendingIntent(Context context, String owner, String repo) {
            super(context);
            mOwner = owner;
            mRepo = repo;
        }

        @Override
        public Intent getIntent(Object data) {
            Intent intent = new Intent(mContext, IssueCommentActivity.class);
            intent.putExtra(IntentExtra.KEY_OWNER, mOwner);
            intent.putExtra(IntentExtra.KEY_REPO, mRepo);
            intent.putExtra(IntentExtra.KEY_NUMBER, (String) data);
            return intent;
        }
    }

    static class PullPendingIntent extends PendingIntent {
        private String mOwner;
        private String mRepo;

        public PullPendingIntent(Context context, String owner, String repo) {
            super(context);
            mOwner = owner;
            mRepo = repo;
        }

        @Override
        public Intent getIntent(Object data) {
            Intent intent = new Intent(mContext, PullCommentActivity.class);
            intent.putExtra(IntentExtra.KEY_OWNER, mOwner);
            intent.putExtra(IntentExtra.KEY_REPO, mRepo);
            intent.putExtra(IntentExtra.KEY_NUMBER, (String) data);
            return intent;
        }
    }
}
