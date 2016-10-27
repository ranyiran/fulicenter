package cn.ran.flicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ran.flicenter.R;
import cn.ran.flicenter.activity.MainActivity;
import cn.ran.flicenter.bean.CartBean;

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
        this.mList = new ArrayList<>();
        mList.addAll(mList);
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
        return holder;
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        CartBean cartBean = mList.get(position);
        holder.chkGoods.setChecked(cartBean.isChecked());

    }


    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
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

        CartViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
