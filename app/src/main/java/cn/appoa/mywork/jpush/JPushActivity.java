package cn.appoa.mywork.jpush;

import android.app.Activity;
import android.os.Bundle;

import cn.jpush.android.api.JPushInterface;

/**
 * 用于接收自定义消息的Activity
 */
public abstract class JPushActivity extends Activity implements JPushMessageReceiver.OnReceiveJPushMessageListener {

    /**
     * 自定义消息接收者
     */
    protected JPushMessageReceiver mJPushMessageReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initJPush();
        super.onCreate(savedInstanceState);
    }

    /**
     * 初始化 JPush
     */
    private void initJPush() {
        // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
        JPushInterface.init(getApplicationContext());
        // 自定义消息接收者
        mJPushMessageReceiver = new JPushMessageReceiver();
        mJPushMessageReceiver.setOnReceiveJPushMessageListener(this);
        // 注册自定义消息接收者
        JPushUtils.getInstance().registerMessageReceiver(this, mJPushMessageReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * 当前Activity是否可见
     */
    public static boolean isForeground = false;

    @Override
    protected void onResume() {
        isForeground = true;
        JPushInterface.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        isForeground = false;
        JPushInterface.onPause(this);
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // 解除注册自定义消息接收者
        JPushUtils.getInstance().unregisterReceiver(this, mJPushMessageReceiver);
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
