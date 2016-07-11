package com.yzdsmart.cnode_material_design.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yzdsmart.cnode_material_design.R;
import com.yzdsmart.cnode_material_design.model.entity.Message;
import com.yzdsmart.cnode_material_design.ui.activity.UserDetailActivity;
import com.yzdsmart.cnode_material_design.ui.util.Navigator;
import com.yzdsmart.cnode_material_design.ui.widget.ContentWebView;
import com.yzdsmart.cnode_material_design.util.FormatUtils;
import com.yzdsmart.cnode_material_design.util.ResUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {

    private final Activity activity;
    private final LayoutInflater inflater;
    private final List<Message> messageList = new ArrayList<>();

    public MessageListAdapter(@NonNull Activity activity) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
    }

    @NonNull
    public List<Message> getMessageList() {
        return messageList;
    }

    public void markAllMessageRead() {
        for (Message message : messageList) {
            message.setRead(true);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_message, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.update(messageList.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_avatar)
        protected ImageView imgAvatar;

        @BindView(R.id.tv_from)
        protected TextView tvFrom;

        @BindView(R.id.tv_time)
        protected TextView tvTime;

        @BindView(R.id.tv_action)
        protected TextView tvAction;

        @BindView(R.id.badge_read)
        protected View badgeRead;

        @BindView(R.id.web_content)
        protected ContentWebView webContent;

        @BindView(R.id.tv_topic_title)
        protected TextView tvTopicTitle;

        private Message message;

        protected ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void update(@NonNull Message message) {
            this.message = message;

            Glide.with(activity).load(message.getAuthor().getAvatarUrl()).placeholder(R.drawable.image_placeholder).dontAnimate().into(imgAvatar);
            tvFrom.setText(message.getAuthor().getLoginName());
            tvTime.setText(FormatUtils.getRelativeTimeSpanString(message.getCreateAt()));
            tvTime.setTextColor(ResUtils.getThemeAttrColor(activity, message.isRead() ? android.R.attr.textColorSecondary : R.attr.colorAccent));
            badgeRead.setVisibility(message.isRead() ? View.GONE : View.VISIBLE);
            tvTopicTitle.setText("话题：" + message.getTopic().getTitle());

            // 判断通知类型
            if (message.getType() == Message.Type.at) {
                if (message.getReply() == null || TextUtils.isEmpty(message.getReply().getId())) {
                    tvAction.setText("在话题中@了您");
                    webContent.setVisibility(View.GONE);
                } else {
                    tvAction.setText("在回复中@了您");
                    webContent.setVisibility(View.VISIBLE);
                    webContent.loadRenderedContent(message.getReply().getContentHtml());  // 这里直接使用WebView，有性能问题
                }
            } else {
                tvAction.setText("回复了您的话题");
                webContent.setVisibility(View.VISIBLE);
                webContent.loadRenderedContent(message.getReply().getContentHtml());  // 这里直接使用WebView，有性能问题
            }
        }

        @OnClick(R.id.img_avatar)
        protected void onBtnAvatarClick() {
            UserDetailActivity.startWithTransitionAnimation(activity, message.getAuthor().getLoginName(), imgAvatar, message.getAuthor().getAvatarUrl());
        }

        @OnClick(R.id.btn_item)
        protected void onBtnItemClick() {
            Navigator.TopicWithAutoCompat.start(activity, message.getTopic().getId());
        }

    }

}
