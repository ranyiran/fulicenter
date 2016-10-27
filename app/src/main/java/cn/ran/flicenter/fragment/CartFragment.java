package cn.ran.flicenter.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ran.flicenter.FuLiCenterApplication;
import cn.ran.flicenter.I;
import cn.ran.flicenter.R;
import cn.ran.flicenter.activity.MainActivity;
import cn.ran.flicenter.adapter.CartAdapter;
import cn.ran.flicenter.bean.CartBean;
import cn.ran.flicenter.bean.UserAvatarBean;
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
public class CartFragment extends Fragment {


    @Bind(R.id.new_goods_tv_refresh)
    TextView newGoodsTvRefresh;
    @Bind(R.id.new_goods_recycler)
    RecyclerView newGoodsRecycler;
    @Bind(R.id.new_goods_swipeRefresh)
    SwipeRefreshLayout newGoodsSwipeRefresh;
    CartAdapter mBtqAdapter;
    MainActivity mContext;
    ArrayList<CartBean> mList;
    LinearLayoutManager mManager;
    @Bind(R.id.currentPrice)
    TextView currentPrice;
    @Bind(R.id.savePrice)
    TextView savePrice;
    @Bind(R.id.btnBuy)
    Button btnBuy;
    @Bind(R.id.emptyCart)
    TextView emptyCart;
    UserAvatarBean user;
    String userName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, view);
        mContext = (MainActivity) getContext();
        mList = new ArrayList<>();
        mBtqAdapter = new CartAdapter(mContext, mList);
        mManager = new LinearLayoutManager(mContext);
        newGoodsRecycler.setLayoutManager(mManager);
        newGoodsRecycler.setAdapter(mBtqAdapter);
        downloadBoutique(I.ACTION_DOWNLOAD);
        initView();
        initData();
        setListener();
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    public void setListener() {
        setOnPullDownListener();
    }

    private void setOnPullDownListener() {
        newGoodsSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newGoodsSwipeRefresh.setEnabled(true);
                newGoodsSwipeRefresh.setRefreshing(true);
                newGoodsTvRefresh.setVisibility(View.VISIBLE);
                downloadBoutique(I.ACTION_PULL_DOWN);
            }
        });
    }

    public void initView() {
        newGoodsSwipeRefresh.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
        newGoodsRecycler.addItemDecoration(new SpaceItemDecoration(12));

    }


    public void initData() {
        downloadBoutique(I.ACTION_DOWNLOAD);
    }

    protected void downloadBoutique(final int actionDownload) {
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            userName = user.getMuserName();
            emptyCart.setVisibility(View.VISIBLE);
            NetDao.downloadCarts(mContext, userName, new OkHttpUtils.OnCompleteListener<CartBean[]>() {
                @Override
                public void onSuccess(CartBean[] result) {
                    if (result != null && result.length > 0) {
                        mBtqAdapter.setMore(true);
                        ArrayList<CartBean> list = ConvertUtils.array2List(result);
                        switch (actionDownload) {
                            case I.ACTION_DOWNLOAD:
                                mBtqAdapter.initCart(list);
                                mBtqAdapter.setMore(true);
                                break;
                            case I.ACTION_PULL_DOWN:
                                mBtqAdapter.initCart(list);
                                mBtqAdapter.setMore(true);
                                newGoodsSwipeRefresh.setRefreshing(false);
                                newGoodsTvRefresh.setVisibility(View.GONE);
                                ImageLoader.release();
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
        emptyCart.setVisibility(View.GONE);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
