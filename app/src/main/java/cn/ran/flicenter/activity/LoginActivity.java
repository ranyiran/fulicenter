package cn.ran.flicenter.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ran.flicenter.FuLiCenterApplication;
import cn.ran.flicenter.I;
import cn.ran.flicenter.R;
import cn.ran.flicenter.bean.Result;
import cn.ran.flicenter.bean.UserAvatarBean;
import cn.ran.flicenter.net.NetDao;
import cn.ran.flicenter.utils.MFGT;
import cn.ran.flicenter.utils.OkHttpUtils;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.backClickArea)
    ImageView tlBack;
    @Bind(R.id.tv_common_title)
    TextView tlTitle;
    @Bind(R.id.etLoginUserName)
    EditText etLoginUserName;
    @Bind(R.id.etLoginPwd)
    EditText etLoginPwd;
    @Bind(R.id.btnLogin)
    Button btnLogin;
    @Bind(R.id.btnReg)
    Button btnReg;

    String userName, password;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String toUserName = intent.getStringExtra("passusername");
        etLoginUserName.setText(toUserName);
    }


    private void initData() {
        userName = etLoginUserName.getText().toString();
        password = etLoginPwd.getText().toString();
        NetDao.loginSet(this, userName, password, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                if (result.getRetCode() == 0) {
                    String json = result.getRetData().toString();
                    Gson gson = new Gson();
                    UserAvatarBean user = gson.fromJson(json, UserAvatarBean.class);
                    String userNick = user.getMuserNick();
                    String userName = FuLiCenterApplication.userName;
                    Toast.makeText(LoginActivity.this, "欢迎用户:" + userNick, Toast.LENGTH_SHORT).show();
                    MFGT.gotoMainActivity((Activity) mContext);
                } else if (result.getRetCode() == I.MSG_LOGIN_UNKNOW_USER) {
                    Toast.makeText(LoginActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                } else if (result.getRetCode() == I.MSG_LOGIN_ERROR_PASSWORD) {
                    Toast.makeText(LoginActivity.this, "账户密码错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @OnClick({R.id.btnLogin, R.id.btnReg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                initData();

                break;
            case R.id.btnReg:
                MFGT.gotoRegister(LoginActivity.this);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        MFGT.finish(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == I.REQUEST_CODE_REGISTER) {
            String name = data.getStringExtra(I.User.USER_NAME);
            etLoginUserName.setText(name);

        }
    }
}
