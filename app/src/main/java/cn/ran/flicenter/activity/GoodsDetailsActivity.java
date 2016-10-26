package cn.ran.flicenter.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ran.flicenter.FuLiCenterApplication;
import cn.ran.flicenter.I;
import cn.ran.flicenter.R;
import cn.ran.flicenter.bean.AlbumsBean;
import cn.ran.flicenter.bean.CartResultBean;
import cn.ran.flicenter.bean.GoodsDetailsBean;
import cn.ran.flicenter.bean.MessageBean;
import cn.ran.flicenter.bean.UserAvatarBean;
import cn.ran.flicenter.net.NetDao;
import cn.ran.flicenter.utils.CommonUtils;
import cn.ran.flicenter.utils.L;
import cn.ran.flicenter.utils.MFGT;
import cn.ran.flicenter.utils.OkHttpUtils;
import cn.ran.flicenter.views.FlowIndicator;
import cn.ran.flicenter.views.SlideAutoLoopView;

/**
 * Created by Administrator on 2016/10/17.
 */
public class GoodsDetailsActivity extends BaseActivity {


    @Bind(R.id.detailsEnglishName)
    TextView detailsEnglishName;
    @Bind(R.id.detailsGoodsName)
    TextView detailsGoodsName;
    @Bind(R.id.detailsColor)
    TextView detailsColor;
    @Bind(R.id.detailsPriceShop)
    TextView detailsPrice;
    @Bind(R.id.detailsPriceCurrent)
    TextView detailsPriceCurrent;
    int goodsId;
    GoodsDetailsActivity mContext;
    @Bind(R.id.detailsSlideAutoLoopView)
    SlideAutoLoopView detailsSlideAutoLoopView;
    @Bind(R.id.detailsGoods)
    WebView detailsGoods;
    @Bind(R.id.detailsFlowIndicator)
    FlowIndicator detailsFlowIndicator;
    @Bind(R.id.iv_details_cart)
    ImageView ivDetailsCart;
    @Bind(R.id.iv_details_collect)
    ImageView ivDetailsCollect;
    @Bind(R.id.iv_details_share)
    ImageView ivDetailsShare;

    String userName;
    UserAvatarBean user;

    boolean isCollect = true;
    @Bind(R.id.lv_details_back)
    ImageView lvDetailsBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.item_goods_details);
        ButterKnife.bind(this);
        mContext = this;
        userName = FuLiCenterApplication.userName;
        goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        if (goodsId == 0) {
            finish();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            userName = user.getMuserName();
            NetDao.downloadIsCollect(mContext, userName, goodsId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    L.i("result" + result.toString());
                    if (result != null) {
                        isCollect = result.isSuccess();
                        if (isCollect) {
                            ivDetailsCollect.setImageResource(R.mipmap.bg_collect_out);
                        } else {
                            ivDetailsCollect.setImageResource(R.mipmap.bg_collect_in);
                        }
                    } else {
                    }
                }

                @Override
                public void onError(String error) {

                }
            });
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
    }

    @Override
    protected void initData() {
        NetDao.downloadGoodsDetail(mContext, goodsId, new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                L.i(result.toString());
                if (result != null) {
                    showGoodDetails(result);
                } else {
                    finish();
                }
            }


            @Override
            public void onError(String error) {
                finish();
                CommonUtils.showLongToast(error + "");
            }
        });
    }

    @OnClick(R.id.lv_details_back)
    public void onBackClick() {
        MFGT.finish(this);
    }

    private void showGoodDetails(GoodsDetailsBean details) {
        detailsEnglishName.setText(details.getGoodsEnglishName());
        detailsGoodsName.setText(details.getGoodsName());
        detailsPrice.setText(details.getShopPrice());
        detailsPriceCurrent.setText(details.getCurrencyPrice());
        detailsSlideAutoLoopView.startPlayLoop(detailsFlowIndicator, getAlbumImgUrl(details), getAlbumImgCount(details));
        detailsGoods.loadDataWithBaseURL(null, details.getGoodsBrief(), I.TEXT_HTML, I.UTF_8, null);
    }

    private int getAlbumImgCount(GoodsDetailsBean details) {
        if (details.getProperties() != null && details.getProperties().length > 0) {
            return details.getProperties()[0].getAlbums().length;
        }
        return 0;

    }

    private String[] getAlbumImgUrl(GoodsDetailsBean details) {
        String[] urls = new String[]{};
        if (details.getProperties() != null && details.getProperties().length > 0) {
            AlbumsBean[] albums = details.getProperties()[0].getAlbums();
            urls = new String[albums.length];
            for (int i = 0; i < albums.length; i++) {
                urls[i] = albums[i].getImgUrl();
            }
            L.i(urls[0].toString());
        }
        return urls;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick({R.id.lv_details_back, R.id.iv_details_cart, R.id.iv_details_collect, R.id.iv_details_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lv_details_back:
                MFGT.finish(this);
                break;
            case R.id.iv_details_cart:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View layout = getLayoutInflater().inflate(R.layout.item_dialog, null);
                final EditText et = (EditText) layout.findViewById(R.id.etCount);
                builder.setTitle("输入添加的商品个数")
                        .setView(layout)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String count = et.getText().toString();
                                initCartData(Integer.valueOf(count));
                            }
                        }).setNegativeButton("取消", null).create().show();

                break;
            case R.id.iv_details_collect:
                user = FuLiCenterApplication.getUser();
                if (user != null) {
                    userName = user.getMuserName();
                    if (!isCollect) {
                        NetDao.addCollect(mContext, userName, goodsId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                            @Override
                            public void onSuccess(MessageBean result) {
                                if (result != null) {
                                    if (result.isSuccess()) {
                                        CommonUtils.showLongToast("收藏成功");
                                        ivDetailsCollect.setImageResource(R.mipmap.bg_collect_out);
                                    }
                                }
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });
                    } else {
                        NetDao.deleteCollect(mContext, userName, goodsId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                            @Override
                            public void onSuccess(MessageBean result) {
                                if (result != null) {
                                    if (result.isSuccess()) {
                                        CommonUtils.showLongToast("取消收藏");
                                        ivDetailsCollect.setImageResource(R.mipmap.bg_collect_in);

                                    }
                                }
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });
                    }
                } else {
                    CommonUtils.showShortToast("未登录");
                    MFGT.gotoLoginActivity(mContext);
                }
                break;
            case R.id.iv_details_share:
                break;
        }
    }

    private void initCartData(int count) {
        NetDao.downloadCart(mContext, goodsId, userName, count, true, new OkHttpUtils.OnCompleteListener<CartResultBean>() {
            @Override
            public void onSuccess(CartResultBean result) {
                if (result.getSuccess() == true) {
                    CommonUtils.showShortToast(R.string.cart_sucess);
                } else {
                    CommonUtils.showShortToast(R.string.cart_error);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
