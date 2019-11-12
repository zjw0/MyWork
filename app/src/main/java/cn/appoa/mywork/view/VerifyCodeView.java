package cn.appoa.mywork.view;

import java.util.List;

import cn.appoa.aframework.view.IBaseView;
import cn.appoa.mywork.bean.VerifyCode;


public interface VerifyCodeView extends IBaseView {

    void setVerifyCode(List<VerifyCode> verifyCode);
}
