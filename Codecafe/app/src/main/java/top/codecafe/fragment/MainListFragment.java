package top.codecafe.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kymjs.base.MainFragment;
import com.kymjs.frame.adapter.BasePullUpRecyclerAdapter;
import com.kymjs.frame.adapter.BaseRecyclerAdapter;
import com.kymjs.kjcore.http.HttpCallBack;
import com.kymjs.kjcore.utils.KJLoger;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import top.codecafe.R;
import top.codecafe.delegate.PullListDelegate;
import top.codecafe.inter.IRequestVo;
import top.codecafe.model.Event;
import top.codecafe.widget.EmptyLayout;

/**
 * 下拉列表界面基类
 *
 * @author kymjs (http://www.kymjs.com/) on 12/3/15.
 */
public abstract class MainListFragment<T> extends MainFragment<PullListDelegate> implements
        SwipeRefreshLayout.OnRefreshListener, IRequestVo, BaseRecyclerAdapter.OnItemClickListener {

    private long upOrDown;
    private boolean titleIsShow = true;
    private static int FLAG = 70;

    protected BasePullUpRecyclerAdapter<T> adapter;
    protected RecyclerView recyclerView;
    protected ArrayList<T> datas = new ArrayList<>();

    protected abstract BasePullUpRecyclerAdapter<T> getAdapter();

    protected abstract String getChangePageTitleAction();

    protected abstract ArrayList<T> parserInAsync(byte[] t);

    protected HttpCallBack callBack = new HttpCallBack() {
        private ArrayList<T> tempDatas;

        @Override
        public void onSuccessInAsync(byte[] t) {
            super.onSuccessInAsync(t);
            try {
                tempDatas = parserInAsync(t);
            } catch (Exception e) {
                tempDatas = null;
            }
        }

        @Override
        public void onSuccess(String t) {
            super.onSuccess(t);
            KJLoger.debug("===列表网络请求:" + t);
            if (viewDelegate.mEmptyLayout != null) {
                if (tempDatas == null || tempDatas.isEmpty() || adapter == null || adapter
                        .getItemCount() < 1) {
                    viewDelegate.mEmptyLayout.setErrorType(EmptyLayout.NODATA);
                } else {
                    viewDelegate.mEmptyLayout.dismiss();
                    adapter.refresh(tempDatas);
                    datas = tempDatas;
                }
            }
        }

        @Override
        public void onFailure(int errorNo, String strMsg) {
            super.onFailure(errorNo, strMsg);
            //有可能界面已经关闭网络请求仍然返回
            if (viewDelegate.mEmptyLayout != null && adapter != null) {
                if (adapter.getItemCount() > 1) {
                    viewDelegate.mEmptyLayout.dismiss();
                } else {
                    viewDelegate.mEmptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                }
            }
        }

        @Override
        public void onFinish() {
            super.onFinish();
            viewDelegate.setSwipeRefreshLoadedState();
        }
    };

    @Override
    protected Class<PullListDelegate> getDelegateClass() {
        return PullListDelegate.class;
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        recyclerView = viewDelegate.get(R.id.recyclerView);
        adapter = getAdapter();
        bindEven();
        viewDelegate.setOnRefreshListener(this);
        adapter.setOnItemClickListener(this);
    }

    private void bindEven() {
        viewDelegate.mEmptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRequest();
                viewDelegate.mEmptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //滚动到底部的监听
                    LinearLayoutManager layoutManager = viewDelegate.getLayoutManager();
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastItems = layoutManager.findFirstVisibleItemPosition();
                    if ((pastItems + visibleItemCount) >= totalItemCount) {
                        onBottom();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                upOrDown += dy;

                //隐藏标题栏事件的启动时机
                if (upOrDown > FLAG && titleIsShow) {
                    Event event = new Event();
                    event.setAction(getChangePageTitleAction());
                    event.arg = 1;
                    EventBus.getDefault().post(event);
                    titleIsShow = false;
                } else if (upOrDown < -10 && !titleIsShow) { // 10,防误触
                    Event event = new Event();
                    event.setAction(getChangePageTitleAction());
                    event.arg = -1;
                    EventBus.getDefault().post(event);
                    titleIsShow = true;
                }
                upOrDown = 0;
            }
        });
        recyclerView.setAdapter(adapter);
    }

    public void onBottom() {
        adapter.setState(BasePullUpRecyclerAdapter.STATE_NO_MORE);
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        doRequest();
    }

    @Override
    public void onRefresh() {
        viewDelegate.setSwipeRefreshLoadingState();
        doRequest();
    }
}
