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
import cn.ran.flicenter.FuLiCenterApplication;
import cn.ran.flicenter.I;
import cn.ran.flicenter.R;
import cn.ran.flicenter.bean.CollectBean;
import cn.ran.flicenter.bean.MessageBean;
import cn.ran.flicenter.bean.UserAvatarBean;
import cn.ran.flicenter.net.NetDao;
import cn.ran.flicenter.utils.CommonUtils;
import cn.ran.flicenter.utils.ImageLoader;
import cn.ran.flicenter.utils.L;
import cn.ran.flicenter.utils.MFGT;
import cn.ran.flicenter.utils.OkHttpUtils;

/**
 * Created by Administrator on 2016/10/17.
 */
public class CollectAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<CollectBean> mList;

    RecyclerView parent;
    boolean isMore;
    String tvFooter;


    UserAvatarBean user;

    int sortBy = I.SORT_BY_PRICE_DESC;

    public CollectAdapter(Context mContext, ArrayList<CollectBean> mList) {
        this.mContext = mContext;
        this.mList = new ArrayList<>();
        mList.addAll(mList);
    }

    public int getSortBy() {
        return sortBy;
    }

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;

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
                layout = inflater.inflate(R.layout.item_collect, parent, false);
                holder = new CollectViewHolder(layout);
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
            CollectViewHolder goods = (CollectViewHolder) holder;
            CollectBean goodsBean = mList.get(position);
            goods.lvGoodsIntroduce.setText(goodsBean.getGoodsName());
            ImageLoader.downloadImg(mContext, goods.lvGoodsImage, goodsBean.getGoodsThumb(), true);
            goods.lvLayoutGoods.setTag(goodsBean);
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

    public void initNewGoods(ArrayList<CollectBean> mList) {

        this.mList.clear();
        this.mList.addAll(mList);
        notifyDataSetChanged();
    }

    public void addNewGoods(ArrayList<CollectBean> mList) {
        this.mList.addAll(mList);
        notifyDataSetChanged();
    }

    @OnClick(R.id.imDelete)
    public void onClick() {
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tvFooter)
        TextView tvFooter;


        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class CollectViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.lv_goods_image)
        ImageView lvGoodsImage;
        @Bind(R.id.lv_goods_introduce)
        TextView lvGoodsIntroduce;
        @Bind(R.id.layout_goods)
        LinearLayout lvLayoutGoods;
        @Bind(R.id.imDelete)
        ImageView imDelete;

        public CollectViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.layout_goods, R.id.imDelete})
        public void onGoodsItemClick(View view) {
            switch (view.getId()) {
                case R.id.layout_goods:
                    CollectBean goodsId = (CollectBean) lvLayoutGoods.getTag();
//            mContext.startActivity(new Intent(mContext, GoodsDetailsActivity.class)
//                    .putExtra(I.GoodsDetails.KEY_GOODS_ID, goodsId));
                    MFGT.gotoGoodsDetailsActivity(mContext, goodsId.getGoodsId());
                    break;
                case R.id.imDelete:
                    final CollectBean goods = (CollectBean) lvLayoutGoods.getTag();
                    user = FuLiCenterApplication.getUser();
                    NetDao.deleteCollect(mContext, user.getMuserName(), goods.getGoodsId(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result != null && result.isSuccess()) {
                                mList.remove(goods);
                                notifyDataSetChanged();
                                CommonUtils.showLongToast("删除成功");
                            } else {
                                CommonUtils.showLongToast(result != null ? result.getMsg() : mContext.getResources().getString(R.string.delete_collect_fail));
                            }
                        }

                        @Override
                        public void onError(String error) {
                            CommonUtils.showLongToast(mContext.getResources().getString(R.string.delete_collect_error));

                        }
                    });
                    break;
            }

        }

    }

}
