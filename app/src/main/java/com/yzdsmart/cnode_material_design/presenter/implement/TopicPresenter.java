package com.yzdsmart.cnode_material_design.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.yzdsmart.cnode_material_design.model.api.ApiClient;
import com.yzdsmart.cnode_material_design.model.api.ApiDefine;
import com.yzdsmart.cnode_material_design.model.api.DefaultCallback;
import com.yzdsmart.cnode_material_design.model.entity.Result;
import com.yzdsmart.cnode_material_design.model.entity.TopicWithReply;
import com.yzdsmart.cnode_material_design.model.storage.LoginShared;
import com.yzdsmart.cnode_material_design.presenter.contract.ITopicPresenter;
import com.yzdsmart.cnode_material_design.ui.view.ITopicView;

import retrofit2.Response;

public class TopicPresenter implements ITopicPresenter {

    private final Activity activity;
    private final ITopicView topicView;

    public TopicPresenter(@NonNull Activity activity, @NonNull ITopicView topicView) {
        this.activity = activity;
        this.topicView = topicView;
    }

    @Override
    public void getTopicAsyncTask(@NonNull String topicId) {
        ApiClient.service.getTopic(topicId, LoginShared.getAccessToken(activity), ApiDefine.MD_RENDER).enqueue(new DefaultCallback<Result.Data<TopicWithReply>>(activity) {

            @Override
            public boolean onResultOk(Response<Result.Data<TopicWithReply>> response, Result.Data<TopicWithReply> result) {
                topicView.onGetTopicOk(result.getData());
                return false;
            }

            @Override
            public void onFinish() {
                topicView.onGetTopicFinish();
            }

        });
    }

}
