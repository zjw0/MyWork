package cn.appoa.mywork.base;

import android.content.Intent;
import android.support.v4.content.ContextCompat;

import com.wangzhen.statusbar.DarkStatusBar;

import cn.appoa.aframework.activity.AfImageActivity;
import cn.appoa.aframework.presenter.BasePresenter;
import cn.appoa.aframework.utils.AtyUtils;
import cn.appoa.aframework.view.ILoginView;
import cn.appoa.mywork.R;
import cn.appoa.mywork.net.API;
import cn.appoa.mywork.ui.LoginActivity;


public abstract class BaseImageActivity<P extends BasePresenter> extends AfImageActivity<P>
        implements ILoginView {

    @Override
    public void initView() {
        DarkStatusBar.get().fitDark(this);//深色模式
        if (titlebar != null) {
            titlebar.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorDefaultTitlebarBg));
            AtyUtils.setPaddingTop(mActivity, titlebar);
        }
    }

    @Override
    public boolean enableSliding() {
        return true;
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
}
