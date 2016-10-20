package cn.ran.flicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ran.flicenter.R;
import cn.ran.flicenter.activity.MainActivity;
import cn.ran.flicenter.bean.BoutiqueBean;
import cn.ran.flicenter.bean.NewGoodsBean;
import cn.ran.flicenter.utils.ImageLoader;
import cn.ran.flicenter.utils.MFGT;

/**
 * Created by Administrator on 2016/10/19.
 */
public class BoutiqueAdapter extends RecyclerView.Adapter {
    MainActivity mContext;
    ArrayList<BoutiqueBean> mList;
    private String footerString;
    boolean isMore;

    String tvTitle;

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

    public BoutiqueAdapter(Context mContext, ArrayList<BoutiqueBean> mList) {
        this.mContext = (MainActivity) mContext;
        this.mList = new ArrayList<>();
        mList.addAll(mList);
    }

    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View layout = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        layout = inflater.inflate(R.layout.item_boutique, parent, false);
        holder = new BoutiqueViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BoutiqueBean boutiqueBean = mList.get(position);
        ImageLoader.downloadImg(mContext, ((BoutiqueViewHolder) holder).boutiqueImageView, boutiqueBean.getImageurl());
        ((BoutiqueViewHolder) holder).boutiqueDescription.setText(boutiqueBean.getDescription());
        ((BoutiqueViewHolder) holder).boutiqueName.setText(boutiqueBean.getName());
        String title = boutiqueBean.getTitle();
        setTvTitle(title);
        ((BoutiqueViewHolder) holder).boutiqueTitle.setText(boutiqueBean.getTitle());
        ((BoutiqueViewHolder) holder).boutiqueLayout.setTag(boutiqueBean);

    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }


    class BoutiqueViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.boutiqueImageView)
        ImageView boutiqueImageView;
        @Bind(R.id.boutiqueTitle)
        TextView boutiqueTitle;
        @Bind(R.id.boutiqueName)
        TextView boutiqueName;
        @Bind(R.id.boutiqueDescription)
        TextView boutiqueDescription;
        @Bind(R.id.boutiqueLayout)
        LinearLayout boutiqueLayout;

        BoutiqueViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.boutiqueLayout)
        public void onBoutiqueClick() {
            BoutiqueBean tag = (BoutiqueBean) boutiqueLayout.getTag();
            MFGT.gotoBouTiQueActivity(mContext, tag);
        }


    }

    public void initBouTiQue(ArrayList<BoutiqueBean> list) {
        this.mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

}
