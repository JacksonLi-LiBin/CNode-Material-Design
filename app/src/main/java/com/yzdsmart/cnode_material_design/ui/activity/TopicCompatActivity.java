package com.yzdsmart.cnode_material_design.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.melnykov.fab.FloatingActionButton;
import com.yzdsmart.cnode_material_design.R;
import com.yzdsmart.cnode_material_design.model.api.ApiDefine;
import com.yzdsmart.cnode_material_design.model.entity.Reply;
import com.yzdsmart.cnode_material_design.model.entity.Topic;
import com.yzdsmart.cnode_material_design.model.entity.TopicWithReply;
import com.yzdsmart.cnode_material_design.model.storage.LoginShared;
import com.yzdsmart.cnode_material_design.presenter.contract.IReplyPresenter;
import com.yzdsmart.cnode_material_design.presenter.contract.ITopicHeaderPresenter;
import com.yzdsmart.cnode_material_design.presenter.contract.ITopicPresenter;
import com.yzdsmart.cnode_material_design.presenter.implement.ReplyPresenter;
import com.yzdsmart.cnode_material_design.presenter.implement.TopicHeaderPresenter;
import com.yzdsmart.cnode_material_design.presenter.implement.TopicPresenter;
import com.yzdsmart.cnode_material_design.ui.base.StatusBarActivity;
import com.yzdsmart.cnode_material_design.ui.dialog.CreateReplyDialog;
import com.yzdsmart.cnode_material_design.ui.listener.DoubleClickBackToContentTopListener;
import com.yzdsmart.cnode_material_design.ui.listener.NavigationFinishClickListener;
import com.yzdsmart.cnode_material_design.ui.listener.TopicJavascriptInterface;
import com.yzdsmart.cnode_material_design.ui.util.Navigator;
import com.yzdsmart.cnode_material_design.ui.util.RefreshUtils;
import com.yzdsmart.cnode_material_design.ui.util.ThemeUtils;
import com.yzdsmart.cnode_material_design.ui.view.ICreateReplyView;
import com.yzdsmart.cnode_material_design.ui.view.IReplyView;
import com.yzdsmart.cnode_material_design.ui.view.ITopicHeaderView;
import com.yzdsmart.cnode_material_design.ui.view.ITopicView;
import com.yzdsmart.cnode_material_design.ui.widget.TopicWebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopicCompatActivity extends StatusBarActivity implements ITopicView, ITopicHeaderView, IReplyView, SwipeRefreshLayout.OnRefreshListener, Toolbar.OnMenuItemClickListener {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.refresh_layout)
    protected SwipeRefreshLayout refreshLayout;

    @BindView(R.id.web_topic)
    protected TopicWebView webTopic;

    @BindView(R.id.icon_no_data)
    protected View iconNoData;

    @BindView(R.id.fab_reply)
    protected FloatingActionButton fabReply;

    private String topicId;
    private Topic topic;

    private ICreateReplyView createReplyView;

    private ITopicPresenter topicPresenter;
    private ITopicHeaderPresenter topicHeaderPresenter;
    private IReplyPresenter replyPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_compat);
        ButterKnife.bind(this);

        topicId = getIntent().getStringExtra(Navigator.TopicWithAutoCompat.EXTRA_TOPIC_ID);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));
        toolbar.inflateMenu(R.menu.topic);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setOnClickListener(new DoubleClickBackToContentTopListener(webTopic));

        topicPresenter = new TopicPresenter(this, this);
        topicHeaderPresenter = new TopicHeaderPresenter(this, this);
        replyPresenter = new ReplyPresenter(this, this);

        createReplyView = CreateReplyDialog.createWithAutoTheme(this, topicId, this);

        webTopic.setFabReply(fabReply);
        webTopic.setBridgeAndLoadPage(new TopicJavascriptInterface(this, createReplyView, topicHeaderPresenter, replyPresenter));

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
        webTopic.updateTopicAndUserId(topic, LoginShared.getId(this));
        iconNoData.setVisibility(View.GONE);
    }

    @Override
    public void onGetTopicFinish() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void appendReplyAndUpdateViews(@NonNull Reply reply) {
        webTopic.appendReply(reply);
    }

    @Override
    public void onCollectTopicOk() {
        webTopic.updateTopicCollect(true);
    }

    @Override
    public void onDecollectTopicOk() {
        webTopic.updateTopicCollect(false);
    }

    @Override
    public void onUpReplyOk(@NonNull Reply reply) {
        webTopic.updateReply(reply);
    }

}
