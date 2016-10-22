package cn.ran.flicenter.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import cn.ran.flicenter.activity.BouTiQueActivity;
import cn.ran.flicenter.activity.CategoryChildActivity;
import cn.ran.flicenter.activity.GoodsDetailsActivity;
import cn.ran.flicenter.I;
import cn.ran.flicenter.activity.LoginActivity;
import cn.ran.flicenter.activity.MainActivity;
import cn.ran.flicenter.R;
import cn.ran.flicenter.activity.RegisterActivity;
import cn.ran.flicenter.bean.BoutiqueBean;
import cn.ran.flicenter.bean.CategoryChildBean;


public class MFGT {
    public static void finish(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public static void gotoMainActivity(Activity context) {
        startActivity(context, MainActivity.class);
    }

    public static void startActivity(Activity context, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    //    intent.putExtra(I.GoodsDetails.KEY_GOODS_ID,goodsId);

    public static void gotoGoodsDetailsActivity(Context context, int goodsId) {
        Intent intent = new Intent();
        intent.setClass(context, GoodsDetailsActivity.class);
        intent.putExtra(I.GoodsDetails.KEY_GOODS_ID, goodsId);
        startActivity(context, intent);
    }

    public static void startActivity(Context context, Intent intent) {
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public static void gotoBouTiQueActivity(Context context, BoutiqueBean bean) {
        Intent intent = new Intent();
        intent.setClass(context, BouTiQueActivity.class);
        intent.putExtra(I.Boutique.ID, bean);
        startActivity(context, intent);
    }

    public static void gotoCategoryActivity(Context context, int catId, String groupName, ArrayList<CategoryChildBean> list) {
        Intent intent = new Intent();
        intent.setClass(context, CategoryChildActivity.class);
        intent.putExtra(I.CategoryChild.CAT_ID, catId);
        intent.putExtra(I.CategoryGroup.NAME, groupName);
        intent.putExtra(I.CategoryChild.ID, list);
        startActivity(context, intent);
    }

    public static void gotoLoginActivity(Activity mContext, Class<LoginActivity> loginActivityClass) {
        Intent intent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(intent);
        mContext.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public static void Register2Login() {

    }

    public static void gotoRegister(Activity mContext) {
        Intent intent = new Intent();
        intent.setClass(mContext, RegisterActivity.class);
        startActivityForResult(mContext, intent, I.REQUEST_CODE_REGISTER);

    }

    public static void startActivityForResult(Activity mContext, Intent intent, int requestCode) {
        mContext.startActivityForResult(intent, requestCode);
        mContext.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

    }
}
