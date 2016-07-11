package com.yzdsmart.cnode_material_design.presenter.contract;

import android.support.annotation.NonNull;

import com.yzdsmart.cnode_material_design.model.entity.TabType;

public interface IMainPresenter {

    void switchTab(@NonNull TabType tab);

    void refreshTopicListAsyncTask();

    void loadMoreTopicListAsyncTask(int page);

    void getUserAsyncTask();

    void getMessageCountAsyncTask();

}
