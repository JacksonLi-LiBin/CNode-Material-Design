package com.yzdsmart.cnode_material_design.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.yzdsmart.cnode_material_design.model.api.ApiClient;
import com.yzdsmart.cnode_material_design.model.api.DefaultCallback;
import com.yzdsmart.cnode_material_design.model.entity.Reply;
import com.yzdsmart.cnode_material_design.model.entity.Result;
import com.yzdsmart.cnode_material_design.model.storage.LoginShared;
import com.yzdsmart.cnode_material_design.presenter.contract.IReplyPresenter;
import com.yzdsmart.cnode_material_design.ui.view.IReplyView;

import retrofit2.Response;

public class ReplyPresenter implements IReplyPresenter {

    private final Activity activity;
    private final IReplyView replyView;

    public ReplyPresenter(@NonNull Activity activity, @NonNull IReplyView replyView) {
        this.activity = activity;
        this.replyView = replyView;
    }

    @Override
    public void upReplyAsyncTask(@NonNull final Reply reply) {
        ApiClient.service.upReply(reply.getId(), LoginShared.getAccessToken(activity)).enqueue(new DefaultCallback<Result.UpReply>(activity) {

            @Override
            public boolean onResultOk(Response<Result.UpReply> response, Result.UpReply result) {
                if (result.getAction() == Reply.UpAction.up) {
                    reply.getUpList().add(LoginShared.getId(getActivity()));
                } else if (result.getAction() == Reply.UpAction.down) {
                    reply.getUpList().remove(LoginShared.getId(getActivity()));
                }
                replyView.onUpReplyOk(reply);
                return false;
            }

        });
    }

}
