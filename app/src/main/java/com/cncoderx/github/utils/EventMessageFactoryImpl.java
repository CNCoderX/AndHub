package com.cncoderx.github.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.cncoderx.github.sdk.model.Event;
import com.cncoderx.github.sdk.model.EventType;

import java.util.Locale;
import java.util.Map;

/**
 * @author cncoderx
 */
public class EventMessageFactoryImpl implements EventType.EventMessageFactory {
    final int linkColor = 0xff0379e1;

    public CharSequence getCommitCommentMessage(Context context, @NonNull Event event) {
        String actor = event.actor.login;
        String repo = event.repo.name;
        SpannableString spanStr = new SpannableString(actor + " commented on " + repo);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), 0, actor.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), spanStr.length() - repo.length(), spanStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    public CharSequence getCreateMessage(Context context, @NonNull Event event) {
        String actor = event.actor.login;
        String repo = event.repo.name;
        String ref = (String) event.payload.get("ref");
        String refType = (String) event.payload.get("ref_type");
        SpannableString spanStr;
        if ("repository".equals(refType)) {
            spanStr = new SpannableString(String.format(Locale.ENGLISH,
                    "%s created %s %s", actor, refType, repo));
        } else {
            spanStr = new SpannableString(String.format(Locale.ENGLISH,
                    "%s created %s %s at %s", actor, refType, ref, repo));
        }
        spanStr.setSpan(new ForegroundColorSpan(linkColor), 0, actor.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), spanStr.length() - repo.length(), spanStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    public CharSequence getDeleteMessage(Context context, @NonNull Event event) {
        String actor = event.actor.login;
        String repo = event.repo.name;
        String ref = (String) event.payload.get("ref");
        String refType = (String) event.payload.get("ref_type");
        SpannableString spanStr = new SpannableString(String.format(Locale.ENGLISH,
                "%s deleted %s %s at %s", actor, refType, ref, repo));
        spanStr.setSpan(new ForegroundColorSpan(linkColor), 0, actor.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), spanStr.length() - repo.length(), spanStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    public CharSequence getDownloadMessage(Context context, @NonNull Event event) {
        String actor = event.actor.login;
        String repo = event.repo.name;
        SpannableString spanStr = new SpannableString(actor + " uploaded a file to " + repo);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), 0, actor.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), spanStr.length() - repo.length(), spanStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    public CharSequence getReleaseMessage(Context context, @NonNull Event event) {
        String actor = event.actor.login;
        String repo = event.repo.name;
        SpannableString spanStr = new SpannableString(actor + " uploaded a file to " + repo);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), 0, actor.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), spanStr.length() - repo.length(), spanStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    public CharSequence getFollowMessage(Context context, @NonNull Event event) {
        String actor = event.actor.login;
        String target = (String) ((Map) event.payload.get("target")).get("login");
        SpannableString spanStr = new SpannableString(actor + " started following " + target);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), 0, actor.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), spanStr.length() - target.length(), spanStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    public CharSequence getForkMessage(Context context, @NonNull Event event) {
        String actor = event.actor.login;
        String repo = event.repo.name;
        SpannableString spanStr = new SpannableString(actor + " forked repository " + repo);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), 0, actor.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), spanStr.length() - repo.length(), spanStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    public CharSequence getGistMessage(Context context, @NonNull Event event) {
        String actor = event.actor.login;
        String action = (String) event.payload.get("action");
        String id = (String) ((Map) event.payload.get("gist")).get("id");
        SpannableString spanStr = new SpannableString(String.format("%s %s Gist %s", actor, action, id));
        spanStr.setSpan(new ForegroundColorSpan(linkColor), 0, actor.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//            spanStr.setSpan(new ForegroundColorSpan(linkColor), spanStr.length() - id.length(), spanStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    public CharSequence getGollumMessage(Context context, @NonNull Event event) {
        String actor = event.actor.login;
        String repo = event.repo.name;
        SpannableString spanStr = new SpannableString(actor + " updated the wiki in " + repo);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), 0, actor.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), spanStr.length() - repo.length(), spanStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    public CharSequence getIssueCommentMessage(Context context, @NonNull Event event) {
        String actor = event.actor.login;
        String repo = event.repo.name;
        String number = ((Map) event.payload.get("issue")).get("number").toString();
        SpannableString spanStr = new SpannableString(String.format("%s commented on issue #%s on %s", actor, number, repo));
        spanStr.setSpan(new ForegroundColorSpan(linkColor), 0, actor.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), spanStr.length() - repo.length() - number.length() - 5, spanStr.length() - repo.length() - 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), spanStr.length() - repo.length(), spanStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    public CharSequence getIssuesMessage(Context context, @NonNull Event event) {
        String actor = event.actor.login;
        String repo = event.repo.name;
        String action = (String) event.payload.get("action");
        String number = ((Map) event.payload.get("issue")).get("number").toString();
        SpannableString spanStr = new SpannableString(String.format("%s %s issue #%s on %s", actor, action, number, repo));
        spanStr.setSpan(new ForegroundColorSpan(linkColor), 0, actor.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), spanStr.length() - repo.length() - number.length() - 5, spanStr.length() - repo.length() - 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), spanStr.length() - repo.length(), spanStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    public CharSequence getMemberMessage(Context context, @NonNull Event event) {
        String actor = event.actor.login;
        String repo = event.repo.name;
        String member = (String) ((Map) event.payload.get("member")).get("login");
        SpannableString spanStr = new SpannableString(String.format("%s added %s as a collaborator to %s", actor, member, repo));
        spanStr.setSpan(new ForegroundColorSpan(linkColor), 0, actor.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), spanStr.length() - repo.length() - member.length() - 22, spanStr.length() - repo.length() - 22, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), spanStr.length() - repo.length(), spanStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    public CharSequence getPublicMessage(Context context, @NonNull Event event) {
        String actor = event.actor.login;
        String repo = event.repo.name;
        SpannableString spanStr = new SpannableString(actor + " open sourced repository " + repo);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), 0, actor.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), spanStr.length() - repo.length(), spanStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    public CharSequence getPullRequestMessage(Context context, @NonNull Event event) {
        String actor = event.actor.login;
        String repo = event.repo.name;
        String action = (String) event.payload.get("action");
        String number = ((Map) event.payload).get("number").toString();
        SpannableString spanStr = new SpannableString(String.format("%s %s pull request #%s on %s", actor, action, number, repo));
        spanStr.setSpan(new ForegroundColorSpan(linkColor), 0, actor.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), spanStr.length() - repo.length() - number.length() - 5, spanStr.length() - repo.length() - 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), spanStr.length() - repo.length(), spanStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    public CharSequence getReviewCommentMessage(Context context, @NonNull Event event) {
        String actor = event.actor.login;
        String repo = event.repo.name;
        SpannableString spanStr = new SpannableString(actor + " commented on " + repo);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), 0, actor.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), spanStr.length() - repo.length(), spanStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    public CharSequence getPushMessage(Context context, @NonNull Event event) {
        String actor = event.actor.login;
        String repo = event.repo.name;
        String ref = (String) event.payload.get("ref");
        if (ref.startsWith("refs/heads/")) {
            ref = ref.substring(11);
        }
        SpannableString spanStr = new SpannableString(String.format("%s pushed to %s at %s", actor, ref, repo));
        spanStr.setSpan(new ForegroundColorSpan(linkColor), 0, actor.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), spanStr.length() - repo.length() - ref.length() - 4, spanStr.length() - repo.length() - 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), spanStr.length() - repo.length(), spanStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    public CharSequence getTeamAddMessage(Context context, @NonNull Event event) {
        String actor = event.actor.login;
        String repo = (String) ((Map) event.payload.get("repository")).get("name");
        String team = (String) ((Map) event.payload.get("team")).get("name");
        SpannableString spanStr = new SpannableString(String.format("%s added %s to team %s", actor, repo, team));
        spanStr.setSpan(new ForegroundColorSpan(linkColor), 0, actor.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), spanStr.length() - team.length() - repo.length() - 9, spanStr.length() - team.length() - 9, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), spanStr.length() - team.length(), spanStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    public CharSequence getWatchMessage(Context context, @NonNull Event event) {
        String actor = event.actor.login;
        String repo = event.repo.name;
        SpannableString spanStr = new SpannableString(actor + " starred " + repo);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), 0, actor.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new ForegroundColorSpan(linkColor), spanStr.length() - repo.length(), spanStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

}
