package cn.appoa.mywork.bean;

import java.io.Serializable;


public class BannerList implements Serializable {

    public String id;
    public String bannerImg;

    public BannerList() {
    }

    public BannerList(String image) {
        this.bannerImg = image;
    }
}
