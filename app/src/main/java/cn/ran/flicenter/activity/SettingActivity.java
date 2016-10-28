package cn.ran.flicenter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ran.flicenter.FuLiCenterApplication;
import cn.ran.flicenter.I;
import cn.ran.flicenter.R;
import cn.ran.flicenter.bean.Result;
import cn.ran.flicenter.bean.UserAvatarBean;
import cn.ran.flicenter.dao.SharePreferencesUtils;
import cn.ran.flicenter.net.NetDao;
import cn.ran.flicenter.utils.CommonUtils;
import cn.ran.flicenter.utils.ImageLoader;
import cn.ran.flicenter.utils.L;
import cn.ran.flicenter.utils.MFGT;
import cn.ran.flicenter.utils.OkHttpUtils;
import cn.ran.flicenter.utils.OnSetAvatarListener;
import cn.ran.flicenter.utils.ResultUtils;

public class SettingActivity extends AppCompatActivity {

    @Bind(R.id.backClickArea)
    ImageView backClickArea;
    @Bind(R.id.tv_common_title)
    TextView tvCommonTitle;
    @Bind(R.id.stAvatar)
    ImageView stAvatar;
    @Bind(R.id.stNick)
    RelativeLayout stNick;
    @Bind(R.id.btnExitLogin)
    Button btnExitLogin;
    @Bind(R.id.tvUserName)
    TextView tvUserName;
    @Bind(R.id.tvUserNick)
    TextView tvUserNick;
    SettingActivity mContext;

    UserAvatarBean user;
    @Bind(R.id.rvUpdateAvatar)
    RelativeLayout rvUpdateAvatar;


    OnSetAvatarListener mOnSetAvatarListener;
    @Bind(R.id.ryLayout)
    RelativeLayout ryLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mContext = this;
        ButterKnife.bind(this);
        initData();
        tvCommonTitle.setText("修改昵称");
    }


    private void initData() {
        if (FuLiCenterApplication.getUser() != null) {
            tvUserName.setText(FuLiCenterApplication.getUser().getMuserName());
            tvUserNick.setText(FuLiCenterApplication.getUser().getMuserNick());
            UserAvatarBean user = FuLiCenterApplication.getUser();
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, stAvatar);
        } else {
            showInfo();
        }

    }

    private void showInfo() {
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, stAvatar);
            tvUserName.setText(user.getMuserName());
            tvUserNick.setText(user.getMuserNick());
        }
    }


    @OnClick({R.id.backClickArea, R.id.stAvatar, R.id.stNick, R.id.btnExitLogin, R.id.rvUpdateAvatar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backClickArea:
                MFGT.finish(this);
                break;
            case R.id.stAvatar:
                break;
            case R.id.stNick:
                Intent intent = new Intent(this, UpdateNickActivity.class);
                MFGT.gotoUpdateNick(mContext);
                break;
            case R.id.btnExitLogin:
                SharePreferencesUtils.getInstance(mContext).removeUser();
                FuLiCenterApplication.setUser(null);
                CommonUtils.showShortToast("退出成功");
                MFGT.gotoLoginActivity(mContext);
                finish();
                break;
            case R.id.rvUpdateAvatar:
                mOnSetAvatarListener = new OnSetAvatarListener(mContext, R.id.ryLayout,
                        user.getMuserName(), I.AVATAR_TYPE_USER_PATH);
                break;
        }
    }

    private void updateAvatar() {
        File file = new File(OnSetAvatarListener.getAvatarPath(mContext, user.getMavatarPath() + "/"
                + user.getMuserName() + user.getMavatarSuffix()));
        L.e("file=" + file.exists());
        L.e("file=" + file.getAbsolutePath());
        final ProgressDialog bd = new ProgressDialog(mContext);
        bd.setMessage(getResources().getString(R.string.update_avatar_ing));
        bd.show();
        NetDao.updateAvatar(mContext, user.getMuserName(), file, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                L.e("s=" + s);
                Result result = ResultUtils.getResultFromJson(s, UserAvatarBean.class);
                L.e("result" + result);
                if (result == null) {
                    CommonUtils.showLongToast(R.string.update_avatar_fail);
                } else {
                    UserAvatarBean u = (UserAvatarBean) result.getRetData();
                    if (result.isRetMsg()) {
                        ImageLoader.release();
                        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(u), mContext, stAvatar);
                        CommonUtils.showLongToast(R.string.update_avatar_sucess);
                    } else {
                        CommonUtils.showLongToast(R.string.update_avatar_fail);
                    }
                }
                bd.dismiss();

            }

            @Override
            public void onError(String error) {
                CommonUtils.showLongToast(error);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }
        if (data != null) {
            mOnSetAvatarListener.setAvatar(requestCode, data, stAvatar);
        }

        if (resultCode == RESULT_OK && requestCode == I.REQUEST_CODE_NICK) {
            CommonUtils.showLongToast("更新用户昵称成功");
            return;
        }
        if (requestCode == OnSetAvatarListener.REQUEST_CROP_PHOTO) {
            updateAvatar();
            return;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MFGT.finish(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        showInfo();

    }

}
