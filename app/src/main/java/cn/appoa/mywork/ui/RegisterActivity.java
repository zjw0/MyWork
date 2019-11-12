package cn.appoa.mywork.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import cn.appoa.aframework.listener.EditOnCheckedChangeListener;
import cn.appoa.aframework.titlebar.BaseTitlebar;
import cn.appoa.aframework.titlebar.DefaultTitlebar;
import cn.appoa.aframework.utils.AtyUtils;
import cn.appoa.mywork.R;
import cn.appoa.mywork.base.VerifyCodeActivity;
import cn.appoa.mywork.bean.VerifyCode;
import cn.appoa.mywork.presenter.RegisterPresenter;
import cn.appoa.mywork.view.RegisterView;


/**
 * 注册
 */
public class RegisterActivity extends VerifyCodeActivity<RegisterPresenter>
        implements RegisterView, View.OnClickListener {


    @Override
    public BaseTitlebar initTitlebar() {
        return new DefaultTitlebar.Builder(this)
                .setBackImage(R.drawable.black_background)
                .setLineHeight(0f)
                .create();
    }

    @Override
    public void initContent(Bundle savedInstanceState) {
        setContent(R.layout.activity_register);
    }

    private EditText et_login_pwd;
    private CheckBox cb_login_pwd;
    private EditText et_login_pwd_again;
    private CheckBox cb_login_pwd_again;
    private TextView tv_login_register;
    private TextView et_invite_code;
    private TextView tv_login_ok;

    @Override
    public void initView() {
        super.initView();
        et_login_pwd = (EditText) findViewById(R.id.et_login_pwd);
        cb_login_pwd = (CheckBox) findViewById(R.id.cb_login_pwd);
        et_login_pwd_again = (EditText) findViewById(R.id.et_login_pwd_again);
        cb_login_pwd_again = (CheckBox) findViewById(R.id.cb_login_pwd_again);
        tv_login_register = (TextView) findViewById(R.id.tv_login_register);
        et_invite_code = (TextView) findViewById(R.id.et_invite_code);
        tv_login_ok = (TextView) findViewById(R.id.tv_login_ok);

        cb_login_pwd.setOnCheckedChangeListener(new EditOnCheckedChangeListener(et_login_pwd));
        cb_login_pwd_again.setOnCheckedChangeListener(new EditOnCheckedChangeListener(et_login_pwd_again));
        tv_login_register.setOnClickListener(this);
        tv_login_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login_register://注册
                confirmVerifyCode();
                break;
            case R.id.tv_login_ok://去登陆
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public RegisterPresenter initPresenter() {
        return new RegisterPresenter();
    }

    @Override
    public void onAttachView() {
        mPresenter.onAttach(this);
    }

    @Override
    public void setVerifyCode(List<VerifyCode> verifyCode) {
        isGetCode = true;
    }

    @Override
    protected String initVerifyCodeType() {
        return "0";//注册
    }

    @Override
    protected void confirmVerifyCodeSuccess() {
        if (AtyUtils.isTextEmpty(et_login_pwd)) {
            AtyUtils.showShort(mActivity, "请输入密码", false);
            return;
        }
        if (AtyUtils.isTextEmpty(et_login_pwd_again)) {
            AtyUtils.showShort(mActivity, "请再次输入密码", false);
            return;
        }
        if (!AtyUtils.isSameText(et_login_pwd, et_login_pwd_again)) {
            AtyUtils.showShort(mActivity, "两次输入的密码不一致", false);
            return;
        }
        mPresenter.register(AtyUtils.getText(et_login_phone),
                AtyUtils.getText(et_login_pwd),
                AtyUtils.getText(et_login_code),
                AtyUtils.getText(et_invite_code));
    }

    @Override
    public void registerSuccess(String phone, String pwd) {
        setResult(RESULT_OK, new Intent()
                .putExtra("phone", phone)
                .putExtra("pwd", pwd));
        finish();
    }
}
