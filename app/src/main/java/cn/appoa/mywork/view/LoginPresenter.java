package cn.appoa.mywork.view;

import java.util.List;
import java.util.Map;

import cn.appoa.aframework.listener.VolleyDatasListener;
import cn.appoa.aframework.listener.VolleyErrorListener;
import cn.appoa.aframework.presenter.BasePresenter;
import cn.appoa.aframework.view.IBaseView;
import cn.appoa.mywork.net.API;
import zm.http.volley.ZmVolley;
import zm.http.volley.request.ZmStringRequest;


public class LoginPresenter extends BasePresenter {

    protected LoginView mLoginView;

    @Override
    public void onAttach(IBaseView view) {
        //super.onAttach(view);
        if (view instanceof LoginView) {
            mLoginView = (LoginView) view;
        }
    }

    @Override
    public void onDetach() {
        //super.onDetach();
        if (mLoginView != null) {
            mLoginView = null;
        }
    }

    /**
     * 登录
     *
     * @param phone
     * @param pwd
     */
//    public void loginPwd(final String phone, final String pwd) {
//        if (mLoginView == null)
//            return;
//        mLoginView.showLoading("正在登录...");
//        Map<String, String> params = API.getParams("loginPhone", phone);
//        params.put("password", pwd);
//        ZmVolley.request(new ZmStringRequest(API.loginKylUser, params,
//                new VolleyDatasListener<UserInfo>(mLoginView, "登录", 2, UserInfo.class) {
//
//                    @Override
//                    public void onDatasResponse(List<UserInfo> datas) {
//                        if (datas != null && datas.size() > 0) {
//                            mLoginView.loginSuccess(datas.get(0));
//                        }
//                    }
//
//                    @Override
//                    public void onResponse(String response) {
//                        super.onResponse(response);
//                    }
//                }, new VolleyErrorListener(mLoginView, "登录", "登录失败，请稍后再试！")), mLoginView.getRequestTag());
//    }

    /**
     * 微信登录
     *
     * @param openId 微信openId
     */
//    public void thirdLogin(final int type, final String openId, final String nickName, final String headImg) {
//        if (mLoginView == null)
//            return;
//        mLoginView.showLoading("正在微信登录...");
//        Map<String, String> params = API.getParams("openId", openId);
//        ZmVolley.request(new ZmStringRequest(API.doLoginWx, params,
//                new VolleyDatasListener<UserInfo>(mLoginView, "微信登录", 2, UserInfo.class) {
//
//                    @Override
//                    public void onDatasResponse(List<UserInfo> datas) {
//                        mLoginView.thirdLoginSuccess(type, openId, nickName, headImg, datas);
//                    }
//
//                    @Override
//                    public void onResponse(String response) {
//                        super.onResponse(response);
//                    }
//                }, new VolleyErrorListener(mLoginView, "微信登录", "微信登录失败，请稍后再试！")), mLoginView.getRequestTag());
//    }

}
