package cn.ran.flicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ran.flicenter.I;
import cn.ran.flicenter.R;
import cn.ran.flicenter.bean.NewGoodsBean;

/**
 * Created by Administrator on 2016/10/17.
 */
public class GoodsAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<NewGoodsBean> mList;

    public GoodsAdapter(Context mContext, ArrayList<NewGoodsBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case I.TYPE_FOOTER:
                holder = new FooterViewHolder(View.inflate(mContext, R.layout.item_newgoods_footer, null));
                break;
            case I.TYPE_ITEM:
                holder = new GoodsViewHolder(View.inflate(mContext, R.layout.item_newgoods, null));
                break;

        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER) {
            FooterViewHolder footer = (FooterViewHolder) holder;
            footer.tvFooter.setText("没有更多数据");
        } else {
            GoodsViewHolder goods = (GoodsViewHolder) holder;
            NewGoodsBean goodsBean = mList.get(position);
            goods.lvGoodsIntroduce.setText(goodsBean.getGoodsName());
            goods.lvGoodsPrice.setText(goodsBean.getShopPrice());
            goods.lvGoodsImage.setImageResource(R.mipmap.goods_thumb);

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


    class FooterViewHolder extends RecyclerView.ViewHolder {
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

        public GoodsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
