package cn.ran.flicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ran.flicenter.I;
import cn.ran.flicenter.R;
import cn.ran.flicenter.bean.BoutiqueBean;
import cn.ran.flicenter.utils.ImageLoader;

/**
 * Created by Administrator on 2016/10/19.
 */
public class BoutiqueAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<BoutiqueBean> mList;
    private String footerString;
    boolean isMore;

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    public BoutiqueAdapter(Context mContext, ArrayList<BoutiqueBean> mList) {
        this.mContext = mContext;
        this.mList = new ArrayList<>();
        mList.addAll(mList);
    }

    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View layout = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == I.TYPE_FOOTER) {
            layout = inflater.inflate(R.layout.item_newgoods_footer, parent, false);
            holder = new GoodsAdapter.FooterViewHolder(layout);
        } else {
            layout = inflater.inflate(R.layout.item_boutique, parent, false);
            holder = new BoutiqueViewHolder(layout);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GoodsAdapter.FooterViewHolder) {
            ((GoodsAdapter.FooterViewHolder) holder).tvFooter.setText(getFooterString());

        }
        if (holder instanceof BoutiqueViewHolder) {
            BoutiqueBean boutiqueBean = mList.get(position);
            ImageLoader.downloadImg(mContext, ((BoutiqueViewHolder) holder).boutiqueImageView, boutiqueBean.getImageurl());
            ((BoutiqueViewHolder) holder).boutiqueDescription.setText(boutiqueBean.getDescription());
            ((BoutiqueViewHolder) holder).boutiqueName.setText(boutiqueBean.getName());
            ((BoutiqueViewHolder) holder).boutiqueTitle.setText(boutiqueBean.getTitle());

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

    public int getFooterString() {
        return isMore ? R.string.load_more : R.string.no_more;
    }

    static class BoutiqueViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.boutiqueImageView)
        ImageView boutiqueImageView;
        @Bind(R.id.boutiqueTitle)
        TextView boutiqueTitle;
        @Bind(R.id.boutiqueName)
        TextView boutiqueName;
        @Bind(R.id.boutiqueDescription)
        TextView boutiqueDescription;

        BoutiqueViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void initBouTiQue(ArrayList<BoutiqueBean> list) {
        this.mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addBouTiQue(ArrayList<BoutiqueBean> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }
}
