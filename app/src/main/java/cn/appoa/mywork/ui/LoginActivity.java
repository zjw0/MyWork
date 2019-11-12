package cn.appoa.mywork.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import cn.appoa.aframework.listener.EditOnCheckedChangeListener;
import cn.appoa.aframework.titlebar.BaseTitlebar;
import cn.appoa.aframework.titlebar.DefaultTitlebar;
import cn.appoa.aframework.utils.AtyUtils;
import cn.appoa.aframework.utils.SpUtils;
import cn.appoa.mywork.R;
import cn.appoa.mywork.base.BaseActivity;
import cn.appoa.mywork.constant.Constant;
import cn.appoa.mywork.presenter.LoginPresenter;
import cn.appoa.mywork.utils.MySPUtils;
import cn.appoa.mywork.view.LoginView;
import okhttp3.internal.platform.Platform;
import zm.bus.event.BusProvider;

public class LoginActivity extends BaseActivity<LoginPresenter>
        implements LoginView, View.OnClickListener, TextView.OnEditorActionListener {

    @Override
    public BaseTitlebar initTitlebar() {
        return new DefaultTitlebar.Builder(mActivity)
                .setBackImage(R.drawable.back_black)
                .setLineHeight(0f)
                .create();
    }

    @Override
    public void initContent(Bundle savedInstanceState) {
        setContent(R.layout.activity_login);
    }

    private EditText et_login_phone;
    private EditText et_login_pwd;
    private CheckBox cb_login_pwd;
    private TextView tv_login_register;
    private TextView tv_save_pwd;
    private TextView tv_login_find_pwd;
    private TextView tv_login_ok;
    private ImageView iv_wechat;

    @Override
    public void initView() {
        super.initView();
        et_login_phone = (EditText) findViewById(R.id.et_login_phone);
        et_login_pwd = (EditText) findViewById(R.id.et_login_pwd);
        cb_login_pwd = (CheckBox) findViewById(R.id.cb_login_pwd);
        tv_login_register = (TextView) findViewById(R.id.tv_login_register);
        tv_save_pwd = (TextView) findViewById(R.id.tv_save_pwd);
        tv_login_find_pwd = (TextView) findViewById(R.id.tv_login_find_pwd);
        tv_login_ok = (TextView) findViewById(R.id.tv_login_ok);
        iv_wechat = (ImageView) findViewById(R.id.iv_wechat);

        et_login_pwd.setOnEditorActionListener(this);
        cb_login_pwd.setOnCheckedChangeListener(new EditOnCheckedChangeListener(et_login_pwd));
        tv_login_register.setOnClickListener(this);
        tv_save_pwd.setOnClickListener(this);
        tv_login_find_pwd.setOnClickListener(this);
        tv_login_ok.setOnClickListener(this);
        iv_wechat.setOnClickListener(this);
    }

    @Override
    public LoginPresenter initPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void onAttachView() {
        mPresenter.onAttach(this);
    }

    @Override
    public void initData() {
        flag = (Boolean) MySPUtils.getData(mActivity, Constant.FLAG, false);
        tv_save_pwd.setCompoundDrawablesWithIntrinsicBounds(flag ? R.drawable.icon_image_select : R.drawable.icon_image_un_select, 0, 0, 0);
        et_login_phone.setText((String) MySPUtils.getData(mActivity, Constant.USER_PHONE, ""));
        et_login_pwd.setText((String) MySPUtils.getData(mActivity, Constant.USER_PWD, ""));
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == R.id.et_login_pwd && actionId == EditorInfo.IME_ACTION_GO) {
            onClick(tv_login_ok);
            return true;
        }
        return false;
    }

    private boolean flag;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login_ok://登录
                if (AtyUtils.isTextEmpty(et_login_phone)) {
                    AtyUtils.showShort(mActivity, "输入你的手机号", false);
                    return;
                }
                if (!AtyUtils.isMobile(AtyUtils.getText(et_login_phone))) {
                    AtyUtils.showShort(mActivity, "请输入正确的手机号", false);
                    return;
                }
                if (AtyUtils.isTextEmpty(et_login_pwd)) {
                    AtyUtils.showShort(mActivity, "输入您的密码", false);
                    return;
                }
                mPresenter.loginPwd(AtyUtils.getText(et_login_phone), AtyUtils.getText(et_login_pwd));
                break;
            case R.id.tv_login_register://注册
                startActivityForResult(new Intent(mActivity, RegisterActivity.class)
                        .putExtra("type", 1), 1);
                break;
            case R.id.tv_login_find_pwd://忘记密码
                startActivityForResult(new Intent(mActivity, PwdResetActivity.class)
                        .putExtra("type", 2), 2);
                break;
            case R.id.tv_save_pwd://记住密码
                flag = !flag;
                tv_save_pwd.setCompoundDrawablesWithIntrinsicBounds(flag ? R.drawable.icon_image_select : R.drawable.icon_image_un_select, 0, 0, 0);
                break;
            case R.id.iv_wechat://微信登录
                showLoading("正在登录微信...");
                ShareSdkUtils.thirdLogin(1, this);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        switch (requestCode) {
            case 1://注册
                et_login_phone.setText(data.getStringExtra("phone"));
                et_login_pwd.setText(data.getStringExtra("pwd"));
                mPresenter.loginPwd(data.getStringExtra("phone"),
                        data.getStringExtra("pwd"));
                break;
            case 2://绑定手机号
                et_login_phone.setText(data.getStringExtra("phone"));
                et_login_pwd.setText(data.getStringExtra("pwd"));
                onClick(tv_login_ok);
                break;
            default:
                break;
        }
    }

    @Override
    public void loginSuccess(UserInfo user) {
        if (user != null) {
            user.saveUserInfo(mActivity);
            AtyUtils.showShort(mActivity, "登录成功", false);
            SpUtils.putData(mActivity, Constant.IS_LOGIN, true);
            MySPUtils.putData(mActivity, Constant.FLAG, flag);
            MySPUtils.putData(mActivity, Constant.USER_PHONE, flag ? AtyUtils.getText(et_login_phone) : "");
            MySPUtils.putData(mActivity, Constant.USER_PWD, flag ? AtyUtils.getText(et_login_pwd) : "");
            BusProvider.getInstance().post(new LoginEvent(1));
            BusProvider.getInstance().post(new PartnerDriverEvent(1));
            setResult(RESULT_OK, new Intent().putExtra("user", user));
            finish();
        }
    }

    @Override
    public void onComplete(final Platform platform, int action, HashMap<String, Object> hashMap) {
        dismissLoading();
        if (action == Platform.ACTION_USER_INFOR)
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissLoading();
                    AtyUtils.showShort(mActivity, "授权成功", false);
                    toThirdLogin(platform);
                }
            });
    }

    @Override
    public void onError(final Platform platform, int action, Throwable throwable) {
        dismissLoading();
        if (action == Platform.ACTION_USER_INFOR)
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissLoading();
                    if (platform != null && platform.getDb() != null
                            && !TextUtils.isEmpty(platform.getDb().getUserId())) {
                        //部分时候失败中也有用户资料，此时认为授权成功
                        toThirdLogin(platform);
                    } else {
                        AtyUtils.showShort(mActivity, "授权失败", false);
                    }
                }
            });
    }

    @Override
    public void onCancel(Platform platform, int action) {
        dismissLoading();
        if (action == Platform.ACTION_USER_INFOR)
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissLoading();
                    AtyUtils.showShort(mActivity, "取消授权", false);
                }
            });
    }

    /**
     * 三方授权成功
     *
     * @param platform
     */
    private void toThirdLogin(Platform platform) {
        if (platform != null) {
            PlatformDb platDB = platform.getDb();
            String openid = platDB.getUserId();//用户openid
            String nickName = platDB.getUserName();//用户昵称
            String userPic = platDB.getUserIcon();//用户头像
            String userGender = platDB.getUserGender();//用户性别
            AtyUtils.i("第三方登录", openid);
            if (TextUtils.equals(Wechat.NAME, platform.getName())) {
                mPresenter.thirdLogin(1, openid, nickName, userPic);
            }
        }
    }

    @Override
    public void thirdLoginSuccess(int type, String open_id, String nickName, String userPic, List<UserInfo> user) {
        if (user == null || user.size() == 0 || TextUtils.isEmpty(user.get(0).password)) {
            // 微信登录
            startActivityForResult(new Intent(mActivity, PwdResetActivity.class)
                    .putExtra("type", 4)
                    .putExtra("openId", open_id)
                    .putExtra("nickName", nickName)
                    .putExtra("userPic", userPic), 2);
        } else {
            loginSuccess(user.get(0));
        }
    }
}
