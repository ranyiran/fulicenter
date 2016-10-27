package cn.ran.flicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ran.flicenter.I;
import cn.ran.flicenter.R;
import cn.ran.flicenter.activity.MainActivity;
import cn.ran.flicenter.bean.CartBean;
import cn.ran.flicenter.bean.GoodsDetailsBean;
import cn.ran.flicenter.utils.ImageLoader;

/**
 * Created by Ran on 2016/10/23.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    MainActivity mContext;
    ArrayList<CartBean> mList;
    boolean isMore;
    String tvTitle;
    private String footerString;

    public CartAdapter(Context mContext, ArrayList<CartBean> mList) {
        this.mContext = (MainActivity) mContext;
        this.mList = mList;
    }

    public String getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(String tvTitle) {
        this.tvTitle = tvTitle;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    @Override

    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CartViewHolder holder = null;
        View layout = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        layout = inflater.inflate(R.layout.item_cart_good, parent, false);
        holder = new CartViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        final CartBean cartBean = mList.get(position);
        GoodsDetailsBean goods = cartBean.getGoods();
        if (goods != null) {
            ImageLoader.downloadImg(mContext, holder.imGoodsImage, goods.getGoodsThumb());
            holder.chkGoods.setChecked(cartBean.isChecked());
            holder.tvGoodsName.setText(goods.getGoodsName());
            holder.tvPrice.setText(goods.getCurrencyPrice());
        }
        holder.tvCount.setText("(" + cartBean.getCount() + ")");
        holder.chkGoods.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cartBean.setChecked(isChecked);
                mContext.sendBroadcast(new Intent(I.BROADCAST_UPDATA_CART));
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void initCart(ArrayList<CartBean> list) {
        this.mList.clear();
        mList = list;
    }

    @OnClick(R.id.chkGoods)
    public void onClick() {
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.chkGoods)
        CheckBox chkGoods;
        @Bind(R.id.imGoodsImage)
        ImageView imGoodsImage;
        @Bind(R.id.tvGoodsName)
        TextView tvGoodsName;
        @Bind(R.id.imAdd)
        ImageView imAdd;
        @Bind(R.id.imDel)
        ImageView imDel;
        @Bind(R.id.tvPrice)
        TextView tvPrice;
        @Bind(R.id.tvCount)
        TextView tvCount;

        CartViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}
