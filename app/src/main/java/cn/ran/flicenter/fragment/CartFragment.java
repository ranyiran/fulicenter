package cn.ran.flicenter.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
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
import cn.ran.flicenter.utils.OkHttpUtils;
import cn.ran.flicenter.utils.ResultUtils;
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
    @Bind(R.id.rlPay)
    RelativeLayout rlPay;


    updateCartReceiver mReceiver;

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
        downloadCart();
        setCartLayout(false);
        initView();
        initData();
        setListener();
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    private void setCartLayout(boolean isCart) {
        rlPay.setVisibility(isCart ? View.VISIBLE : View.GONE);
        emptyCart.setVisibility(isCart ? View.GONE : View.VISIBLE);
        newGoodsRecycler.setVisibility(isCart ? View.VISIBLE : View.GONE);
        //sumPrice();
    }

    public void setListener() {
        setOnPullDownListener();
        IntentFilter filter = new IntentFilter(I.BROADCAST_UPDATA_CART);
        IntentFilter filterDelete = new IntentFilter(I.BROADCAST_DELETE_CART);
        mReceiver = new updateCartReceiver();
        mContext.registerReceiver(mReceiver, filterDelete);
        mContext.registerReceiver(mReceiver, filter);
    }

    private void setOnPullDownListener() {
        newGoodsSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newGoodsSwipeRefresh.setEnabled(true);
                newGoodsSwipeRefresh.setRefreshing(true);
                newGoodsTvRefresh.setVisibility(View.VISIBLE);
                downloadCart();
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
        downloadCart();
    }

    protected void downloadCart() {
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            userName = user.getMuserName();
            NetDao.downloadCarts(mContext, userName, new OkHttpUtils.OnCompleteListener<String>() {
                @Override
                public void onSuccess(String s) {
                    ArrayList<CartBean> list = ResultUtils.getCartFromJson(s);
                    newGoodsSwipeRefresh.setRefreshing(false);
                    newGoodsTvRefresh.setVisibility(View.GONE);
                    if (list != null && list.size() > 0) {
                        mBtqAdapter.initCart(mList);
                        mList.addAll(list);
                        mBtqAdapter.setMore(true);
                        setCartLayout(true);
                        sumPrice();
                              /*  mBtqAdapter.initCart(mList);
                                mList.addAll(list);


                                mBtqAdapter.setMore(true);
                                ImageLoader.release();*/

                    } else {
                        setCartLayout(false);

                    }
                }

                @Override
                public void onError(String error) {
                    setCartLayout(false);
                }

             /*   //emptyCart.setVisibility(View.VISIBLE);
                setCartLayout(false);
            }

            @Override
            public void onError (String error){
                setCartLayout(false);
                newGoodsSwipeRefresh.setRefreshing(false);
                //  emptyCart.setVisibility(View.VISIBLE);
                CommonUtils.showShortToast("出错");
            }*/
            });
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void sumPrice() {
        int sumPrice = 0;
        int rankPrice = 0;
        if (mList != null && mList.size() > 0) {
            for (CartBean c : mList) {
                if (c.isChecked()) {
                    sumPrice += getPrice(c.getGoods().getCurrencyPrice()) * c.getCount();
                    rankPrice += getPrice(c.getGoods().getRankPrice()) * c.getCount();
                }
            }
            currentPrice.setText("合计:￥" + Double.valueOf(sumPrice));
            savePrice.setText("节省：￥" + Double.valueOf(sumPrice - rankPrice));
        } else {
            setCartLayout(false);
            currentPrice.setText("合计:￥0");
            savePrice.setText("节省:￥0");
        }
    }

    private int getPrice(String price) {
        price = price.substring(price.indexOf("￥") + 1);
        return Integer.valueOf(price);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    class updateCartReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            setCartLayout(mList != null && mList.size() > 0);
            sumPrice();
        }

    }
}
