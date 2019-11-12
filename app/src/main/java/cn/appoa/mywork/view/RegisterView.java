package cn.appoa.mywork.view;


public interface RegisterView extends VerifyCodeView {

    void registerSuccess(String phone, String pwd);
}
