package cn.appoa.mywork.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.wangzhen.statusbar.DarkStatusBar;

import butterknife.ButterKnife;
import cn.appoa.aframework.activity.AfActivity;
import cn.appoa.aframework.dialog.DefaultHintDialog;
import cn.appoa.aframework.listener.ConfirmHintDialogListener;
import cn.appoa.aframework.manager.AtyManager;
import cn.appoa.aframework.presenter.BasePresenter;
import cn.appoa.aframework.utils.AtyUtils;
import cn.appoa.aframework.utils.SpUtils;
import cn.appoa.aframework.view.ILoginView;
import cn.appoa.mywork.R;
import cn.appoa.mywork.jpush.JPushUtils;
import cn.appoa.mywork.net.API;
import cn.appoa.mywork.ui.LoginActivity;
import zm.bus.event.BusProvider;

public abstract class BaseActivity<P extends BasePresenter> extends AfActivity<P>
        implements ILoginView {

    @Override
    public void bindButterKnife() {
        ButterKnife.bind(this);
    }

    @Override
    public void unBindButterKnife() {
        //ButterKnife.unbind(this);
        ButterKnife.bind(this).unbind();
    }

    @Override
    public void initView() {
        DarkStatusBar.get().fitDark(this);//深色模式
        if (titlebar != null) {
            titlebar.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorDefaultTitlebarBg));
            AtyUtils.setPaddingTop(mActivity, titlebar);
        }
    }

    @Override
    public boolean isLogin() {
        return API.isLogin();
    }

    @Override
    public void toLoginActivity() {
        startActivityForResult(new Intent(mActivity, LoginActivity.class), 999);
    }

    @Override
    public void toLoginSuccess(Intent data) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999 && resultCode == -1 && data != null) {
            toLoginSuccess(data);
        }
    }


    /**
     * 新消息
     */
    public static final String ACTION_NEW_MESSAGE_RECEIVED = "action_new_message_received";

    /**
     * 广播监听
     */
    public class MyActivityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if (TextUtils.equals(action, BaseActivity.ACTION_NEW_MESSAGE_RECEIVED)) {
                    onReceivedMessage();
                } else {
                    onReceivedBroadcast(context, intent);
                }
            }
        }
    }

    /**
     * 收到新消息广播
     */
    protected void onReceivedMessage() {
        AtyUtils.i("MyActivityReceiver", "收到新消息");
    }

    /**
     * 收到其他消息广播
     *
     * @param context
     * @param intent
     */
    protected void onReceivedBroadcast(Context context, Intent intent) {
        AtyUtils.i("MyActivityReceiver", "收到广播");
    }

    protected LocalBroadcastManager broadcastManager;
    protected IntentFilter intentFilter;
    protected MyActivityReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 广播监听
        if (broadcastManager == null) {
            broadcastManager = LocalBroadcastManager.getInstance(this);
        }
        if (intentFilter == null) {
            intentFilter = new IntentFilter();
        }
        intentFilter.addAction(BaseActivity.ACTION_NEW_MESSAGE_RECEIVED);
        if (receiver == null) {
            receiver = new MyActivityReceiver();
        }
        broadcastManager.registerReceiver(receiver, intentFilter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 广播监听
        if (broadcastManager != null && receiver != null) {
            broadcastManager.unregisterReceiver(receiver);
        }
    }


    /**
     * 退出
     */
    protected void exitApp() {

        // 本地数据删除
        SpUtils.clearData(mActivity);
        JPushUtils.getInstance().setAlias("");
        AtyUtils.showShort(mActivity, "退出成功", false);
        //BusProvider.getInstance().post(new LoginEvent(2));
        finish();
    }


    @Override
    public boolean enableSliding() {
        return true;
    }

}
