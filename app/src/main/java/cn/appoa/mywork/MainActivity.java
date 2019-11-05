package cn.appoa.mywork;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.appoa.aframework.utils.AtyUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AtyUtils.showShort(this,"集成成功",true);

    }
}
