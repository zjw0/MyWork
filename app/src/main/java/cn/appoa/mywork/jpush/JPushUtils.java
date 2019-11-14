package cn.appoa.mywork.jpush;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONObject;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.jpush.android.data.JPushLocalNotification;

/**
 * 极光推送工具类
 */
public class JPushUtils {

    /**
     * 打印的TAG
     */
    private static final String TAG = JPushUtils.class.getSimpleName();

    /**
     * 构造器
     */
    private JPushUtils() {
    }

    /**
     * 本类实例
     */
    private static JPushUtils instance = null;

    /**
     * 单例模式创建实例
     *
     * @return instance
     */
    public synchronized static JPushUtils getInstance() {
        if (instance == null) {
            instance = new JPushUtils();
        }
        return instance;
    }

    /**
     * ApplicationContext
     */
    private Context appContext = null;

    /**
     * 获取上下文
     *
     * @return ApplicationContext
     */
    public Context getContext() {
        return appContext;
    }

    /**
     * 初始化JPush 在自定义Application的onCreate()方法中必须调用
     *
     * @param appContext 上下文
     * @param isDebug    是否开启日志,发布时请关闭日志
     * @return
     */
    public void init(Context appContext, boolean isDebug) {
        // 初始化上下文
        this.appContext = appContext;
        // 设置开启日志,发布时请关闭日志
        JPushInterface.setDebugMode(isDebug);
        // 初始化 JPush
        JPushInterface.init(appContext);
    }

    /**
     * 注册自定义消息接收者 在onCreate()中调用
     *
     * @param mContext
     * @param mJPushMessageReceiver
     */
    public void registerMessageReceiver(Context mContext, JPushMessageReceiver mJPushMessageReceiver) {
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(JPushConstant.MESSAGE_RECEIVED_ACTION);
        mContext.registerReceiver(mJPushMessageReceiver, filter);
    }

    /**
     * 解除注册自定义消息接收者 在onDestroy()中调用
     *
     * @param mContext
     * @param mJPushMessageReceiver
     */
    public void unregisterReceiver(Context mContext, JPushMessageReceiver mJPushMessageReceiver) {
        mContext.unregisterReceiver(mJPushMessageReceiver);
    }

    /**
     * 设置是否接收推送消息
     *
     * @param isAllowedToPush
     */
    public void setIsAllowedToPush(boolean isAllowedToPush) {
        if (appContext == null) {
            return;
        }
        if (isAllowedToPush) {
            // 接收推送消息
            if (JPushInterface.isPushStopped(appContext)) {
                JPushInterface.resumePush(appContext);
            }
        } else {
            // 禁止接收推送消息
            if (!JPushInterface.isPushStopped(appContext)) {
                JPushInterface.stopPush(appContext);
            }
        }
    }

    /**
     * 获取是否接收推送消息
     *
     * @return
     */
    public boolean getIsAllowedToPush() {
        if (appContext == null) {
            return false;
        }
        return !JPushInterface.isPushStopped(appContext);
    }

    /**
     * 获取RegistrationID
     * <p>
     * 集成了 JPush SDK 的应用程序在第一次成功注册到 JPush 服务器时，JPush 服务器会给客户端返回一个唯一的该设备的标识 -
     * RegistrationID。JPush SDK 会以广播的形式发送 RegistrationID 到应用程序。
     *
     * @return
     */
    public String getRegistrationID() {
        if (appContext == null) {
            return null;
        }
        String rid = JPushInterface.getRegistrationID(appContext);
        return rid;
    }

