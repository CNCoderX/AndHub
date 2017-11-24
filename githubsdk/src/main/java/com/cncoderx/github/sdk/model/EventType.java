package com.cncoderx.github.sdk.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * @author cncoderx
 */
public enum EventType {
    CommitCommentEvent {
        @Override
        public CharSequence getMessage(Context context, @NonNull Event event, EventMessageFactory factory) {
            return factory.getCommitCommentMessage(context, event);
        }
    },
    CreateEvent {
        @Override
        public CharSequence getMessage(Context context, @NonNull Event event, EventMessageFactory factory) {
            return factory.getCreateMessage(context, event);
        }
    },
    DeleteEvent {
        @Override
        public CharSequence getMessage(Context context, @NonNull Event event, EventMessageFactory factory) {
            return factory.getDeleteMessage(context, event);
        }
    },
    DownloadEvent {
        @Override
        public CharSequence getMessage(Context context, @NonNull Event event, EventMessageFactory factory) {
            return factory.getDownloadMessage(context, event);
        }
    },
    ReleaseEvent {
        @Override
        public CharSequence getMessage(Context context, @NonNull Event event, EventMessageFactory factory) {
            return factory.getReleaseMessage(context, event);
        }
    },
    FollowEvent {
        @Override
        public CharSequence getMessage(Context context, @NonNull Event event, EventMessageFactory factory) {
            return factory.getFollowMessage(context, event);
        }
    },
    ForkEvent {
        @Override
        public CharSequence getMessage(Context context, @NonNull Event event, EventMessageFactory factory) {
            return factory.getForkMessage(context, event);
        }
    },
    GistEvent {
        @Override
        public CharSequence getMessage(Context context, @NonNull Event event, EventMessageFactory factory) {
            return factory.getGistMessage(context, event);
        }
    },
    GollumEvent {
        @Override
        public CharSequence getMessage(Context context, @NonNull Event event, EventMessageFactory factory) {
            return factory.getGollumMessage(context, event);
        }
    },
    IssueCommentEvent {
        @Override
        public CharSequence getMessage(Context context, @NonNull Event event, EventMessageFactory factory) {
            return factory.getIssueCommentMessage(context, event);
        }
    },
    IssuesEvent {
        @Override
        public CharSequence getMessage(Context context, @NonNull Event event, EventMessageFactory factory) {
            return factory.getIssuesMessage(context, event);
        }
    },
    MemberEvent {
        @Override
        public CharSequence getMessage(Context context, @NonNull Event event, EventMessageFactory factory) {
            return factory.getMemberMessage(context, event);
        }
    },
    PublicEvent {
        @Override
        public CharSequence getMessage(Context context, @NonNull Event event, EventMessageFactory factory) {
            return factory.getPublicMessage(context, event);
        }
    },
    PullRequestEvent {
        @Override
        public CharSequence getMessage(Context context, @NonNull Event event, EventMessageFactory factory) {
            return factory.getPullRequestMessage(context, event);
        }
    },
    PullRequestReviewCommentEvent {
        @Override
        public CharSequence getMessage(Context context, @NonNull Event event, EventMessageFactory factory) {
            return factory.getReviewCommentMessage(context, event);
        }
    },
    PushEvent {
        @Override
        public CharSequence getMessage(Context context, @NonNull Event event, EventMessageFactory factory) {
            return factory.getPushMessage(context, event);
        }
    },
    TeamAddEvent {
        @Override
        public CharSequence getMessage(Context context, @NonNull Event event, EventMessageFactory factory) {
            return factory.getTeamAddMessage(context, event);
        }
    },
    WatchEvent {
        @Override
        public CharSequence getMessage(Context context, @NonNull Event event, EventMessageFactory factory) {
            return factory.getWatchMessage(context, event);
        }
    };

    public abstract CharSequence getMessage(Context context, @NonNull Event event, EventMessageFactory factory);

    class LinkSpan extends ClickableSpan {
        int color;
        Intent pendingIntent;

        public LinkSpan(int color, Intent pendingIntent) {
            this.color = color;
            this.pendingIntent = pendingIntent;
        }

        @Override
        public void onClick(View widget) {
            Context context = widget.getContext();
            if (context instanceof Activity) {
                context.startActivity(pendingIntent);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(color);
            ds.setUnderlineText(false);
        }
    }

    public static interface EventMessageFactory {

        CharSequence getCommitCommentMessage(Context context, @NonNull Event event);

        CharSequence getCreateMessage(Context context, @NonNull Event event);

        CharSequence getDeleteMessage(Context context, @NonNull Event event);

        CharSequence getDownloadMessage(Context context, @NonNull Event event);

        CharSequence getReleaseMessage(Context context, @NonNull Event event);

        CharSequence getFollowMessage(Context context, @NonNull Event event);

        CharSequence getForkMessage(Context context, @NonNull Event event);

        CharSequence getGistMessage(Context context, @NonNull Event event);

        CharSequence getGollumMessage(Context context, @NonNull Event event);

        CharSequence getIssueCommentMessage(Context context, @NonNull Event event);

        CharSequence getIssuesMessage(Context context, @NonNull Event event);

        CharSequence getMemberMessage(Context context, @NonNull Event event);

        CharSequence getPublicMessage(Context context, @NonNull Event event);

        CharSequence getPullRequestMessage(Context context, @NonNull Event event);

        CharSequence getReviewCommentMessage(Context context, @NonNull Event event);

        CharSequence getPushMessage(Context context, @NonNull Event event);

        CharSequence getTeamAddMessage(Context context, @NonNull Event event);

        CharSequence getWatchMessage(Context context, @NonNull Event event);
    }
}
