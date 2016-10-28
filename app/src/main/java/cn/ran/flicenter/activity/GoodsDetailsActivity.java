package cn.ran.flicenter.activity;

import android.content.DialogInterface;
import android.content.Intent;
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
import cn.ran.onekeyshare.OnekeyShare;
import cn.sharesdk.framework.ShareSDK;

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
                user = FuLiCenterApplication.getUser();
                if (user != null) {
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
                } else {
                    MFGT.gotoLoginActivity(mContext);
                }
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
                showShare();
                break;
        }
    }

    public void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == I.REQUEST_CODE_DETAIL) {

        }
    }
}