    /**
     * 获取AppKey
     *
     * @return
     */
    public String getAppKey() {
        if (appContext == null) {
            return null;
        }
        Bundle metaData = null;
        String appKey = null;
        try {
            ApplicationInfo ai = appContext.getPackageManager().getApplicationInfo(appContext.getPackageName(),
                    PackageManager.GET_META_DATA);
            if (null != ai)
                metaData = ai.metaData;
            if (null != metaData) {
                appKey = metaData.getString(JPushConstant.KEY_APP_KEY);
                if ((null == appKey) || appKey.length() != 24) {
                    appKey = null;
                }
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return appKey;
    }

    /**
     * 获取DeviceId
     *
     * @return
     */
    public String getDeviceId() {
        if (appContext == null) {
            return null;
        }
        String deviceId = JPushInterface.getUdid(appContext);
        return deviceId;
    }

    /**
     * 获取Imei
     *
     * @return
     */
    public String getImei() {
        if (appContext == null) {
            return null;
        }
        String imei = null;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) appContext
                    .getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return imei;
    }

    /**
     * 在Handler中设置别名和标签
     */
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case JPushConstant.MSG_SET_ALIAS:
                    Log.d(TAG, "在Handler中设置别名");
                    if (appContext != null)
                        JPushInterface.setAliasAndTags(appContext, (String) msg.obj, null, mAliasCallback);
                    break;
                case JPushConstant.MSG_SET_TAGS:
                    Log.d(TAG, "在Handler中设置标签");
                    if (appContext != null)
                        JPushInterface.setAliasAndTags(appContext, null, (Set<String>) msg.obj, mTagsCallback);
                    break;
                case JPushConstant.MSG_SET_ALIAS_SUCCESS:
                    // 设置别名成功
                    // String alias = (String) msg.obj;
                    break;
                case JPushConstant.MSG_SET_TAGS_SUCCESS:
                    // 设置标签成功
                    // Set<String> tagSet = (Set<String>) msg.obj;
                    break;
            }
        }
    };

    /**
     * 设置别名
     * <p>
     * null 此次调用不设置此值。（注：不是指的字符串"null"） "" （空字符串）表示取消之前的设置 每次调用设置有效的别名，覆盖之前的设置
     * 有效的别名组成：字母（区分大小写）、数字、下划线、汉字、特殊字符(v2.1.6支持)@!#$&*+=.|￥。
     * 限制：alias命名长度限制为40字节。（判断长度需采用UTF-8编码）
     *
     * @param alias
     */
    public void setAlias(String alias) {
        //检查alias 的有效性
        if (!isValidTagAndAlias(alias)) {
            Log.e(TAG, "Alias格式不对");
            return;
        }
        // 设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(JPushConstant.MSG_SET_ALIAS, alias));
    }

    /**
     * 设置标签
     * <p>
     * null 此次调用不设置此值。（注：不是指的字符串"null"） 空数组或列表表示取消之前的设置
     * 每次调用至少设置一个tag，覆盖之前的设置，不是新增
     * 有效的标签组成：字母（区分大小写）、数字、下划线、汉字、特殊字符(v2.1.6支持)@!#$&*+=.|￥。 限制：每个tag命名长度限制为
     * 40字节，最多支持设置 1000 个 tag，但总长度不得超过7K字节。（判断长度需采用UTF-8编码）
     *
     * @param tag 多个标签用英文逗号分隔
     */
    public void setTag(String tag) {
        // 检查 tag 的有效性
        // ","隔开的多个 转换成 Set
        String[] sArray = tag.split(",");
        Set<String> tagSet = new LinkedHashSet<String>();
        for (String sTagItme : sArray) {
            if (!isValidTagAndAlias(sTagItme)) {
                Log.e(TAG, "Tag格式不对");
                return;
            }
            tagSet.add(sTagItme);
        }
        // 设置Tag
        mHandler.sendMessage(mHandler.obtainMessage(JPushConstant.MSG_SET_TAGS, tagSet));
    }

    /**
     * 校验Tag Alias 只能是数字,英文字母和中文
     *
     * @param s
     * @return
     */
    private boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * 设置别名的回调
     */
    public final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "设置别名成功";
                    Log.i(TAG, logs);
                    mHandler.sendMessage(mHandler.obtainMessage(JPushConstant.MSG_SET_ALIAS_SUCCESS, alias));
                    break;
                case 6002:
                    logs = "设置别名或标签失败，60秒后重试";
                    Log.i(TAG, logs);
                    if (appContext != null && isConnected(appContext)) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(JPushConstant.MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Log.i(TAG, "没有联网");
                    }
                    break;
                default:
                    logs = "设置失败，错误码 = " + code;
                    Log.e(TAG, logs);
            }
        }

    };

    /**
     * 设置标签的回调
     */
    public final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "设置标签成功";
                    Log.i(TAG, logs);
                    mHandler.sendMessage(mHandler.obtainMessage(JPushConstant.MSG_SET_TAGS_SUCCESS, tags));
                    break;
                case 6002:
                    logs = "设置别名或标签失败，60秒后重试";
                    Log.i(TAG, logs);
                    if (appContext != null && isConnected(appContext)) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(JPushConstant.MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        Log.i(TAG, "没有联网");
                    }
                    break;
                default:
                    logs = "设置失败，错误码 = " + code;
                    Log.e(TAG, logs);
            }
        }

    };

    /**
     * 是否联网
     *
     * @param context
     * @return
     */
    private boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    /**
     * 清除指定通知
     *
     * @param notificationId 此 notificationId 来源于intent参数
     *                       JPushInterface.EXTRA_NOTIFICATION_ID
     */
    public void clearNotificationById(int notificationId) {
        if (appContext == null) {
            return;
        }
        JPushInterface.clearNotificationById(appContext, notificationId);
    }

    /**
     * 清除全部通知
     */
    public void clearAllNotifications() {
        if (appContext == null) {
            return;
        }
        JPushInterface.clearAllNotifications(appContext);
    }

    /**
     * 设置接收推送时间
     *
     * @param weekDays  0表示星期天，1表示星期一，以此类推。 （7天制，Set集合里面的int范围为0到6） Sdk1.2.9 –
     *                  新功能:set的值为null,则任何时间都可以收到消息和通知，set的size为0，则表示任何时间都收不到消息和通知
     * @param startHour 允许推送的开始时间 （24小时制：startHour的范围为0到23）
     * @param endHour   允许推送的结束时间 （24小时制：endHour的范围为0到23）
     */
    public void setPushTime(Set<Integer> weekDays, int startHour, int endHour) {
        if (appContext == null) {
            return;
        }
        if (weekDays.size() > 7) {
            return;
        }
        if (startHour < 0 || startHour > 23 || endHour < 0 || endHour > 23) {
            return;
        }
        JPushInterface.setPushTime(appContext, weekDays, startHour, endHour);
        // Set<Integer> days = new HashSet<Integer>();
        // days.add(1);
        // days.add(2);
        // days.add(3);
        // days.add(4);
        // days.add(5);
        // JPushInterface.setPushTime(getApplicationContext(), days, 10, 23);
        // 此代码表示周一到周五、上午10点到晚上23点，都可以推送。
    }

    /**
     * 设置通知静默时间(默认情况下用户在收到推送通知时，客户端可能会有震动，响铃等提示。但用户在睡觉、开会等时间点希望为 "免打扰"
     * 模式，也是静音时段的概念.如果在该时间段内收到消息，则：不会有铃声和震动)
     *
     * @param startHour   开始小时（24小时制，范围：0~23 ）
     * @param startMinute 开始分钟（范围：0~59 ）
     * @param endHour     结束小时（24小时制，范围：0~23 ）
     * @param endMinute   结束分钟（范围：0~59 ）
     */
    public void setSilenceTime(int startHour, int startMinute, int endHour, int endMinute) {
        if (appContext == null) {
            return;
        }
        if (startHour < 0 || startHour > 23 || startMinute < 0 || startMinute > 59 || endHour < 0 || endHour > 23
                || endMinute < 0 || endMinute > 59) {
            return;
        }
        JPushInterface.setSilenceTime(appContext, startHour, startMinute, endHour, endMinute);
        // JPushInterface.setSilenceTime(getApplicationContext(), 22, 30, 8,30);
        // 此代码表示晚上10：30点到第二天早上8：30点为静音时段。
    }

    /**
     * 发送本地通知
     *
     * @param notificationId 本地通知的ID
     * @param builderId      本地通知样式
     * @param title          本地通知的title
     * @param content        本地通知的content
     * @param json           额外的数据信息extras为json字符串
     * @param time           本地通知触发时间(多少毫秒后发送)
     */
    public void addLocalNotification(long notificationId, long builderId, String title, String content,
                                     Map<String, Object> map, long time) {
        if (appContext == null) {
            return;
        }
        JPushLocalNotification notification = new JPushLocalNotification();
        // 设置本地通知的ID
        notification.setNotificationId(notificationId);
        // 设置本地通知样式
        notification.setBuilderId(builderId);
        // 设置本地通知的title
        notification.setTitle(title);
        // 设置本地通知的content
        notification.setContent(content);
        // 设置额外的数据信息extras为json字符串
        notification.setExtras(new JSONObject(map).toString());
        // 设置本地通知触发时间
        notification.setBroadcastTime(System.currentTimeMillis() + time);
        // 发送本地通知
        JPushInterface.addLocalNotification(appContext, notification);
    }

    /**
     * 发送本地通知
     *
     * @param notificationId 本地通知的ID
     * @param builderId      本地通知样式
     * @param title          本地通知的title
     * @param content        本地通知的content
     * @param json           额外的数据信息extras为json字符串
     * @param date           本地通知触发时间
     */
    public void addLocalNotification(long notificationId, long builderId, String title, String content,
                                     Map<String, Object> map, Date date) {
        if (appContext == null) {
            return;
        }
        JPushLocalNotification notification = new JPushLocalNotification();
        // 设置本地通知的ID
        notification.setNotificationId(notificationId);
        // 设置本地通知样式
        notification.setBuilderId(builderId);
        // 设置本地通知的title
        notification.setTitle(title);
        // 设置本地通知的content
        notification.setContent(content);
        // 设置额外的数据信息extras为json字符串
        notification.setExtras(new JSONObject(map).toString());
        // 设置本地通知触发时间
        notification.setBroadcastTime(date);
        // 发送本地通知
        JPushInterface.addLocalNotification(appContext, notification);
    }

    /**
     * 发送本地通知
     *
     * @param notificationId 本地通知的ID
     * @param builderId      本地通知样式
     * @param title          本地通知的title
     * @param content        本地通知的content
     * @param map            额外的数据信息extras为json字符串
     * @param year           本地通知触发时间-年
     * @param month          本地通知触发时间-月
     * @param day            本地通知触发时间-日
     * @param hour           本地通知触发时间-时
     * @param minute         本地通知触发时间-分
     * @param second         本地通知触发时间-秒
     */
    public void addLocalNotification(long notificationId, long builderId, String title, String content,
                                     Map<String, Object> map, int year, int month, int day, int hour, int minute, int second) {
        if (appContext == null) {
            return;
        }
        JPushLocalNotification notification = new JPushLocalNotification();
        // 设置本地通知的ID
        notification.setNotificationId(notificationId);
        // 设置本地通知样式
        notification.setBuilderId(builderId);
        // 设置本地通知的title
        notification.setTitle(title);
        // 设置本地通知的content
        notification.setContent(content);
        // 设置额外的数据信息extras为json字符串
        notification.setExtras(new JSONObject(map).toString());
        // 设置本地通知触发时间
        notification.setBroadcastTime(year, month, day, hour, minute, second);
        // 发送本地通知
        JPushInterface.addLocalNotification(appContext, notification);
    }

    /**
     * 移除指定的本地通知
     *
     * @param notificationId
     */
    public void removeLocalNotification(long notificationId) {
        if (appContext == null) {
            return;
        }
        JPushInterface.removeLocalNotification(appContext, notificationId);
    }

    /**
     * 移除所有的本地通知
     */
    public void clearLocalNotifications() {
        if (appContext == null) {
            return;
        }
        JPushInterface.clearLocalNotifications(appContext);
    }

    /**
     * 设置保留最近通知条数(默认5条) 仅对通知有效。 所谓保留最近的，意思是，如果有新的通知到达，之前列表里最老的那条会被移除。
     * 例如，设置为保留最近5条通知。假设已经有 5 条显示在通知栏，当第 6 条到达时，第 1 条将会被移除。
     *
     * @param maxNum
     */
    public void setLatestNotificationNumber(int maxNum) {
        if (appContext == null) {
            return;
        }
        JPushInterface.setLatestNotificationNumber(appContext, maxNum);
    }
}
