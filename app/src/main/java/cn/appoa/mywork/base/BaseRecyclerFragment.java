package cn.appoa.mywork.base;

import android.content.Intent;
import android.support.v7.widget.SimpleItemAnimator;

import com.scwang.smartrefresh.layout.fragment.PullToRefreshRecyclerViewFragment;

import cn.appoa.aframework.utils.AsyncRun;
import cn.appoa.aframework.view.ILoginView;
import cn.appoa.mywork.net.API;
import cn.appoa.mywork.ui.LoginActivity;


public abstract class BaseRecyclerFragment<T> extends PullToRefreshRecyclerViewFragment<T>
        implements ILoginView {

    @Override
    public boolean isLogin() {
        return API.isLogin();
    }

    @Override
    public void toLoginActivity() {
        startActivityForResult(new Intent(mActivity, LoginActivity.class), 999);
    }

    @Override
    public void toLoginSuccess(Intent data) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999 && resultCode == -1 && data != null) {
            toLoginSuccess(data);
        }
    }

    @Override
    public void initRefreshView() {
        super.initRefreshView();
        //RecyclerView调用notifyItemChanged闪烁问题
        //https://blog.csdn.net/u014537423/article/details/52777978
        ((SimpleItemAnimator)refreshView.getItemAnimator())
                .setSupportsChangeAnimations(false);
    }

    /**
     * 是否开启测试（调接口时候改为false）
     */
    protected boolean isTestData = false;

    @Override
    public void initData() {
        if (isTestData) {
            AsyncRun.runInBack(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(1 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    AsyncRun.runInMain(new Runnable() {

                        @Override
                        public void run() {
                            onSuccessResponse(null);
                        }
                    });
                }
            });
        } else {
            super.initData();
        }
    }

    /**
     * 刷新数据
     */
    public void refresh() {
        onRefresh(refreshLayout);
    }

    @Override
    public void showLoading(CharSequence message) {
        // super.showLoading(message);
    }

    @Override
    public void dismissLoading() {
        // super.dismissLoading();
    }

//    // 需要自动加载更多请取消注释以下代码
//    @Override
//    public boolean setRefreshMode() {
//        return false;
//        // return true;
//    }
//
//    @Override
//    public void initRefreshLayout(Bundle savedInstanceState) {
//        // refreshLayout.setEnableAutoLoadMore(true);// 是否启用列表惯性滑动到底部时自动加载更多
//    }
//
//    @Override
//    protected void setAdapter() {
//        if (isHasLoadMore()) {
//            if (adapter != null) {
//                adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
//
//                    @Override
//                    public void onLoadMoreRequested() {
//                        onLoadMore(refreshLayout);
//                    }
//                }, recyclerView);
//            }
//        }
//    }
//
//    /**
//     * 是否自动加载更多
//     */
//    public boolean isHasLoadMore() {
//        return true;
//    }

}
