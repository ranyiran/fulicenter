package cn.ran.flicenter.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ran.flicenter.I;
import cn.ran.flicenter.R;
import cn.ran.flicenter.activity.MainActivity;
import cn.ran.flicenter.adapter.BoutiqueAdapter;
import cn.ran.flicenter.bean.BoutiqueBean;
import cn.ran.flicenter.bean.NewGoodsBean;
import cn.ran.flicenter.net.NetDao;
import cn.ran.flicenter.utils.CommonUtils;
import cn.ran.flicenter.utils.ConvertUtils;
import cn.ran.flicenter.utils.ImageLoader;
import cn.ran.flicenter.utils.L;
import cn.ran.flicenter.utils.OkHttpUtils;
import cn.ran.flicenter.views.SpaceItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoutiqueFragment extends Fragment {


    @Bind(R.id.new_goods_tv_refresh)
    TextView newGoodsTvRefresh;
    @Bind(R.id.new_goods_recycler)
    RecyclerView newGoodsRecycler;
    @Bind(R.id.new_goods_swipeRefresh)
    SwipeRefreshLayout newGoodsSwipeRefresh;
    BoutiqueAdapter mBtqAdapter;
    MainActivity mContext;
    ArrayList<BoutiqueBean> mList;
    LinearLayoutManager mManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_goods, container, false);
        ButterKnife.bind(this, view);
        mContext = (MainActivity) getContext();
        mList = new ArrayList<BoutiqueBean>();
        mBtqAdapter = new BoutiqueAdapter(mContext, mList);
        mManager = new LinearLayoutManager(mContext);
        newGoodsRecycler.setLayoutManager(mManager);
        newGoodsRecycler.setAdapter(mBtqAdapter);
        initData(I.ACTION_DOWNLOAD);
        initView();
        return view;
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

    private void initData(final int actionDownload) {
        NetDao.downloadBouTiQue(mContext, new OkHttpUtils.OnCompleteListener<BoutiqueBean[]>() {
            @Override
            public void onSuccess(BoutiqueBean[] result) {
                if (result != null && result.length > 0) {
                    mBtqAdapter.setMore(true);
                    ArrayList<BoutiqueBean> list = ConvertUtils.array2List(result);
                    switch (actionDownload) {
                        case I.ACTION_DOWNLOAD:
                            mBtqAdapter.initBouTiQue(list);
                            mBtqAdapter.setMore(true);
                            break;
                        case I.ACTION_PULL_DOWN:
                            mBtqAdapter.initBouTiQue(list);
                            mBtqAdapter.setMore(true);
                            newGoodsSwipeRefresh.setRefreshing(false);
                            newGoodsTvRefresh.setVisibility(View.GONE);
                            ImageLoader.release();
                            break;
                        case I.ACTION_PULL_UP:
                            mBtqAdapter.addBouTiQue(list);
                            break;


                    }
                    L.i(list.toString());
                }
            }

            @Override
            public void onError(String error) {
                newGoodsSwipeRefresh.setRefreshing(false);
                CommonUtils.showShortToast("出错");
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
