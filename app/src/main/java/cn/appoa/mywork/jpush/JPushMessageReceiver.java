package cn.appoa.mywork.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 推送自定义消息的监听
 */
public class JPushMessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (JPushConstant.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
            String message = intent.getStringExtra(JPushConstant.KEY_MESSAGE);
            String extras = intent.getStringExtra(JPushConstant.KEY_EXTRAS);
            StringBuilder showMsg = new StringBuilder();
            showMsg.append(JPushConstant.KEY_MESSAGE + " : " + message + "\n");
            if (!isEmpty(extras)) {
                showMsg.append(JPushConstant.KEY_EXTRAS + " : " + extras + "\n");
            }
            Log.d("自定义消息-->", showMsg.toString());
            if (onReceiveJPushMessageListener != null) {
                onReceiveJPushMessageListener.onReceiveJPushMessage(message, extras);
            }
        }
    }

    /**
     * 判断String是否为空
     *
     * @param s
     * @return
     */
    private boolean isEmpty(String s) {
        if (null == s)
            return true;
        if (s.length() == 0)
            return true;
        if (s.trim().length() == 0)
            return true;
        return false;
    }

    /**
     * 接收自定义消息
     */
    private OnReceiveJPushMessageListener onReceiveJPushMessageListener;

    /**
     * 设置接收自定义消息
     *
     * @param onReceiveJPushMessageListener 接收自定义消息
     */
    public void setOnReceiveJPushMessageListener(OnReceiveJPushMessageListener onReceiveJPushMessageListener) {
        this.onReceiveJPushMessageListener = onReceiveJPushMessageListener;
    }

    /**
     * 接收自定义消息接口
     */
    public interface OnReceiveJPushMessageListener {

        /**
         * 接收自定义消息
         *
         * @param message
         * @param extras
         */
        void onReceiveJPushMessage(String message, String extras);
    }
}
