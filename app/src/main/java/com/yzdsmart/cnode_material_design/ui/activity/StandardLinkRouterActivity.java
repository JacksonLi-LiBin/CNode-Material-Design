package com.yzdsmart.cnode_material_design.ui.activity;

import android.os.Bundle;

import com.yzdsmart.cnode_material_design.R;
import com.yzdsmart.cnode_material_design.ui.base.BaseActivity;
import com.yzdsmart.cnode_material_design.ui.util.Navigator;
import com.yzdsmart.cnode_material_design.ui.util.ToastUtils;

public class StandardLinkRouterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Navigator.openStandardLink(this, getIntent().getDataString())) {
            ToastUtils.with(this).show(R.string.invalid_link);
        }
        finish();
    }

}
