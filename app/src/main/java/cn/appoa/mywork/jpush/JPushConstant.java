package cn.appoa.mywork.jpush;

/**
 * 极光推送常量类
 */
public class JPushConstant {

    /**
     * 标题的键
     */
    public static final String KEY_TITLE = "title";

    /**
     * 消息的键
     */
    public static final String KEY_MESSAGE = "message";

    /**
     * 扩展信息的键
     */
    public static final String KEY_EXTRAS = "extras";

    /**
     * 接收自定义消息的Action
     */
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";

    /**
     * appKey的键
     */
    public static final String KEY_APP_KEY = "JPUSH_APPKEY";

    /**
     * 设置别名
     */
    public static final int MSG_SET_ALIAS = 1001;

    /**
     * 设置别名成功
     */
    public static final int MSG_SET_ALIAS_SUCCESS = 1003;

    /**
     * 设置标签
     */
    public static final int MSG_SET_TAGS = 1002;

    /**
     * 设置标签成功
     */
    public static final int MSG_SET_TAGS_SUCCESS = 1004;

}
