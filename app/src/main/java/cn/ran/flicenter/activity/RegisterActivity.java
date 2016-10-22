package cn.ran.flicenter.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ran.flicenter.I;
import cn.ran.flicenter.R;
import cn.ran.flicenter.bean.Result;
import cn.ran.flicenter.bean.UserAvatarBean;
import cn.ran.flicenter.net.NetDao;
import cn.ran.flicenter.utils.CommonUtils;
import cn.ran.flicenter.utils.L;
import cn.ran.flicenter.utils.MFGT;
import cn.ran.flicenter.utils.OkHttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    @Bind(R.id.backClickArea)
    ImageView backClickArea;
    @Bind(R.id.tv_common_title)
    TextView tvCommonTitle;
    @Bind(R.id.etRegUserName)
    EditText etRegUserName;
    @Bind(R.id.etUserNick)
    EditText etUserNick;
    @Bind(R.id.etRegPwd)
    EditText etRegPwd;
    @Bind(R.id.etRegRePwd)
    EditText etRegRePwd;
    @Bind(R.id.btnEmpty)
    Button btnEmpty;
    @Bind(R.id.btnReg)
    Button btnReg;

    String userName;
    String userNick;
    String password;
    String rePassword;
    String mPassUserName;


    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = this;
        ButterKnife.bind(this);


    }

    @Override
    public void onBackPressed() {
        MFGT.finish(this);
    }

    @OnClick({R.id.btnEmpty, R.id.btnReg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEmpty:
                onEmpty();
                break;
            case R.id.btnReg:
                userName = etRegUserName.getText().toString().trim();
                userNick = etUserNick.getText().toString().trim();
                password = etRegPwd.getText().toString().trim();
                rePassword = etRegRePwd.getText().toString().trim();
                if (userName.isEmpty()) {
                    CommonUtils.showShortToast(R.string.user_name_connot_be_empty);
                    etRegUserName.requestFocus();
                    return;
                } else if (!userName.matches("[a-zA-Z]\\w{5,15}")) {
                    CommonUtils.showShortToast(R.string.illegal_user_name);
                    etRegUserName.requestFocus();
                    return;
                } else if (userNick.isEmpty()) {
                    CommonUtils.showShortToast(R.string.nick_name_connot_be_empty);
                    etRegUserName.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(userNick)) {
                    CommonUtils.showShortToast(R.string.nick_name_connot_be_empty);
                    etRegUserName.requestFocus();
                    return;
                } else if (password.isEmpty()) {
                    CommonUtils.showShortToast(R.string.password_connot_be_empty);
                    etRegUserName.requestFocus();
                    return;
                } else if (rePassword.isEmpty()) {
                    CommonUtils.showShortToast(R.string.confirm_password_connot_be_empty);
                    etRegUserName.requestFocus();
                    return;
                } else if (!rePassword.equals(password)) {
                    CommonUtils.showShortToast(R.string.confirmpassword);
                    etRegUserName.requestFocus();
                    return;
                }
                register();
                L.i("" + userName);
                break;
        }
    }

    private void register() {
        final ProgressDialog bd = new ProgressDialog(mContext);
        bd.setMessage(getResources().getString(R.string.registering));
        bd.show();
        NetDao.register(mContext, userName, userNick, password, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                if (result == null) {
                    CommonUtils.showShortToast(R.string.register_fail);
                } else {
                    if (result.isRetMsg()) {
                        CommonUtils.showLongToast(R.string.register_success);
                        setResult(RESULT_OK, new Intent().putExtra(I.User.USER_NAME, userName));
                        MFGT.finish((Activity) mContext);
                    } else {
                        CommonUtils.showLongToast(R.string.register_fail_exists);
                        etRegUserName.requestFocus();
                    }
                }
            }

            @Override
            public void onError(String error) {
                CommonUtils.showShortToast(R.string.register_fail);
                bd.dismiss();
            }
        });
    }

    private void onEmpty() {
        etRegUserName.setText("");
        etUserNick.setText("");
        etRegPwd.setText("");
        etRegRePwd.setText("");
    }
}
