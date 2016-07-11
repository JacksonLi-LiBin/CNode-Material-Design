package com.yzdsmart.cnode_material_design.ui.dialog;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.yzdsmart.cnode_material_design.R;
import com.yzdsmart.cnode_material_design.model.storage.SettingShared;

public final class AlertDialogUtils {

    private AlertDialogUtils() {}

    public static AlertDialog.Builder createBuilderWithAutoTheme(@NonNull Activity activity) {
        return new AlertDialog.Builder(activity, SettingShared.isEnableThemeDark(activity) ? R.style.AppDialogDark_Alert : R.style.AppDialogLight_Alert);
    }

}
