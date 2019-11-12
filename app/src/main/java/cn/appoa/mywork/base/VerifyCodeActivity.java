package cn.appoa.mywork.base;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import cn.appoa.aframework.utils.AtyUtils;
import cn.appoa.mywork.R;
import cn.appoa.mywork.presenter.VerifyCodePresenter;


/**
 * 发验证码的Activity
 *
 * @param <P>
 */
public abstract class VerifyCodeActivity<P extends VerifyCodePresenter> extends BaseActivity<P> {

    protected EditText et_login_phone;
    protected EditText et_login_code;
    protected TextView tv_login_code;

    @Override
    public void initView() {
        super.initView();
        et_login_phone = (EditText) findViewById(R.id.et_login_phone);
        et_login_code = (EditText) findViewById(R.id.et_login_code);
        tv_login_code = (TextView) findViewById(R.id.tv_login_code);
        tv_login_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVerifyCode();
            }
        });
    }

    /**
     * 电话
     */
    protected String phone;

    /**
     * 是否获取到验证码
     */
    protected boolean isGetCode;

    /**
     * 获取验证码
     */
    protected void getVerifyCode() {
        if (AtyUtils.isTextEmpty(et_login_phone)) {
            AtyUtils.showShort(mActivity, "请先输入手机号", false);
            return;
        }
        if (!AtyUtils.isMobile(AtyUtils.getText(et_login_phone))) {
            AtyUtils.showShort(mActivity, "请输入正确的手机号", false);
            return;
        }
        if (TextUtils.isEmpty(phone))
            phone = AtyUtils.getText(et_login_phone);
        countDown(tv_login_code);
        if (mPresenter != null) {
            //mPresenter.getVerifyCode(initVerifyCodeType(), phone);
        }
    }

    /**
     * 验证码类型:（0注册1忘记密码2修改手机号3绑定手机号4设置/重置支付密码）
     */
    protected abstract String initVerifyCodeType();

    /**
     * 倒计时60秒
     */
    protected int time;

    /**
     * 倒计时
     */
    protected void countDown(final TextView textView) {
        if ("获取验证码".equals(textView.getText().toString().trim())) {
            time = 60;
            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            if (time <= 0) {
                                textView.setEnabled(true);
                                textView.setText("获取验证码");
                                textView.setTextColor(ContextCompat.getColor(mActivity, R.color.colorTheme));
//                                textView.setBackgroundResource(R.drawable.shape_solid_theme_50dp);
                                timer.cancel();
                                return;
                            } else {
                                textView.setEnabled(false);
                                textView.setText(Integer.toString(time--) + "s后重发");
                                textView.setTextColor(ContextCompat.getColor(mActivity, R.color.colorTheme));
//                                textView.setBackgroundResource(R.drawable.shape_solid_cccccc_50dp);
                            }
                        }
                    });
                }
            }, 1000, 1000);
        }
    }

    /**
     * 校验
     */
    protected void confirmVerifyCode() {
        if (!isGetCode) {
            AtyUtils.showShort(mActivity, "请先获取验证码", false);
            return;
        }
        if (AtyUtils.isTextEmpty(et_login_phone)) {
            AtyUtils.showShort(mActivity, "请输入手机号", false);
            return;
        }
        if (!AtyUtils.isMobile(AtyUtils.getText(et_login_phone))) {
            AtyUtils.showShort(mActivity, "请输入正确的手机号", false);
            return;
        }
        if (!TextUtils.equals(phone, AtyUtils.getText(et_login_phone))) {
            AtyUtils.showShort(mActivity, "更换手机号需重新获取验证码", false);
            return;
        }
        if (AtyUtils.isTextEmpty(et_login_code)) {
            AtyUtils.showShort(mActivity, "请输入验证码", false);
            return;
        }
//        if (!TextUtils.equals(code, AtyUtils.getText(et_login_code))) {
//            AtyUtils.showShort(mActivity, "请输入正确的验证码", false);
//            return;
//        }
        confirmVerifyCodeSuccess();
    }

    /**
     * 提交
     */
    protected abstract void confirmVerifyCodeSuccess();

}
