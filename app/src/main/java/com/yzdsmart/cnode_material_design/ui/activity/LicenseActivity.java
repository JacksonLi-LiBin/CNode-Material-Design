package com.yzdsmart.cnode_material_design.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.yzdsmart.cnode_material_design.R;
import com.yzdsmart.cnode_material_design.ui.base.StatusBarActivity;
import com.yzdsmart.cnode_material_design.ui.listener.NavigationFinishClickListener;
import com.yzdsmart.cnode_material_design.ui.util.ThemeUtils;
import com.yzdsmart.cnode_material_design.ui.util.ToastUtils;
import com.yzdsmart.cnode_material_design.util.ResUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LicenseActivity extends StatusBarActivity {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.tv_license)
    protected TextView tvLicense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));

        try {
            tvLicense.setText(ResUtils.getRawString(this, R.raw.open_source));
        } catch (IOException e) {
            tvLicense.setText(null);
            ToastUtils.with(this).show("资源读取失败");
        }
    }

}
