package cn.ran.flicenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import cn.ran.flicenter.bean.GoodsDetailsBean;
import cn.ran.flicenter.utils.L;
import cn.ran.flicenter.utils.OkHttpUtils;

/**
 * Created by Administrator on 2016/10/17.
 */
public class GoodsDetailsActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_goods_details);
        int goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        L.i("" + goodsId);
        initData(goodsId);
    }


    private void initData(int goodsId) {
        OkHttpUtils<GoodsDetailsBean> utils = new OkHttpUtils<>(this);
        utils.setRequestUrl(I.SERVER_ROOT + I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(I.GoodsDetails.KEY_GOODS_ID, String.valueOf(goodsId))
                .targetClass(GoodsDetailsBean.class)
                .execute(new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
                    @Override
                    public void onSuccess(GoodsDetailsBean result) {
                        L.i(result.toString());
                    }

                    @Override
                    public void onError(String error) {

                    }
                });

    }
}
