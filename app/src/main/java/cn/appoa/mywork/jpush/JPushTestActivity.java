package cn.appoa.mywork.jpush;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;

/**
 * 接收通知消息的Activity
 */
public class JPushTestActivity extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextView = new TextView(this);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = getIntent().getExtras();
            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            mTextView.setText("推送通知标题 : " + title + "\n" + "推送通知内容 : " + content + "\n" + "推送通知扩展信息: " + extra);
        }
        setContentView(mTextView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

}
