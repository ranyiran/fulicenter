package cn.ran.flicenter.net;

import android.content.Context;

import cn.ran.flicenter.I;
import cn.ran.flicenter.bean.NewGoodsBean;
import cn.ran.flicenter.utils.OkHttpUtils;

/**
 * Created by Administrator on 2016/10/17.
 */
public class NetDao {
    public static void downloadNewGoods(Context mContext, int pageId, OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener) {
        OkHttpUtils utils = new OkHttpUtils(mContext);
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID, String.valueOf(I.CAT_ID))
                .addParam(I.PAGE_ID, String.valueOf(pageId))
                .addParam(I.PAGE_SIZE, String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodsBean[].class)
                .execute(listener);

    }
}
