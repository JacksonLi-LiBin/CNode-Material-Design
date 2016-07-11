package com.yzdsmart.cnode_material_design.ui.view;

import android.support.annotation.NonNull;

import com.yzdsmart.cnode_material_design.model.entity.Reply;


public interface ICreateReplyView {

    void showWindow();

    void dismissWindow();

    void onAt(@NonNull Reply target, @NonNull Integer targetPosition);

    void onContentError(@NonNull String message);

    void onReplyTopicOk(@NonNull Reply reply);

    void onReplyTopicStart();

    void onReplyTopicFinish();

}
