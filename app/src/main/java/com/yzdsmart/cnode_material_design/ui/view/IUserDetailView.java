package com.yzdsmart.cnode_material_design.ui.view;

import android.support.annotation.NonNull;

import com.yzdsmart.cnode_material_design.model.entity.Topic;
import com.yzdsmart.cnode_material_design.model.entity.User;

import java.util.List;

public interface IUserDetailView {

    void onGetUserOk(@NonNull User user);

    void onGetCollectTopicListOk(@NonNull List<Topic> topicList);

    void onGetUserError(@NonNull String message);

    void onGetUserStart();

    void onGetUserFinish();

}
