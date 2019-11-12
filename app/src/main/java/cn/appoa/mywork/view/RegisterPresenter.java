package cn.appoa.mywork.view;

import java.util.Map;

import cn.appoa.aframework.listener.VolleyErrorListener;
import cn.appoa.aframework.listener.VolleySuccessListener;
import cn.appoa.aframework.view.IBaseView;
import cn.appoa.keyoule.net.API;
import zm.http.volley.ZmVolley;
import zm.http.volley.request.ZmStringRequest;

public class RegisterPresenter extends VerifyCodePresenter {

    protected RegisterView mRegisterView;

    @Override
    public void onAttach(IBaseView view) {
        super.onAttach(view);
        if (view instanceof RegisterView) {
            mRegisterView = (RegisterView) view;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mRegisterView != null) {
            mRegisterView = null;
        }
    }

    /**
     * 注册
     *
     * @param phone
     * @param pwd
     * @param code
     */
    public void register(final String phone, final String pwd, String code, String invite_code) {
        if (mRegisterView == null) {
            return;
        }
        mRegisterView.showLoading("正在注册...");
        Map<String, String> params = API.getParams("loginPhone", phone);
        params.put("code", code);
        params.put("password", pwd);
        params.put("inviteCode", invite_code);
        ZmVolley.request(new ZmStringRequest(API.registerKylUser, params,
                new VolleySuccessListener(mRegisterView, "注册", 3) {

                    @Override
                    public void onSuccessResponse(String response) {
                        mRegisterView.registerSuccess(phone, pwd);
                    }
                }, new VolleyErrorListener(mRegisterView, "注册", "注册失败，请稍后再试！")), mRegisterView.getRequestTag());
    }
}
