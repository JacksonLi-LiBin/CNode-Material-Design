package com.yzdsmart.cnode_material_design.ui.view;

import android.support.annotation.NonNull;

import com.yzdsmart.cnode_material_design.model.entity.Notification;

public interface INotificationView {

    void onGetMessagesOk(@NonNull Notification notification);

    void onGetMessagesFinish();

    void onMarkAllMessageReadOk();

}
