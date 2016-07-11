package com.yzdsmart.cnode_material_design.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.yzdsmart.cnode_material_design.R;
import com.yzdsmart.cnode_material_design.ui.base.BaseActivity;
import com.yzdsmart.cnode_material_design.ui.util.ActivityUtils;
import com.yzdsmart.cnode_material_design.util.HandlerUtils;

public class LaunchActivity extends BaseActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        HandlerUtils.postDelayed(this, 2000);
    }

    @Override
    public void run() {
        if (ActivityUtils.isAlive(this)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

}
