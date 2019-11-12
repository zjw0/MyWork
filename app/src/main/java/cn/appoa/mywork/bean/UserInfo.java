package cn.appoa.mywork.bean;

import android.content.Context;

import java.io.Serializable;

import cn.appoa.aframework.utils.SpUtils;
import cn.appoa.mywork.constant.Constant;

/**
 * 用户资料
 */
public class UserInfo implements Serializable {

    public String id;
    public String orderBy;
    public String groupBy;
    public int pageNo;
    public int pageSize;
    public boolean isNewRecord;
    public String remarks;
    public String createDate;
    public String updateDate;
    public String extendMap;
    public String totalCount;
    public String totalDate;
    public String totalType;
    public String loginPhone;
    public String password;
    public String openId;
    public String payPassword;
    public String inviteCode;
    public String inviteCodeImg;
    public int inviteNo;
    public String inviteId;
    public String inviteIdLabel;
    public String inviteIdPicture;
    public String headImg;
    public String nickName;
    public String sex;// sex(1男2女)
    public String sexLabel;
    public String sexPicture;
    public String signature;
    public String wxAccount;
    public double balance;
    public double totalIncome;// 累计收益（元）
    public double deposit;
    public String shopId;
    public String partnera;
    public String partnerb;
    public String partnerc;
    public String partnerd;

    public void saveUserInfo(Context context) {
        SpUtils.putData(context, Constant.USER_ID, id);
        SpUtils.putData(context, Constant.SHOP_ID, shopId);
        //合伙人类型 partnera=接送 partnerb=加油 partnerc=物业费 partnerd=物品
        SpUtils.putData(context, Constant.USER_PARTNERA, partnera);
        SpUtils.putData(context, Constant.USER_PARTNERB, partnerb);
        SpUtils.putData(context, Constant.USER_PARTNERC, partnerc);
        SpUtils.putData(context, Constant.USER_PARTNERD, partnerd);
        SpUtils.putData(context,"loginPhone",loginPhone);
        SpUtils.putData(context,"deposit",String.valueOf(deposit));
    }
}
