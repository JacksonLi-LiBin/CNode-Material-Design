package com.yzdsmart.cnode_material_design.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.yzdsmart.cnode_material_design.R;
import com.yzdsmart.cnode_material_design.model.api.ApiClient;
import com.yzdsmart.cnode_material_design.model.api.DefaultCallback;
import com.yzdsmart.cnode_material_design.model.api.ForegroundCallback;
import com.yzdsmart.cnode_material_design.model.entity.Result;
import com.yzdsmart.cnode_material_design.model.entity.Topic;
import com.yzdsmart.cnode_material_design.model.entity.User;
import com.yzdsmart.cnode_material_design.presenter.contract.IUserDetailPresenter;
import com.yzdsmart.cnode_material_design.ui.util.ActivityUtils;
import com.yzdsmart.cnode_material_design.ui.view.IUserDetailView;
import com.yzdsmart.cnode_material_design.util.HandlerUtils;

import java.util.List;

import retrofit2.Response;

public class UserDetailPresenter implements IUserDetailPresenter {

    private final Activity activity;
    private final IUserDetailView userDetailView;

    private boolean loading = false;

    public UserDetailPresenter(@NonNull Activity activity, @NonNull IUserDetailView userDetailView) {
        this.activity = activity;
        this.userDetailView = userDetailView;
    }

    @Override
    public void getUserAsyncTask(@NonNull String loginName) {
        if (!loading) {
            loading = true;
            userDetailView.onGetUserStart();
            ApiClient.service.getUser(loginName).enqueue(new ForegroundCallback<Result.Data<User>>(activity) {

                private long startLoadingTime = System.currentTimeMillis();

                private long getPostTime() {
                    long postTime = 1000 - (System.currentTimeMillis() - startLoadingTime);
                    if (postTime > 0) {
                        return postTime;
                    } else {
                        return 0;
                    }
                }

                @Override
                public boolean onResultOk(final Response<Result.Data<User>> response, final Result.Data<User> result) {
                    HandlerUtils.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (ActivityUtils.isAlive(getActivity())) {
                                userDetailView.onGetUserOk(result.getData());
                                onFinish();
                            }
                        }

                    }, getPostTime());
                    return true;
                }

                @Override
                public boolean onResultError(final Response<Result.Data<User>> response, final Result.Error error) {
                    HandlerUtils.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (ActivityUtils.isAlive(getActivity())) {
                                userDetailView.onGetUserError(response.code() == 404 ? error.getErrorMessage() : getActivity().getString(R.string.data_load_faild_and_click_avatar_to_reload));
                                onFinish();
                            }
                        }

                    }, getPostTime());
                    return true;
                }

                @Override
                public boolean onCallException(Throwable t, Result.Error error) {
                    HandlerUtils.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (ActivityUtils.isAlive(getActivity())) {
                                userDetailView.onGetUserError(getActivity().getString(R.string.data_load_faild_and_click_avatar_to_reload));
                                onFinish();
                            }
                        }

                    }, getPostTime());
                    return true;
                }

                @Override
                public void onFinish() {
                    userDetailView.onGetUserFinish();
                    loading = false;
                }

            });
            ApiClient.service.getCollectTopicList(loginName).enqueue(new DefaultCallback<Result.Data<List<Topic>>>(activity) {

                @Override
                public boolean onResultOk(Response<Result.Data<List<Topic>>> response, Result.Data<List<Topic>> result) {
                    userDetailView.onGetCollectTopicListOk(result.getData());
                    return false;
                }

            });
        }
    }

}
