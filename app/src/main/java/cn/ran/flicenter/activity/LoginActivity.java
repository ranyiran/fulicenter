package cn.ran.flicenter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
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
import cn.ran.flicenter.R;
import cn.ran.flicenter.bean.Result;
import cn.ran.flicenter.bean.UserAvatarBean;
import cn.ran.flicenter.net.NetDao;
import cn.ran.flicenter.utils.L;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        userName = etLoginUserName.getText().toString();
        password = etLoginPwd.getText().toString();
    }

    private void initData() {
        onLogin(userName, password);

    }


    private void onLogin(String userName, String password) {
        NetDao.loginSet(this, userName, password, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                if (result.getRetCode() == 0 && result.isRetMsg() == true) {
                    String json = result.getRetData().toString();
                    Gson gson = new Gson();
                    UserAvatarBean user = gson.fromJson(json, UserAvatarBean.class);
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
                MFGT.startActivity(this, RegisterActivity.class);
                break;
        }
    }
}
