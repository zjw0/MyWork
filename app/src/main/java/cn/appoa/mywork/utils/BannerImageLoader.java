package cn.appoa.mywork.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.youth.banner.loader.ImageLoaderInterface;

import cn.appoa.aframework.app.AfApplication;
import cn.appoa.mywork.R;
import cn.appoa.mywork.bean.BannerList;
import cn.appoa.mywork.net.API;


/**
 * 轮播图加载图片
 */
public class BannerImageLoader implements ImageLoaderInterface<View> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int layoutId;

    public BannerImageLoader() {
        super();
        this.layoutId = R.layout.item_banner;
    }

    public BannerImageLoader(int layoutId) {
        super();
        this.layoutId = layoutId;
    }

    @Override
    public View createImageView(Context context) {
        return View.inflate(context, layoutId, null);
    }

    @Override
    public void displayImage(Context context, Object obj, View view) {
        BannerList banner = (BannerList) obj;
        ImageView iv_banner = (ImageView) view.findViewById(R.id.iv_banner);
        AfApplication.imageLoader.loadImage(API.IMAGE_URL + banner.bannerImg, iv_banner, R.drawable.default_img);
    }

}
