package cn.ran.flicenter.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ran.flicenter.R;
import cn.ran.flicenter.activity.MainActivity;
import cn.ran.flicenter.adapter.CategoryAdapter;
import cn.ran.flicenter.bean.CategoryChildBean;
import cn.ran.flicenter.bean.CategoryGroupBean;
import cn.ran.flicenter.net.NetDao;
import cn.ran.flicenter.utils.ConvertUtils;
import cn.ran.flicenter.utils.L;
import cn.ran.flicenter.utils.OkHttpUtils;

public class CategoryFragment extends Fragment {


    @Bind(R.id.expandableListView)
    ExpandableListView expandableListView;
    CategoryAdapter mAdapter;
    GridLayoutManager mManager;
    MainActivity mContext;
    ArrayList<CategoryGroupBean> mGroupList;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;

    int groupCount;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);

        mContext = (MainActivity) getContext();
        mGroupList = new ArrayList<>();
        mChildList = new ArrayList<>();
        mAdapter = new CategoryAdapter(mContext, mGroupList, mChildList);

        initData();
        initView();
        setListener();
        return view;
    }

    private void initData() {
        downloadGroup();
    }

    private void downloadGroup() {
        NetDao.downloadCateGroup(mContext, new OkHttpUtils.OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {

                L.i("downloadGroup=" + result);
                if (result != null && result.length > 0) {
                    ArrayList<CategoryGroupBean> groupList = ConvertUtils.array2List(result);
                    L.i("groupList=" + groupList.size());
                    mGroupList.addAll(groupList);
                    for (int i = 0; i < groupList.size(); i++) {
                        mChildList.add(new ArrayList<CategoryChildBean>());

                        CategoryGroupBean g = groupList.get(i);
                        downloadChild(g.getId(), i);
                    }
                }
            }

            @Override
            public void onError(String error) {
                L.e("Error=" + error);
            }
        });
    }

    private void downloadChild(int id, final int index) {
        groupCount++;
        NetDao.downloadCateChild(mContext, id, new OkHttpUtils.OnCompleteListener<CategoryChildBean[]>() {
            @Override
            public void onSuccess(CategoryChildBean[] result) {
                L.i("downloadChild=" + result);
                if (result != null && result.length > 0) {
                    ArrayList<CategoryChildBean> childList = ConvertUtils.array2List(result);
                    L.i("childList=" + childList.size());
                    mChildList.set(index, childList);
                    if (groupCount == mGroupList.size()) {
                        mAdapter.initData(mGroupList, mChildList);
                    }
                }
            }

            @Override
            public void onError(String error) {
                L.e("Error=" + error);
            }
        });
    }

    private void initView() {
        mManager = new GridLayoutManager(mContext, 2);
        expandableListView.setGroupIndicator(null);
        expandableListView.setAdapter(mAdapter);

    }

    private void setListener() {
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
