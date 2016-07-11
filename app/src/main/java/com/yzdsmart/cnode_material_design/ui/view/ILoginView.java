package com.yzdsmart.cnode_material_design.ui.view;

import android.support.annotation.NonNull;

import com.yzdsmart.cnode_material_design.model.entity.Result;

import retrofit2.Call;

public interface ILoginView {

    void onAccessTokenError(@NonNull String message);

    void onLoginOk(@NonNull String accessToken, @NonNull Result.Login loginInfo);

    void onLoginStart(@NonNull Call<Result.Login> call);

    void onLoginFinish();

}
