package cn.appoa.mywork.base;

import android.content.Intent;

import cn.appoa.aframework.fragment.AfFragment;
import cn.appoa.aframework.presenter.BasePresenter;
import cn.appoa.aframework.view.ILoginView;
import cn.appoa.mywork.net.API;
import cn.appoa.mywork.ui.LoginActivity;


public abstract class BaseFragment<P extends BasePresenter> extends AfFragment<P>
        implements ILoginView {

    @Override
    public boolean isLogin() {
        return API.isLogin();
    }

    @Override
    public void toLoginActivity() {
        startActivityForResult(new Intent(mActivity, LoginActivity.class), 998);
    }

    @Override
    public void toLoginSuccess(Intent data) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999 && resultCode == -1 && data != null) {
            toLoginSuccess(data);
        }
    }
}
