package com.yzdsmart.cnode_material_design.ui.listener;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.webkit.JavascriptInterface;

import com.yzdsmart.cnode_material_design.R;
import com.yzdsmart.cnode_material_design.model.entity.Reply;
import com.yzdsmart.cnode_material_design.model.storage.LoginShared;
import com.yzdsmart.cnode_material_design.model.util.EntityUtils;
import com.yzdsmart.cnode_material_design.presenter.contract.IReplyPresenter;
import com.yzdsmart.cnode_material_design.presenter.contract.ITopicHeaderPresenter;
import com.yzdsmart.cnode_material_design.ui.activity.LoginActivity;
import com.yzdsmart.cnode_material_design.ui.activity.UserDetailActivity;
import com.yzdsmart.cnode_material_design.ui.util.ToastUtils;
import com.yzdsmart.cnode_material_design.ui.view.ICreateReplyView;
import com.yzdsmart.cnode_material_design.util.HandlerUtils;

public final class TopicJavascriptInterface {

    public static final String NAME = "topicBridge";

    private final Activity activity;
    private final ICreateReplyView createReplyView;
    private final ITopicHeaderPresenter topicHeaderPresenter;
    private final IReplyPresenter replyPresenter;

    public TopicJavascriptInterface(@NonNull Activity activity, @NonNull ICreateReplyView createReplyView, @NonNull ITopicHeaderPresenter topicHeaderPresenter, @NonNull IReplyPresenter replyPresenter) {
        this.activity = activity;
        this.createReplyView = createReplyView;
        this.topicHeaderPresenter = topicHeaderPresenter;
        this.replyPresenter = replyPresenter;
    }

    @JavascriptInterface
    public void collectTopic(String topicId) {
        if (LoginActivity.startForResultWithAccessTokenCheck(activity)) {
            topicHeaderPresenter.collectTopicAsyncTask(topicId);
        }
    }

    @JavascriptInterface
    public void decollectTopic(String topicId) {
        if (LoginActivity.startForResultWithAccessTokenCheck(activity)) {
            topicHeaderPresenter.decollectTopicAsyncTask(topicId);
        }
    }

    @JavascriptInterface
    public void upReply(String replyJson) {
        if (LoginActivity.startForResultWithAccessTokenCheck(activity)) {
            Reply reply = EntityUtils.gson.fromJson(replyJson, Reply.class);
            if (reply.getAuthor().getLoginName().equals(LoginShared.getLoginName(activity))) {
                ToastUtils.with(activity).show(R.string.can_not_up_yourself_reply);
            } else {
                replyPresenter.upReplyAsyncTask(reply);
            }
        }
    }

    @JavascriptInterface
    public void at(final String targetJson, final int targetPosition) {
        if (LoginActivity.startForResultWithAccessTokenCheck(activity)) {
            HandlerUtils.post(new Runnable() {

                @Override
                public void run() {
                    Reply target = EntityUtils.gson.fromJson(targetJson, Reply.class);
                    createReplyView.onAt(target, targetPosition);
                }

            });
        }
    }

    @JavascriptInterface
    public void openUser(String loginName) {
        UserDetailActivity.start(activity, loginName);
    }

}
