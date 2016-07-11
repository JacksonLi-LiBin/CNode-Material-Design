package com.yzdsmart.cnode_material_design.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.yzdsmart.cnode_material_design.R;
import com.yzdsmart.cnode_material_design.model.entity.Notification;
import com.yzdsmart.cnode_material_design.presenter.contract.INotificationPresenter;
import com.yzdsmart.cnode_material_design.presenter.implement.NotificationPresenter;
import com.yzdsmart.cnode_material_design.ui.adapter.MessageListAdapter;
import com.yzdsmart.cnode_material_design.ui.base.StatusBarActivity;
import com.yzdsmart.cnode_material_design.ui.listener.DoubleClickBackToContentTopListener;
import com.yzdsmart.cnode_material_design.ui.listener.NavigationFinishClickListener;
import com.yzdsmart.cnode_material_design.ui.util.RefreshUtils;
import com.yzdsmart.cnode_material_design.ui.util.ThemeUtils;
import com.yzdsmart.cnode_material_design.ui.view.IBackToContentTopView;
import com.yzdsmart.cnode_material_design.ui.view.INotificationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationActivity extends StatusBarActivity implements INotificationView, IBackToContentTopView, Toolbar.OnMenuItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.refresh_layout)
    protected SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recycler_view)
    protected RecyclerView recyclerView;

    @BindView(R.id.icon_no_data)
    protected View iconNoData;

    private MessageListAdapter adapter;

    private INotificationPresenter notificationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));
        toolbar.inflateMenu(R.menu.notification);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setOnClickListener(new DoubleClickBackToContentTopListener(this));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessageListAdapter(this);
        recyclerView.setAdapter(adapter);

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
        adapter.getMessageList().clear();
        adapter.getMessageList().addAll(notification.getHasNotReadMessageList());
        adapter.getMessageList().addAll(notification.getHasReadMessageList());
        adapter.notifyDataSetChanged();
        iconNoData.setVisibility(adapter.getMessageList().size() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onGetMessagesFinish() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onMarkAllMessageReadOk() {
        adapter.markAllMessageRead();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void backToContentTop() {
        recyclerView.scrollToPosition(0);
    }

}
