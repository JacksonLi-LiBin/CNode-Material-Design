package com.yzdsmart.cnode_material_design.ui.view;

import android.support.annotation.NonNull;

import com.yzdsmart.cnode_material_design.model.entity.Reply;
import com.yzdsmart.cnode_material_design.model.entity.TopicWithReply;

public interface ITopicView {

    void onGetTopicOk(@NonNull TopicWithReply topic);

    void onGetTopicFinish();

    void appendReplyAndUpdateViews(@NonNull Reply reply);

}
