package cn.ran.flicenter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ran.flicenter.I;
import cn.ran.flicenter.R;
import cn.ran.flicenter.bean.AlbumsBean;
import cn.ran.flicenter.bean.GoodsDetailsBean;
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
    @Bind(R.id.lv_details_back)
    ImageView lvDetailsBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.item_goods_details);
        ButterKnife.bind(this);

        goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        if (goodsId == 0) {
            finish();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
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
}
