package cn.appoa.mywork;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


import butterknife.BindView;
import cn.appoa.aframework.utils.AtyUtils;
import cn.appoa.mywork.base.BaseActivity;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_main)
    public TextView tv_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AtyUtils.showShort(this,"集成成功",true);

    }


//    @Override
//    public void initContent(Bundle savedInstanceState) {
//        setContent(R.layout.activity_main);
//        AtyUtils.showShort(this,"集成成功",true);
//    }

//    @Override
//    public void initData() {
//
//    }

}
