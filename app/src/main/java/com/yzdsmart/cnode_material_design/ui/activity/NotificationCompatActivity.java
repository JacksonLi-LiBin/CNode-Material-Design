package com.yzdsmart.cnode_material_design.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.yzdsmart.cnode_material_design.R;
import com.yzdsmart.cnode_material_design.model.entity.Message;
import com.yzdsmart.cnode_material_design.model.entity.Notification;
import com.yzdsmart.cnode_material_design.presenter.contract.INotificationPresenter;
import com.yzdsmart.cnode_material_design.presenter.implement.NotificationPresenter;
import com.yzdsmart.cnode_material_design.ui.base.StatusBarActivity;
import com.yzdsmart.cnode_material_design.ui.listener.DoubleClickBackToContentTopListener;
import com.yzdsmart.cnode_material_design.ui.listener.NavigationFinishClickListener;
import com.yzdsmart.cnode_material_design.ui.util.RefreshUtils;
import com.yzdsmart.cnode_material_design.ui.util.ThemeUtils;
import com.yzdsmart.cnode_material_design.ui.view.INotificationView;
import com.yzdsmart.cnode_material_design.ui.widget.NotificationWebView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationCompatActivity extends StatusBarActivity implements INotificationView, Toolbar.OnMenuItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.refresh_layout)
    protected SwipeRefreshLayout refreshLayout;

    @BindView(R.id.web_notification)
    protected NotificationWebView webNotification;

    @BindView(R.id.icon_no_data)
    protected View iconNoData;

    private INotificationPresenter notificationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_compat);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));
        toolbar.inflateMenu(R.menu.notification);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setOnClickListener(new DoubleClickBackToContentTopListener(webNotification));

        notificationPresenter = new NotificationPresenter(this, this);

        RefreshUtils.init(refreshLayout, this);
        RefreshUtils.refresh(refreshLayout, this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done_all:
                notificationPresenter.markAllMessageReadAsyncTask();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onRefresh() {
        notificationPresenter.getMessagesAsyncTask();
    }

    @Override
    public void onGetMessagesOk(@NonNull Notification notification) {
        List<Message> messageList = new ArrayList<>();
        messageList.addAll(notification.getHasNotReadMessageList());
        messageList.addAll(notification.getHasReadMessageList());
        webNotification.updateMessageList(messageList);
        iconNoData.setVisibility(messageList.size() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onGetMessagesFinish() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onMarkAllMessageReadOk() {
        webNotification.markAllMessageRead();
    }

}
