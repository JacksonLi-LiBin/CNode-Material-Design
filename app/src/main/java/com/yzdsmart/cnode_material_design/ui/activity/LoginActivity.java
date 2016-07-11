package com.yzdsmart.cnode_material_design.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.yzdsmart.cnode_material_design.R;
import com.yzdsmart.cnode_material_design.model.entity.Result;
import com.yzdsmart.cnode_material_design.model.storage.LoginShared;
import com.yzdsmart.cnode_material_design.presenter.contract.ILoginPresenter;
import com.yzdsmart.cnode_material_design.presenter.implement.LoginPresenter;
import com.yzdsmart.cnode_material_design.ui.base.FullLayoutActivity;
import com.yzdsmart.cnode_material_design.ui.dialog.AlertDialogUtils;
import com.yzdsmart.cnode_material_design.ui.dialog.ProgressDialog;
import com.yzdsmart.cnode_material_design.ui.listener.DialogCancelCallListener;
import com.yzdsmart.cnode_material_design.ui.listener.NavigationFinishClickListener;
import com.yzdsmart.cnode_material_design.ui.util.DisplayUtils;
import com.yzdsmart.cnode_material_design.ui.util.ThemeUtils;
import com.yzdsmart.cnode_material_design.ui.util.ToastUtils;
import com.yzdsmart.cnode_material_design.ui.view.ILoginView;
import com.yzdsmart.cnode_material_design.util.FormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class LoginActivity extends FullLayoutActivity implements ILoginView {

    public static final int REQUEST_LOGIN = FormatUtils.createRequestCode();

    public static void startForResult(@NonNull Activity activity) {
        activity.startActivityForResult(new Intent(activity, LoginActivity.class), REQUEST_LOGIN);
    }

    public static boolean startForResultWithAccessTokenCheck(@NonNull final Activity activity) {
        if (TextUtils.isEmpty(LoginShared.getAccessToken(activity))) {
            AlertDialogUtils.createBuilderWithAutoTheme(activity)
                    .setMessage(R.string.need_login_tip)
                    .setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startForResult(activity);
                        }

                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
            return false;
        } else {
            return true;
        }
    }

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.adapt_status_bar)
    protected View adaptStatusBar;

    @BindView(R.id.edt_access_token)
    protected MaterialEditText edtAccessToken;

    private ProgressDialog progressDialog;

    private ILoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight_FitsStatusBar, R.style.AppThemeDark_FitsStatusBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        DisplayUtils.adaptStatusBar(this, adaptStatusBar);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));

        progressDialog = ProgressDialog.createWithAutoTheme(this);
        progressDialog.setMessage(R.string.logging_in_$_);

        loginPresenter = new LoginPresenter(this, this);
    }

    @OnClick(R.id.btn_login)
    protected void onBtnLoginClick() {
        loginPresenter.loginAsyncTask(edtAccessToken.getText().toString().trim());
    }

    @OnClick(R.id.btn_qrcode)
    protected void onBtnQrcodeClick() {
        QRCodeActivity.startForResultWithPermissionCheck(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == QRCodeActivity.PERMISSIONS_REQUEST_QRCODE) {
            QRCodeActivity.startForResultWithPermissionHandle(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QRCodeActivity.REQUEST_QRCODE && resultCode == RESULT_OK) {
            edtAccessToken.setText(data.getStringExtra(QRCodeActivity.EXTRA_QRCODE));
            edtAccessToken.setSelection(edtAccessToken.length());
            onBtnLoginClick();
        }
    }

    @OnClick(R.id.btn_login_tip)
    protected void onBtnLoginTipClick() {
        AlertDialogUtils.createBuilderWithAutoTheme(this)
                .setMessage(R.string.how_to_get_access_token_tip_content)
                .setPositiveButton(R.string.confirm, null)
                .show();
    }

    @Override
    public void onAccessTokenError(@NonNull String message) {
        edtAccessToken.setError(message);
        edtAccessToken.requestFocus();
    }

    @Override
    public void onLoginOk(@NonNull String accessToken, @NonNull Result.Login loginInfo) {
        LoginShared.login(this, accessToken, loginInfo);
        ToastUtils.with(this).show(R.string.login_success);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onLoginStart(@NonNull Call<Result.Login> call) {
        progressDialog.setOnCancelListener(new DialogCancelCallListener(call));
        progressDialog.show();
    }

    @Override
    public void onLoginFinish() {
        progressDialog.setOnCancelListener(null);
        progressDialog.dismiss();
    }

}
