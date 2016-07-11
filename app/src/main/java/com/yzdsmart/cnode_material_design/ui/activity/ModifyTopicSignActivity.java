package com.yzdsmart.cnode_material_design.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.yzdsmart.cnode_material_design.R;
import com.yzdsmart.cnode_material_design.model.storage.SettingShared;
import com.yzdsmart.cnode_material_design.ui.base.StatusBarActivity;
import com.yzdsmart.cnode_material_design.ui.listener.NavigationFinishClickListener;
import com.yzdsmart.cnode_material_design.ui.util.ThemeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ModifyTopicSignActivity extends StatusBarActivity implements Toolbar.OnMenuItemClickListener {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.edt_content)
    protected EditText edtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_topic_sign);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));
        toolbar.inflateMenu(R.menu.modify_topic_sign);
        toolbar.setOnMenuItemClickListener(this);

        edtContent.setText(SettingShared.getTopicSignContent(this));
        edtContent.setSelection(edtContent.length());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_default:
                edtContent.setText(SettingShared.DEFAULT_TOPIC_SIGN_CONTENT);
                edtContent.setSelection(edtContent.length());
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SettingShared.setTopicSignContent(this, edtContent.getText().toString());
    }

}
