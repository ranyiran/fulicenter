package cn.ran.flicenter.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ran.flicenter.R;
import cn.ran.flicenter.bean.CategoryChildBean;
import cn.ran.flicenter.bean.CategoryGroupBean;
import cn.ran.flicenter.utils.ImageLoader;

/**
 * Created by Administrator on 2016/10/20.
 */
public class CategoryAdapter extends BaseExpandableListAdapter {
    Context mContext;
    ArrayList<CategoryGroupBean> mGroupList;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;

    public CategoryAdapter(Context mContext, ArrayList<CategoryGroupBean> mGroupList
            , ArrayList<ArrayList<CategoryChildBean>> mChildList) {
        this.mContext = mContext;
        this.mGroupList = new ArrayList<>();
        this.mGroupList.addAll(mGroupList);
        this.mChildList = new ArrayList<>();
        this.mChildList.addAll(mChildList);
    }

    @Override
    public int getGroupCount() {
        return mGroupList != null ? mGroupList.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildList != null && mChildList.get(groupPosition)
                != null ? mChildList.get(groupPosition).size() : 0;
    }

    @Override
    public CategoryGroupBean getGroup(int groupPosition) {
        return mGroupList != null ? mGroupList.get(groupPosition) : null;
    }

    @Override
    public CategoryChildBean getChild(int groupPosition, int childPosition) {
        return mChildList != null && mChildList.get(groupPosition)
                != null ? mChildList.get(groupPosition).get(childPosition) : null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view
            , ViewGroup parent) {
        GroupViewHolder holder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_category_group, null);
            holder = new GroupViewHolder(view);
            view.setTag(holder);
        } else {
            view.getTag();
            holder = (GroupViewHolder) view.getTag();
        }
        CategoryGroupBean groupBean = getGroup(groupPosition);
        if (groupBean != null) {
            ImageLoader.downloadImg(mContext, holder.cateGroupImage, groupBean.getImageUrl());
            holder.cateGroupTitle.setText(groupBean.getName());
            holder.cateGroupExpand.setImageResource(isExpanded ? R.mipmap.expand_off :
                    R.mipmap.expand_on);

        }
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        ChildViewHolder holder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_category_child, null);
            holder = new ChildViewHolder(view);
            view.setTag(holder);
        } else {
            view.getTag();
            holder = (ChildViewHolder) view.getTag();
        }

        CategoryChildBean childBean = getChild(groupPosition, childPosition);
        if (childBean != null) {
            ImageLoader.downloadImg(mContext, holder.cateChildImage, childBean.getImageUrl());
            holder.cateChildTitle.setText(childBean.getName());
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void initData(ArrayList<CategoryGroupBean>
                                 mGroupList, ArrayList<ArrayList<CategoryChildBean>> mChildList) {
        if (mGroupList != null) {
            this.mGroupList.clear();
        }
        this.mGroupList.addAll(mGroupList);
        if (mChildList != null) {
            this.mChildList.clear();
        }
        this.mChildList.addAll(mChildList);
    }


    static class GroupViewHolder {
        @Bind(R.id.cateGroupImage)
        ImageView cateGroupImage;
        @Bind(R.id.cateGroupTitle)
        TextView cateGroupTitle;
        @Bind(R.id.cateGroupExpand)
        ImageView cateGroupExpand;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildViewHolder {
        @Bind(R.id.cateChildImage)
        ImageView cateChildImage;
        @Bind(R.id.cateChildTitle)
        TextView cateChildTitle;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
