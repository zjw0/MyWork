package cn.appoa.mywork;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import cn.appoa.aframework.bean.AppVersion;
import cn.appoa.mywork.base.BaseActivity;
import cn.appoa.mywork.presenter.MainPresenter;
import cn.appoa.mywork.view.MainView;

public class MainActivity extends BaseActivity<MainPresenter> implements MainView {

    @BindView(R.id.tv_main)
    public TextView tv_main;


    @Override
    public void initContent(Bundle savedInstanceState) {
        setContent(R.layout.activity_main);
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {
        String[] permissions = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
        };
        requestPermissions(permissions, null);


    }

    @Override
    public void setVersion(AppVersion data) {

    }
}
