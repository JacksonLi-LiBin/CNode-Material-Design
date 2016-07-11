package com.yzdsmart.cnode_material_design.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.yzdsmart.cnode_material_design.model.api.ApiClient;
import com.yzdsmart.cnode_material_design.model.api.DefaultCallback;
import com.yzdsmart.cnode_material_design.model.entity.Result;
import com.yzdsmart.cnode_material_design.model.storage.LoginShared;
import com.yzdsmart.cnode_material_design.presenter.contract.ITopicHeaderPresenter;
import com.yzdsmart.cnode_material_design.ui.view.ITopicHeaderView;

import retrofit2.Response;

public class TopicHeaderPresenter implements ITopicHeaderPresenter {

    private final Activity activity;
    private final ITopicHeaderView topicHeaderView;

    public TopicHeaderPresenter(@NonNull Activity activity, @NonNull ITopicHeaderView topicHeaderView) {
        this.activity = activity;
        this.topicHeaderView = topicHeaderView;
    }

    @Override
    public void collectTopicAsyncTask(@NonNull String topicId) {
        ApiClient.service.collectTopic(LoginShared.getAccessToken(activity), topicId).enqueue(new DefaultCallback<Result>(activity) {

            @Override
            public boolean onResultOk(Response<Result> response, Result result) {
                topicHeaderView.onCollectTopicOk();
                return false;
            }

        });
    }

    @Override
    public void decollectTopicAsyncTask(@NonNull String topicId) {
        ApiClient.service.decollectTopic(LoginShared.getAccessToken(activity), topicId).enqueue(new DefaultCallback<Result>(activity) {

            @Override
            public boolean onResultOk(Response<Result> response, Result result) {
                topicHeaderView.onDecollectTopicOk();
                return false;
            }

        });
    }

}
