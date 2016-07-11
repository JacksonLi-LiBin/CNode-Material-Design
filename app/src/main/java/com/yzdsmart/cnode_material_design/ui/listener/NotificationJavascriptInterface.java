package com.yzdsmart.cnode_material_design.ui.listener;

import android.content.Context;
import android.support.annotation.NonNull;
import android.webkit.JavascriptInterface;

import com.yzdsmart.cnode_material_design.ui.activity.UserDetailActivity;
import com.yzdsmart.cnode_material_design.ui.util.Navigator;

public final class NotificationJavascriptInterface {

    private volatile static NotificationJavascriptInterface singleton;

    public static NotificationJavascriptInterface with(@NonNull Context context) {
        if (singleton == null) {
            synchronized (NotificationJavascriptInterface.class) {
                if (singleton == null) {
                    singleton = new NotificationJavascriptInterface(context);
                }
            }
        }
        return singleton;
    }

    public static final String NAME = "notificationBridge";

    private final Context context;

    private NotificationJavascriptInterface(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    @JavascriptInterface
    public void openTopic(String topicId) {
        Navigator.TopicWithAutoCompat.start(context, topicId);
    }

    @JavascriptInterface
    public void openUser(String loginName) {
        UserDetailActivity.start(context, loginName);
    }

}
