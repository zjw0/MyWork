package cn.appoa.mywork.view;

import java.util.List;

import cn.appoa.aframework.view.IBaseView;
import cn.appoa.mywork.bean.UserInfo;


public interface LoginView extends IBaseView {

    void loginSuccess(UserInfo user);

    void thirdLoginSuccess(int type, String open_id, String nickName, String headImg, List<UserInfo> data);
}
