package com.yzdsmart.cnode_material_design.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;

import com.yzdsmart.cnode_material_design.R;
import com.yzdsmart.cnode_material_design.ui.base.StatusBarActivity;
import com.yzdsmart.cnode_material_design.ui.listener.NavigationFinishClickListener;
import com.yzdsmart.cnode_material_design.ui.util.ThemeUtils;
import com.yzdsmart.cnode_material_design.ui.widget.PreviewWebView;
import com.yzdsmart.cnode_material_design.util.FormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarkdownPreviewActivity extends StatusBarActivity {

    private static final String EXTRA_MARKDOWN = "markdown";

    public static void start(@NonNull Activity activity, String markdown) {
        Intent intent = new Intent(activity, MarkdownPreviewActivity.class);
        intent.putExtra(EXTRA_MARKDOWN, markdown);
        activity.startActivity(intent);
    }

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.web_preview)
    protected PreviewWebView webPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markdown_preview);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));

        String markdown = getIntent().getStringExtra(EXTRA_MARKDOWN);
        webPreview.loadRenderedContent(FormatUtils.handleHtml(FormatUtils.renderMarkdown(markdown)));
    }

    @Override
    public void onBackPressed() {
        if (webPreview.canGoBack()) {
            webPreview.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
