package com.yzdsmart.cnode_material_design.ui.listener;

import android.content.Context;
import android.support.annotation.NonNull;
import android.webkit.JavascriptInterface;

import com.yzdsmart.cnode_material_design.ui.activity.ImagePreviewActivity;

public final class ImageJavascriptInterface {

    private volatile static ImageJavascriptInterface singleton;

    public static ImageJavascriptInterface with(@NonNull Context context) {
        if (singleton == null) {
            synchronized (ImageJavascriptInterface.class) {
                if (singleton == null) {
                    singleton = new ImageJavascriptInterface(context);
                }
            }
        }
        return singleton;
    }

    public static final String NAME = "imageBridge";

    private final Context context;

    private ImageJavascriptInterface(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    @JavascriptInterface
    public void openImage(String imageUrl) {
        ImagePreviewActivity.start(context, imageUrl);
    }

}
