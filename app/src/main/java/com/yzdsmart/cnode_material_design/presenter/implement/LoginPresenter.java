package com.yzdsmart.cnode_material_design.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.yzdsmart.cnode_material_design.R;
import com.yzdsmart.cnode_material_design.model.api.ApiClient;
import com.yzdsmart.cnode_material_design.model.api.DefaultCallback;
import com.yzdsmart.cnode_material_design.model.entity.Result;
import com.yzdsmart.cnode_material_design.presenter.contract.ILoginPresenter;
import com.yzdsmart.cnode_material_design.ui.view.ILoginView;
import com.yzdsmart.cnode_material_design.util.FormatUtils;

import retrofit2.Call;
import retrofit2.Response;

public class LoginPresenter implements ILoginPresenter {

    private final Activity activity;
    private final ILoginView loginView;

    public LoginPresenter(@NonNull Activity activity, @NonNull ILoginView loginView) {
        this.activity = activity;
        this.loginView = loginView;
    }

    @Override
    public void loginAsyncTask(final String accessToken) {
        if (!FormatUtils.isAccessToken(accessToken)) {
            loginView.onAccessTokenError(activity.getString(R.string.access_token_format_error));
        } else {
            Call<Result.Login> call = ApiClient.service.accessToken(accessToken);
            loginView.onLoginStart(call);
            call.enqueue(new DefaultCallback<Result.Login>(activity) {

                @Override
                public boolean onResultOk(Response<Result.Login> response, Result.Login loginInfo) {
                    loginView.onLoginOk(accessToken, loginInfo);
                    return false;
                }

                @Override
                public boolean onResultAuthError(Response<Result.Login> response, Result.Error error) {
                    loginView.onAccessTokenError(getActivity().getString(R.string.access_token_auth_error));
                    return false;
                }

                @Override
                public void onFinish() {
                    loginView.onLoginFinish();
                }

            });
        }
    }

}
