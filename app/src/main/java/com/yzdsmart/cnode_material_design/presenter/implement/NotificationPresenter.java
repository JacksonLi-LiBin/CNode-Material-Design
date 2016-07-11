package com.yzdsmart.cnode_material_design.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.yzdsmart.cnode_material_design.model.api.ApiClient;
import com.yzdsmart.cnode_material_design.model.api.ApiDefine;
import com.yzdsmart.cnode_material_design.model.api.DefaultCallback;
import com.yzdsmart.cnode_material_design.model.entity.Notification;
import com.yzdsmart.cnode_material_design.model.entity.Result;
import com.yzdsmart.cnode_material_design.model.storage.LoginShared;
import com.yzdsmart.cnode_material_design.presenter.contract.INotificationPresenter;
import com.yzdsmart.cnode_material_design.ui.view.INotificationView;

import retrofit2.Response;

public class NotificationPresenter implements INotificationPresenter {

    private final Activity activity;
    private final INotificationView notificationView;

    public NotificationPresenter(@NonNull Activity activity, @NonNull INotificationView notificationView) {
        this.activity = activity;
        this.notificationView = notificationView;
    }

    @Override
    public void getMessagesAsyncTask() {
        ApiClient.service.getMessages(LoginShared.getAccessToken(activity), ApiDefine.MD_RENDER).enqueue(new DefaultCallback<Result.Data<Notification>>(activity) {

            @Override
            public boolean onResultOk(Response<Result.Data<Notification>> response, Result.Data<Notification> result) {
                notificationView.onGetMessagesOk(result.getData());
                return false;
            }

            @Override
            public void onFinish() {
                notificationView.onGetMessagesFinish();
            }

        });
    }

    @Override
    public void markAllMessageReadAsyncTask() {
        ApiClient.service.markAllMessageRead(LoginShared.getAccessToken(activity)).enqueue(new DefaultCallback<Result>(activity) {

            @Override
            public boolean onResultOk(Response<Result> response, Result result) {
                notificationView.onMarkAllMessageReadOk();
                return false;
            }

        });
    }

}
