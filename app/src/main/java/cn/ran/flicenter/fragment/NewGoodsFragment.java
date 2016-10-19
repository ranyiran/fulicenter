/*
package cn.ran.flicenter.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ran.flicenter.I;
import cn.ran.flicenter.R;
import cn.ran.flicenter.adapter.GoodsAdapter;
import cn.ran.flicenter.bean.NewGoodsBean;
import cn.ran.flicenter.utils.ImageLoader;
import cn.ran.flicenter.utils.L;
import cn.ran.flicenter.utils.OkHttpUtils;

*/
/**
 * A simple {@link Fragment} subclass.
 *//*

public class NewGoodsFragment extends Fragment {
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mLvContext;
    TextView mTvFresh;
    ArrayList<NewGoodsBean> mNewGoodsList;
    GoodsAdapter mAdapter;


    int mPageId = I.PAGE_ID_DEFAULT;
    GridLayoutManager mManagaer;
    int mNewStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_goods, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.new_goods_swipeRefresh);
        mLvContext = (RecyclerView) view.findViewById(R.id.new_goods_recycler);
        mTvFresh = (TextView) view.findViewById(R.id.new_goods_tv_refresh);
        mLvContext.setAdapter(mAdapter);
        mLvContext.setLayoutManager(mManagaer);
        downloadNewGoods(I.ACTION_DOWNLOAD, mPageId);
        setListener();
        return view;
    }

    private void setListener() {
        setOnPullDownListener();
        setOnPullUpListener();
    }

    private void setOnPullUpListener() {
        mLvContext.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastIndex;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                mNewStatus = newState;
                lastIndex = mManagaer.findLastVisibleItemPosition();
                if (lastIndex >= mAdapter.getItemCount() - 1 && newState == RecyclerView.SCROLL_STATE_IDLE &&
                        mAdapter.isMore()) {
                    mPageId++;
                    downloadNewGoods(I.ACTION_PULL_UP, mPageId);
                }
                if (newState != RecyclerView.SCROLL_STATE_DRAGGING) {
                    mAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastIndex = mManagaer.findLastVisibleItemPosition();
            }
        });
    }

    private void setOnPullDownListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setEnabled(true);
                mSwipeRefreshLayout.setRefreshing(true);
                mTvFresh.setVisibility(View.VISIBLE);
                mPageId = 1;
                downloadNewGoods(I.ACTION_PULL_DOWN, mPageId);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mNewGoodsList = new ArrayList<>();
        mAdapter = new GoodsAdapter(context, mNewGoodsList);
        mManagaer = new GridLayoutManager(getActivity(),2,LinearLayoutManager.VERTICAL,false);
        mManagaer.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position==mAdapter.getItemCount()-1?2:1;
            }
        });

    }

    private void downloadNewGoods(final int downloadStatus, int mPageIdDefault) {
        //NewGoodsBean[],mPageIdDefault,
        final OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<NewGoodsBean[]>();
        utils.url(I.SERVER_ROOT + I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.GoodsDetails.KEY_CAT_ID, I.CAT_ID + "")
                .addParam(I.PAGE_ID, mPageIdDefault + "")
                .addParam(I.PAGE_SIZE, I.PAGE_SIZE_DEFAULT + "")
                .targetClass(NewGoodsBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
                    @Override
                    public void onSuccess(NewGoodsBean[] result) {
                        ArrayList<NewGoodsBean> goods = utils.array2List(result);
                        mAdapter.setMore(goods != null && goods.size() > 0);
                        if (!mAdapter.isMore()) {
                            if (downloadStatus == I.ACTION_PULL_UP) {
                                mAdapter.setTvFooter("没有更多数据");

                            }
                            return;

                        }
                        switch (downloadStatus) {
                            case I.ACTION_DOWNLOAD:
                                mAdapter.initNewGoods(goods);
                                mAdapter.setTvFooter("加载更多数据");
                                break;
                            case I.ACTION_PULL_DOWN:
                                mAdapter.initNewGoods(goods);
                                mAdapter.setTvFooter("加载更多数据");
                                mSwipeRefreshLayout.setRefreshing(false);
                                mTvFresh.setVisibility(View.GONE);
                                ImageLoader.release();
                                break;
                            case I.ACTION_PULL_UP:
                                mAdapter.addNewGoods(goods);
                                break;


                        }
                        L.i(goods.toString());
                    }

                    @Override
                    public void onError(String error) {

                    }
                });

    }


}
*/
