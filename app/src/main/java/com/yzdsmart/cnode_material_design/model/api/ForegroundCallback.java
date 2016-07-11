package com.yzdsmart.cnode_material_design.model.api;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.yzdsmart.cnode_material_design.model.entity.Result;
import com.yzdsmart.cnode_material_design.ui.util.ActivityUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForegroundCallback<T extends Result> implements Callback<T>, CallbackLifecycle<T> {

    private final Activity activity;

    public ForegroundCallback(@NonNull Activity activity) {
        this.activity = activity;
    }

    @NonNull
    protected final Activity getActivity() {
        return activity;
    }

    @Override
    public final void onResponse(Call<T> call, Response<T> response) {
        if (ActivityUtils.isAlive(activity)) {
            boolean interrupt;
            if (response.isSuccessful()) {
                interrupt = onResultOk(response, response.body());
            } else {
                interrupt = onResultError(response, Result.buildError(response));
            }
            if (!interrupt) {
                onFinish();
            }
        }
    }

    @Override
    public final void onFailure(Call<T> call, Throwable t) {
        if (ActivityUtils.isAlive(activity)) {
            boolean interrupt;
            if (call.isCanceled()) {
                interrupt = onCallCancel();
            } else {
                interrupt = onCallException(t, Result.buildError(t));
            }
            if (!interrupt) {
                onFinish();
            }
        }
    }

    @Override
    public boolean onResultOk(Response<T> response, T result) {
        return false;
    }

    @Override
    public boolean onResultError(Response<T> response, Result.Error error) {
        return false;
    }

    @Override
    public boolean onCallCancel() {
        return false;
    }

    @Override
    public boolean onCallException(Throwable t, Result.Error error) {
        return false;
    }

    @Override
    public void onFinish() {}

}
