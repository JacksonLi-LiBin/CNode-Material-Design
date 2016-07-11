package com.yzdsmart.cnode_material_design.ui.listener;

import android.webkit.JavascriptInterface;

import com.yzdsmart.cnode_material_design.util.FormatUtils;

import org.joda.time.DateTime;

public final class FormatJavascriptInterface {

    public static final FormatJavascriptInterface instance = new FormatJavascriptInterface();
    public static final String NAME = "formatBridge";

    private FormatJavascriptInterface() {}

    @JavascriptInterface
    public String getRelativeTimeSpanString(String time) {
        return FormatUtils.getRelativeTimeSpanString(new DateTime(time));
    }

}
