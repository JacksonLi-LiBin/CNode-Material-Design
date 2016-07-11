package com.yzdsmart.cnode_material_design.presenter.contract;

import android.support.annotation.NonNull;

import com.yzdsmart.cnode_material_design.model.entity.TabType;


public interface ICreateTopicPresenter {

    void createTopicAsyncTask(@NonNull TabType tab, String title, String content);

}
