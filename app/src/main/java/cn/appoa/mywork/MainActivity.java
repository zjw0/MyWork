package cn.appoa.mywork;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


import butterknife.BindView;
import cn.appoa.aframework.bean.AppVersion;
import cn.appoa.aframework.utils.AtyUtils;
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

    }

    @Override
    public void setVersion(AppVersion data) {

    }
}
