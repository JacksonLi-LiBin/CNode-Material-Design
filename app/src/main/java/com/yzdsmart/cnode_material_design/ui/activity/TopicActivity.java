package com.yzdsmart.cnode_material_design.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;
import com.yzdsmart.cnode_material_design.R;
import com.yzdsmart.cnode_material_design.model.api.ApiDefine;
import com.yzdsmart.cnode_material_design.model.entity.Reply;
import com.yzdsmart.cnode_material_design.model.entity.Topic;
import com.yzdsmart.cnode_material_design.model.entity.TopicWithReply;
import com.yzdsmart.cnode_material_design.model.storage.SettingShared;
import com.yzdsmart.cnode_material_design.model.util.EntityUtils;
import com.yzdsmart.cnode_material_design.presenter.contract.ITopicPresenter;
import com.yzdsmart.cnode_material_design.presenter.implement.TopicPresenter;
import com.yzdsmart.cnode_material_design.ui.adapter.ReplyListAdapter;
import com.yzdsmart.cnode_material_design.ui.base.StatusBarActivity;
import com.yzdsmart.cnode_material_design.ui.dialog.AlertDialogUtils;
import com.yzdsmart.cnode_material_design.ui.dialog.CreateReplyDialog;
import com.yzdsmart.cnode_material_design.ui.listener.DoubleClickBackToContentTopListener;
import com.yzdsmart.cnode_material_design.ui.listener.NavigationFinishClickListener;
import com.yzdsmart.cnode_material_design.ui.util.Navigator;
import com.yzdsmart.cnode_material_design.ui.util.RefreshUtils;
import com.yzdsmart.cnode_material_design.ui.util.ThemeUtils;
import com.yzdsmart.cnode_material_design.ui.view.IBackToContentTopView;
import com.yzdsmart.cnode_material_design.ui.view.ICreateReplyView;
import com.yzdsmart.cnode_material_design.ui.view.ITopicView;
import com.yzdsmart.cnode_material_design.ui.viewholder.TopicHeader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopicActivity extends StatusBarActivity implements ITopicView, IBackToContentTopView, SwipeRefreshLayout.OnRefreshListener, Toolbar.OnMenuItemClickListener {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.refresh_layout)
    protected SwipeRefreshLayout refreshLayout;

    @BindView(R.id.list_view)
    protected ListView listView;

    @BindView(R.id.icon_no_data)
    protected View iconNoData;

    @BindView(R.id.fab_reply)
    protected FloatingActionButton fabReply;

    private String topicId;
    private Topic topic;

    private ICreateReplyView createReplyView;
    private TopicHeader header;
    private ReplyListAdapter adapter;

    private ITopicPresenter topicPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        ButterKnife.bind(this);

        if (SettingShared.isShowTopicRenderCompatTip(this)) {
            SettingShared.markShowTopicRenderCompatTip(this);
            AlertDialogUtils.createBuilderWithAutoTheme(this)
                    .setMessage(R.string.topic_render_compat_tip)
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }

        topicId = getIntent().getStringExtra(Navigator.TopicWithAutoCompat.EXTRA_TOPIC_ID);

        if (!TextUtils.isEmpty(getIntent().getStringExtra(Navigator.TopicWithAutoCompat.EXTRA_TOPIC))) {
            topic = EntityUtils.gson.fromJson(getIntent().getStringExtra(Navigator.TopicWithAutoCompat.EXTRA_TOPIC), Topic.class);
        }

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));
        toolbar.inflateMenu(R.menu.topic);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setOnClickListener(new DoubleClickBackToContentTopListener(this));

        createReplyView = CreateReplyDialog.createWithAutoTheme(this, topicId, this);
        header = new TopicHeader(this, listView);
        header.updateViews(topic, false, 0);
        adapter = new ReplyListAdapter(this, createReplyView);
        listView.setAdapter(adapter);

        iconNoData.setVisibility(topic == null ? View.VISIBLE : View.GONE);

        fabReply.attachToListView(listView);

        topicPresenter = new TopicPresenter(this, this);

        RefreshUtils.init(refreshLayout, this);
        RefreshUtils.refresh(refreshLayout, this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                if (topic != null) {
                    Navigator.openShare(this, "《" + topic.getTitle() + "》\n" + ApiDefine.TOPIC_LINK_URL_PREFIX + topicId + "\n—— 来自CNode社区");
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onRefresh() {
        topicPresenter.getTopicAsyncTask(topicId);
    }

    @OnClick(R.id.fab_reply)
    protected void onBtnReplyClick() {
        if (topic != null && LoginActivity.startForResultWithAccessTokenCheck(this)) {
            createReplyView.showWindow();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LoginActivity.REQUEST_LOGIN && resultCode == RESULT_OK) {
            refreshLayout.setRefreshing(true);
            onRefresh();
        }
    }

    @Override
    public void onGetTopicOk(@NonNull TopicWithReply topic) {
        this.topic = topic;
        header.updateViews(topic);
        adapter.setReplyList(topic.getReplyList());
        adapter.notifyDataSetChanged();
        iconNoData.setVisibility(View.GONE);
    }

    @Override
    public void onGetTopicFinish() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void appendReplyAndUpdateViews(@NonNull Reply reply) {
        adapter.addReply(reply);
        adapter.notifyDataSetChanged();
        header.updateReplyCount(adapter.getReplyList().size());
        listView.smoothScrollToPosition(adapter.getReplyList().size());
    }

    @Override
    public void backToContentTop() {
        listView.setSelection(0);
    }

}
