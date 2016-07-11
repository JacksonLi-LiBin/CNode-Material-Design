package com.yzdsmart.cnode_material_design.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;


import com.yzdsmart.cnode_material_design.R;
import com.yzdsmart.cnode_material_design.model.api.ApiClient;
import com.yzdsmart.cnode_material_design.model.api.DefaultCallback;
import com.yzdsmart.cnode_material_design.model.entity.Result;
import com.yzdsmart.cnode_material_design.model.entity.TabType;
import com.yzdsmart.cnode_material_design.model.storage.LoginShared;
import com.yzdsmart.cnode_material_design.model.storage.SettingShared;
import com.yzdsmart.cnode_material_design.presenter.contract.ICreateTopicPresenter;
import com.yzdsmart.cnode_material_design.ui.view.ICreateTopicView;

import retrofit2.Response;

public class CreateTopicPresenter implements ICreateTopicPresenter {

    private final Activity activity;
    private final ICreateTopicView createTopicView;

    public CreateTopicPresenter(@NonNull Activity activity, @NonNull ICreateTopicView createTopicView) {
        this.activity = activity;
        this.createTopicView = createTopicView;
    }

    @Override
    public void createTopicAsyncTask(@NonNull TabType tab, String title, String content) {
        if (TextUtils.isEmpty(title) || title.length() < 10) {
            createTopicView.onTitleError(activity.getString(R.string.title_empty_error_tip));
        } else if (TextUtils.isEmpty(content)) {
            createTopicView.onContentError(activity.getString(R.string.content_empty_error_tip));
        } else {
            if (SettingShared.isEnableTopicSign(activity)) { // 添加小尾巴
                content += "\n\n" + SettingShared.getTopicSignContent(activity);
            }
            createTopicView.onCreateTopicStart();
            ApiClient.service.createTopic(LoginShared.getAccessToken(activity), tab, title, content).enqueue(new DefaultCallback<Result.CreateTopic>(activity) {

                @Override
                public boolean onResultOk(Response<Result.CreateTopic> response, Result.CreateTopic result) {
                    createTopicView.onCreateTopicOk(result.getTopicId());
                    return false;
                }

                @Override
                public void onFinish() {
                    createTopicView.onCreateTopicFinish();
                }

            });
        }
    }

}
