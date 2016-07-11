package com.yzdsmart.cnode_material_design.ui.view;

import android.support.annotation.NonNull;

import com.yzdsmart.cnode_material_design.model.entity.TabType;
import com.yzdsmart.cnode_material_design.model.entity.Topic;
import com.yzdsmart.cnode_material_design.ui.viewholder.LoadMoreFooter;

import java.util.List;

public interface IMainView {

    void onSwitchTabOk(@NonNull TabType tab);

    void onRefreshTopicListOk(@NonNull List<Topic> topicList);

    void onRefreshTopicListFinish();

    void onLoadMoreTopicListOk(@NonNull List<Topic> topicList);

    void onLoadMoreTopicListFinish(@NonNull LoadMoreFooter.State state);

    void updateUserInfoViews();

    void updateMessageCountViews(int count);

}
