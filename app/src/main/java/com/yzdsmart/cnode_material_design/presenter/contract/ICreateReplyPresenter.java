package com.yzdsmart.cnode_material_design.presenter.contract;

import android.support.annotation.NonNull;

public interface ICreateReplyPresenter {

    void createReplyAsyncTask(@NonNull String topicId, String content, String targetId);

}
