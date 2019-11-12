package cn.appoa.mywork.presenter;

import java.util.List;
import java.util.Map;

import cn.appoa.aframework.listener.VolleyDatasListener;
import cn.appoa.aframework.listener.VolleyErrorListener;
import cn.appoa.aframework.presenter.BasePresenter;
import cn.appoa.aframework.view.IBaseView;
import cn.appoa.mywork.bean.VerifyCode;
import cn.appoa.mywork.net.API;
import cn.appoa.mywork.view.VerifyCodeView;
import zm.http.volley.ZmVolley;
import zm.http.volley.request.ZmStringRequest;

public class VerifyCodePresenter extends BasePresenter {

    protected VerifyCodeView mVerifyCodeView;

    @Override
    public void onAttach(IBaseView view) {
        //super.onAttach(view);
        if (view instanceof VerifyCodeView) {
            mVerifyCodeView = (VerifyCodeView) view;
        }
    }

    @Override
    public void onDetach() {
        //super.onDetach();
        if (mVerifyCodeView != null) {
            mVerifyCodeView = null;
        }
    }

    /**
     * 获取验证码
     *
     * @param phone
     * @param type  0注册1忘记密码2修改手机号3绑定手机号4设置/重置支付密码
     */
//    public void getVerifyCode(String type, String phone) {
//        if (mVerifyCodeView == null) {
//            return;
//        }
//        mVerifyCodeView.showLoading("正在发送验证码，请稍后...");
//        Map<String, String> params = API.getParams("phone", phone);
//        params.put("type", type);
//        ZmVolley.request(new ZmStringRequest(API.getKylSmsCode, params,
//                new VolleyDatasListener<VerifyCode>(mVerifyCodeView, "获取验证码", 3, VerifyCode.class) {
//
//                    @Override
//                    public void onDatasResponse(List<VerifyCode> datas) {
//                        mVerifyCodeView.setVerifyCode(datas);
//                    }
//                }, new VolleyErrorListener(mVerifyCodeView, "获取验证码", "发送验证码失败，请稍后再试！")), mVerifyCodeView.getRequestTag());
//    }
}
