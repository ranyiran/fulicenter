package cn.ran.flicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ran.flicenter.I;
import cn.ran.flicenter.R;
import cn.ran.flicenter.bean.NewGoodsBean;
import cn.ran.flicenter.utils.ImageLoader;
import cn.ran.flicenter.utils.L;
import cn.ran.flicenter.utils.MFGT;

/**
 * Created by Administrator on 2016/10/17.
 */
public class GoodsAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<NewGoodsBean> mList;

    RecyclerView parent;
    boolean isMore;
    String tvFooter;

    int sortBy = I.SORT_BY_PRICE_DESC;

    public int getSortBy() {
        return sortBy;
    }

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
        sortBy();
        
        notifyDataSetChanged();
    }

    public String getTvFooter() {
        return tvFooter;
    }

    public void setTvFooter(String tvFooter) {
        this.tvFooter = tvFooter;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    public GoodsAdapter(Context mContext, ArrayList<NewGoodsBean> mList) {
        this.mContext = mContext;
        this.mList = new ArrayList<>();
        mList.addAll(mList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = (RecyclerView) parent;
        RecyclerView.ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View layout = null;
        switch (viewType) {
            case I.TYPE_FOOTER:
                layout = inflater.inflate(R.layout.item_newgoods_footer, parent, false);
                holder = new FooterViewHolder(layout);
                break;
            case I.TYPE_ITEM:
                layout = inflater.inflate(R.layout.item_newgoods, parent, false);
                holder = new GoodsViewHolder(layout);
                break;

        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER) {
            FooterViewHolder footer = (FooterViewHolder) holder;
            footer.tvFooter.setText(getTvFooter());
        } else {
            GoodsViewHolder goods = (GoodsViewHolder) holder;
            NewGoodsBean goodsBean = mList.get(position);
            goods.lvGoodsIntroduce.setText(goodsBean.getGoodsName());
            goods.lvGoodsPrice.setText(goodsBean.getShopPrice());
            ImageLoader.downloadImg(mContext, goods.lvGoodsImage, goodsBean.getGoodsThumb(), true);
            goods.lvLayoutGoods.setTag(goodsBean.getGoodsId());
            L.i("goodsBean:" + goodsBean.getGoodsId());

        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        } else {
            return I.TYPE_ITEM;
        }
    }


    static class FooterViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tvFooter)
        TextView tvFooter;


        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class GoodsViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.lv_goods_image)
        ImageView lvGoodsImage;
        @Bind(R.id.lv_goods_introduce)
        TextView lvGoodsIntroduce;
        @Bind(R.id.lv_goods_price)
        TextView lvGoodsPrice;
        @Bind(R.id.layout_goods)
        LinearLayout lvLayoutGoods;


        public GoodsViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.layout_goods)
        public void onGoodsItemClick() {
            int goodsId = (int) lvLayoutGoods.getTag();
//            mContext.startActivity(new Intent(mContext, GoodsDetailsActivity.class)
//                    .putExtra(I.GoodsDetails.KEY_GOODS_ID, goodsId));
            MFGT.gotoGoodsDetailsActivity(mContext, goodsId);

        }
    }

    public void initNewGoods(ArrayList<NewGoodsBean> mList) {

        this.mList.clear();
        this.mList.addAll(mList);
        notifyDataSetChanged();
    }

    public void addNewGoods(ArrayList<NewGoodsBean> mList) {
        this.mList.addAll(mList);
        notifyDataSetChanged();
    }

    private void sortBy() {
        Collections.sort(mList, new Comparator<NewGoodsBean>() {
            @Override
            public int compare(NewGoodsBean lhs, NewGoodsBean rhs) {
                int result = 0;
                switch (sortBy) {
                    case I.SORT_BY_ADDTIME_ASC:
                        result = (int) (Long.valueOf(lhs.getAddTime()) - Long.valueOf(rhs.getAddTime()));
                        break;
                    case I.SORT_BY_ADDTIME_DESC:
                        result = (int) (Long.valueOf(rhs.getAddTime()) - Long.valueOf(lhs.getAddTime()));
                        break;
                    case I.SORT_BY_PRICE_ASC:
                        result = getPrice(lhs.getCurrencyPrice()) - getPrice(rhs.getCurrencyPrice());
                        break;
                    case I.SORT_BY_PRICE_DESC:
                        result = getPrice(rhs.getCurrencyPrice()) - getPrice(lhs.getCurrencyPrice());

                        break;

                }
                return result;
            }

            private int getPrice(String price) {
                price = price.substring(price.indexOf("￥") + 1);
                return Integer.valueOf(price);
            }
        });
    }

}
