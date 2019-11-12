package cn.appoa.mywork.net;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.appoa.aframework.app.AfApplication;
import cn.appoa.aframework.bean.BMapApi;
import cn.appoa.aframework.bean.ProvinceList;
import cn.appoa.aframework.constant.AfConstant;
import cn.appoa.aframework.utils.AESUtils;
import cn.appoa.aframework.utils.JsonUtils;
import cn.appoa.aframework.utils.SpUtils;
import cn.appoa.mywork.constant.Constant;


/**
 * 接口工具类
 */
public class APIUtils extends JsonUtils {

    /**
     * 参数封装
     *
     * @return
     */
    public static Map<String, String> getParams() {
        return getParams(null, "keyoule_app");
    }

    /**
     * 参数封装
     *
     * @param key
     * @param value
     * @return
     */
    public static Map<String, String> getParams(String key, String value) {
        Map<String, String> params = new HashMap<>();
        params.put("encrypt", AESUtils.encode(value));
        if (key != null) {
            params.put(key, value);
        }
        return params;
    }

    /**
     * 是否登录
     *
     * @return
     */
    public static boolean isLogin() {
        return (Boolean) SpUtils.getData(AfApplication.appContext, AfConstant.IS_LOGIN, false);
    }

    /**
     * 用户id
     *
     * @return
     */
    public static String getUserId() {
        return (String) SpUtils.getData(AfApplication.appContext, AfConstant.USER_ID, "");
    }

    /**
     * 商家id
     *
     * @return
     */
    public static String getShopId() {
        return (String) SpUtils.getData(AfApplication.appContext, Constant.SHOP_ID, "");
    }

    /**
     * 用户合伙人接送id
     *
     * @return
     */
    public static String getUserPartnerIdA() {
        return (String) SpUtils.getData(AfApplication.appContext, Constant.USER_PARTNERA, "");
    }

    /**
     * 用户合伙人加油站id
     *
     * @return
     */
    public static String getUserPartnerIdB() {
        return (String) SpUtils.getData(AfApplication.appContext, Constant.USER_PARTNERB, "");
    }

    /**
     * 用户合伙人物业id
     *
     * @return
     */
    public static String getUserPartnerIdC() {
        return (String) SpUtils.getData(AfApplication.appContext, Constant.USER_PARTNERC, "");
    }

    /**
     * 用户合伙人物品id
     *
     * @return
     */
    public static String getUserPartnerIdD() {
        return (String) SpUtils.getData(AfApplication.appContext, Constant.USER_PARTNERD, "");
    }

    /**
     * 格式化时间样式
     * 默认样式 yyyy-MM-dd HH:mm
     *
     * @param time
     * @return
     */
    public static String formatData(String time, String... format) {
        String result = time;
        try {
            SimpleDateFormat formatFrom = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat formatTo = null;
            if (format.length > 0) {
                formatTo = new SimpleDateFormat(format[0]);
            } else {
                formatTo = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            }
            Date resultDate = formatFrom.parse(time);
            result = formatTo.format(resultDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取省市区
     *
     * @param context
     * @return
     */
    public static List<ProvinceList> getProviceList(Context context) {
        List<ProvinceList> list = new ArrayList<ProvinceList>();
        String json = API.getJson(context, "region.json");
        if (!TextUtils.isEmpty(json)) {
            BMapApi data = JSON.parseObject(json, BMapApi.class);
            if (data.content != null) {
                list.addAll(data.content.sub);
            }
        }
        return list;
    }

    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream is = context.getAssets().open(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    //设置view的margin
    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    //设置view四边相同的margin
    public static void setMargins(View v, int dipValue) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(dipValue, dipValue, dipValue, dipValue);
            v.requestLayout();
        }
    }

    //分割
    public static List<String> imgList(String sImg, String... imgFrag) {
        if (!TextUtils.isEmpty(sImg)) {
            List<String> sList = new ArrayList<>();
            String[] s1 = sImg.split("\\|");
            for (String aS1 : s1) {
                if (imgFrag.length > 0) {
                    sList.add(imgFrag[0] + aS1);
                } else {
                    sList.add(API.IMAGE_URL + aS1);
                }
            }
            return sList;
        }
        return null;
    }
}
