package cn.ran.flicenter.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ran.flicenter.FuLiCenterApplication;
import cn.ran.flicenter.R;
import cn.ran.flicenter.activity.CollectActivity;
import cn.ran.flicenter.activity.MainActivity;
import cn.ran.flicenter.activity.SettingActivity;
import cn.ran.flicenter.bean.MessageBean;
import cn.ran.flicenter.bean.Result;
import cn.ran.flicenter.bean.UserAvatarBean;
import cn.ran.flicenter.dao.UserDao;
import cn.ran.flicenter.net.NetDao;
import cn.ran.flicenter.utils.ImageLoader;
import cn.ran.flicenter.utils.L;
import cn.ran.flicenter.utils.MFGT;
import cn.ran.flicenter.utils.OkHttpUtils;
import cn.ran.flicenter.utils.ResultUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonFragment extends Fragment {
    MainActivity mContext;


    @Bind(R.id.tvSetting)
    TextView tvSetting;
    @Bind(R.id.ivAvatar)
    ImageView ivAvatar;
    @Bind(R.id.tvUserName)
    TextView tvUserName;
    UserAvatarBean user;
    @Bind(R.id.tvCollectGoods)
    TextView tvCollectGoods;
    @Bind(R.id.lyCollectGoods)
    LinearLayout lyCollectGoods;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View layout = inflater.inflate(R.layout.fragment_person, container, false);
        ButterKnife.bind(this, layout);
        mContext = (MainActivity) getActivity();
        initView();
        initData();
        return layout;
    }

    private void initView() {

    }

    private void initData() {
        UserAvatarBean user = FuLiCenterApplication.getUser();
        if (user == null) {
            MFGT.gotoLoginActivity(mContext);
            return;
        } else {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, ivAvatar);
            tvUserName.setText(user.getMuserNick());
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            initData();
            findUserByUserName();
            findCollectCount();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public void findUserByUserName() {
        NetDao.syncUser(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                Result result = ResultUtils.getResultFromJson(s, UserAvatarBean.class);
                if (result != null) {
                    UserAvatarBean u = (UserAvatarBean) result.getRetData();
                    if (!user.equals(u)) {
                        UserDao dao = new UserDao(mContext);
                        boolean a = dao.saveUser(u);
                        if (a) {
                            FuLiCenterApplication.setUser(u);
                            user = u;
                            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, ivAvatar);
                            tvUserName.setText(user.getMuserNick());
                        }
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public void findCollectCount() {
        NetDao.downLoadCollectCount(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                L.i(result.toString());
                if (result != null) {
                    String msg = result.getMsg();
                    if (result.isSuccess()) {
                        tvCollectGoods.setText(msg);
                        L.i("mgsg++++++++++" + msg);
                    } else {
                        tvCollectGoods.setText(0 + "");
                        L.i("mgsg++++++++++" + msg);
                    }

                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @OnClick({R.id.tvSetting, R.id.lyCollectGoods})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSetting:
                MFGT.startActivity(mContext, SettingActivity.class);
                MFGT.finish(mContext);
                break;
            case R.id.lyCollectGoods:
                MFGT.startActivity(mContext, CollectActivity.class);
                break;
        }
    }
}
