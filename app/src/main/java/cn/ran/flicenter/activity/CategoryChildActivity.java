package cn.ran.flicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ran.flicenter.I;
import cn.ran.flicenter.R;
import cn.ran.flicenter.adapter.GoodsAdapter;
import cn.ran.flicenter.bean.NewGoodsBean;
import cn.ran.flicenter.net.NetDao;
import cn.ran.flicenter.utils.ConvertUtils;
import cn.ran.flicenter.utils.ImageLoader;
import cn.ran.flicenter.utils.L;
import cn.ran.flicenter.utils.OkHttpUtils;
import cn.ran.flicenter.views.SpaceItemDecoration;

public class CategoryChildActivity extends AppCompatActivity {


    @Bind(R.id.new_goods_tv_refresh)
    TextView newGoodsTvRefresh;
    @Bind(R.id.new_goods_recycler)
    RecyclerView newGoodsRecycler;
    @Bind(R.id.new_goods_swipeRefresh)
    SwipeRefreshLayout newGoodsSwipeRefresh;

    GoodsAdapter mAdapter;
    CategoryChildActivity mContext;
    ArrayList<NewGoodsBean> mList;
    GridLayoutManager glm;


    int mPageId = 1;
    int mNewState;
    int tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.Activity_category_sort);
        ButterKnife.bind(this);
        mContext = this;
        mList = new ArrayList<NewGoodsBean>();
        mAdapter = new GoodsAdapter(mContext, mList);
        glm = new GridLayoutManager(mContext, I.COLUM_NUM, LinearLayoutManager.VERTICAL, false);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == mAdapter.getItemCount() - 1 ? 2 : 1;
            }
        });
        newGoodsRecycler.setLayoutManager(glm);
        newGoodsRecycler.setAdapter(mAdapter);
        Intent intent = getIntent();
        tag = intent.getIntExtra(I.CategoryChild.CAT_ID, 0);
        initData(I.ACTION_DOWNLOAD, tag, mPageId);
        initView();
        setListener();
    }

    private void setListener() {
        setOnPullDownListener();
        setOnPullUpListener();
    }

    private void setOnPullUpListener() {
        newGoodsRecycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastIndex;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                mNewState = newState;
                lastIndex = glm.findLastVisibleItemPosition();
                if (lastIndex >= mAdapter.getItemCount() - 1 && newState == RecyclerView.SCROLL_STATE_IDLE &&
                        mAdapter.isMore()) {
                    mPageId++;
                    L.i(mPageId + "");
                    downloadNewGoods(I.ACTION_PULL_UP, tag, mPageId);
                }
                if (newState != RecyclerView.SCROLL_STATE_DRAGGING) {
                    mAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastIndex = glm.findFirstVisibleItemPosition();
            }
        });
    }

    private void downloadNewGoods(final int downloadStatus, int tag, int mPageId) {
        NetDao.downloadCategoryChild(mContext, tag, mPageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
                    @Override
                    public void onSuccess(NewGoodsBean[] result) {
                        L.i(result.toString());
                        if (result != null && result.length > 0) {
                            mAdapter.setMore(true);
                            ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                            switch (downloadStatus) {
                                case I.ACTION_DOWNLOAD:
                                    mAdapter.initNewGoods(list);
                                    mAdapter.setTvFooter("加载更多数据");
                                    break;
                                case I.ACTION_PULL_DOWN:
                                    mAdapter.initNewGoods(list);
                                    mAdapter.setTvFooter("加载更多数据");
                                    newGoodsSwipeRefresh.setRefreshing(false);
                                    newGoodsTvRefresh.setVisibility(View.GONE);
                                    ImageLoader.release();
                                    break;
                                case I.ACTION_PULL_UP:
                                    mAdapter.addNewGoods(list);
                                    break;


                            }
                            L.i(list.toString());
                        }
                        if (downloadStatus == I.ACTION_PULL_UP) {
                            mAdapter.setTvFooter("没有更多数据");

                        }
                    }

                    @Override
                    public void onError(String error) {
                        newGoodsSwipeRefresh.setRefreshing(false);

                    }
                }

        );
    }

    private void setOnPullDownListener() {
        newGoodsSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newGoodsSwipeRefresh.setEnabled(true);
                newGoodsSwipeRefresh.setRefreshing(true);
                newGoodsTvRefresh.setVisibility(View.VISIBLE);
                mPageId = 1;
                downloadNewGoods(I.ACTION_PULL_DOWN, tag, mPageId);
            }
        });
    }

    private void initView() {
        newGoodsSwipeRefresh.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
        newGoodsRecycler.addItemDecoration(new SpaceItemDecoration(12));

    }

    private void initData(int download, int actionDownload, int mPageId) {
        downloadNewGoods(I.ACTION_DOWNLOAD, tag, mPageId);
    }
}
