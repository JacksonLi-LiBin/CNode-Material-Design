package com.yzdsmart.cnode_material_design.model.api;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

import com.yzdsmart.cnode_material_design.R;
import com.yzdsmart.cnode_material_design.model.entity.Result;
import com.yzdsmart.cnode_material_design.ui.activity.LoginActivity;
import com.yzdsmart.cnode_material_design.ui.dialog.AlertDialogUtils;
import com.yzdsmart.cnode_material_design.ui.util.ToastUtils;

import retrofit2.Response;

public class DefaultCallback<T extends Result> extends ForegroundCallback<T> {

    public DefaultCallback(@NonNull Activity activity) {
        super(activity);
    }

    @Override
    public final boolean onResultError(Response<T> response, Result.Error error) {
        if (response.code() == 401) {
            return onResultAuthError(response, error);
        } else {
            return onResultOtherError(response, error);
        }
    }

    public boolean onResultAuthError(Response<T> response, Result.Error error) {
        AlertDialogUtils.createBuilderWithAutoTheme(getActivity())
                .setMessage(R.string.access_token_out_of_date)
                .setPositiveButton(R.string.relogin, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginActivity.startForResult(getActivity());
                    }

                })
                .setNegativeButton(R.string.cancel, null)
                .show();
        return false;
    }

    public boolean onResultOtherError(Response<T> response, Result.Error error) {
        ToastUtils.with(getActivity()).show(error.getErrorMessage());
        return false;
    }

    @Override
    public boolean onCallException(Throwable t, Result.Error error) {
        ToastUtils.with(getActivity()).show(error.getErrorMessage());
        return false;
    }

}
