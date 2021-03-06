package com.yzdsmart.cnode_material_design.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.yzdsmart.cnode_material_design.R;
import com.yzdsmart.cnode_material_design.model.entity.TabType;
import com.yzdsmart.cnode_material_design.model.storage.SettingShared;
import com.yzdsmart.cnode_material_design.model.storage.TopicShared;
import com.yzdsmart.cnode_material_design.presenter.contract.ICreateTopicPresenter;
import com.yzdsmart.cnode_material_design.presenter.implement.CreateTopicPresenter;
import com.yzdsmart.cnode_material_design.ui.base.StatusBarActivity;
import com.yzdsmart.cnode_material_design.ui.dialog.ProgressDialog;
import com.yzdsmart.cnode_material_design.ui.listener.NavigationFinishClickListener;
import com.yzdsmart.cnode_material_design.ui.util.Navigator;
import com.yzdsmart.cnode_material_design.ui.util.ThemeUtils;
import com.yzdsmart.cnode_material_design.ui.util.ToastUtils;
import com.yzdsmart.cnode_material_design.ui.view.ICreateTopicView;
import com.yzdsmart.cnode_material_design.ui.widget.EditorBarHandler;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateTopicActivity extends StatusBarActivity implements Toolbar.OnMenuItemClickListener, ICreateTopicView {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.spn_tab)
    protected Spinner spnTab;

    @BindView(R.id.edt_title)
    protected EditText edtTitle;

    @BindView(R.id.layout_editor_bar)
    protected ViewGroup editorBar;

    @BindView(R.id.edt_content)
    protected EditText edtContent;

    private ProgressDialog progressDialog;

    private ICreateTopicPresenter createTopicPresenter;

    private boolean saveTopicDraft = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_topic);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));
        toolbar.inflateMenu(R.menu.create_topic);
        toolbar.setOnMenuItemClickListener(this);

        progressDialog = ProgressDialog.createWithAutoTheme(this);
        progressDialog.setMessage(R.string.posting_$_);
        progressDialog.setCancelable(false);

        // 创建EditorBar
        new EditorBarHandler(this, editorBar, edtContent);

        // 载入草稿
        if (SettingShared.isEnableTopicDraft(this)) {
            spnTab.setSelection(TopicShared.getDraftTabPosition(this));
            edtContent.setText(TopicShared.getDraftContent(this));
            edtContent.setSelection(edtContent.length());
            edtTitle.setText(TopicShared.getDraftTitle(this));
            edtTitle.setSelection(edtTitle.length()); // 这个必须最后调用
        }

        createTopicPresenter = new CreateTopicPresenter(this, this);
    }

    /**
     * 实时保存草稿
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (SettingShared.isEnableTopicDraft(this) && saveTopicDraft) {
            TopicShared.setDraftTabPosition(this, spnTab.getSelectedItemPosition());
            TopicShared.setDraftTitle(this, edtTitle.getText().toString());
            TopicShared.setDraftContent(this, edtContent.getText().toString());
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send:
                createTopicPresenter.createTopicAsyncTask(getTabByPosition(spnTab.getSelectedItemPosition()), edtTitle.getText().toString().trim(), edtContent.getText().toString().trim());
                return true;
            default:
                return false;
        }
    }

    private TabType getTabByPosition(int position) {
        switch (position) {
            case 0:
                return TabType.share;
            case 1:
                return TabType.ask;
            case 2:
                return TabType.job;
            default:
                return TabType.share;
        }
    }

    @Override
    public void onTitleError(@NonNull String message) {
        ToastUtils.with(this).show(message);
        edtTitle.requestFocus();
    }

    @Override
    public void onContentError(@NonNull String message) {
        ToastUtils.with(this).show(message);
        edtContent.requestFocus();
    }

    @Override
    public void onCreateTopicOk(@NonNull String topicId) {
        saveTopicDraft = false;
        TopicShared.clear(this);
        ToastUtils.with(this).show(R.string.post_success);
        Navigator.TopicWithAutoCompat.start(this, topicId);
        finish();
    }

    @Override
    public void onCreateTopicStart() {
        progressDialog.show();
    }

    @Override
    public void onCreateTopicFinish() {
        progressDialog.dismiss();
    }

}
