package cn.ran.flicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.pingplusplus.android.PingppLog;
import com.pingplusplus.libone.PaymentHandler;
import com.pingplusplus.libone.PingppOne;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ran.flicenter.FuLiCenterApplication;
import cn.ran.flicenter.I;
import cn.ran.flicenter.R;
import cn.ran.flicenter.bean.CartBean;
import cn.ran.flicenter.bean.UserAvatarBean;
import cn.ran.flicenter.net.NetDao;
import cn.ran.flicenter.utils.CommonUtils;
import cn.ran.flicenter.utils.L;
import cn.ran.flicenter.utils.OkHttpUtils;
import cn.ran.flicenter.utils.ResultUtils;

/**
 * Created by Administrator on 2016/10/28.
 */
public class AddressActivity extends AppCompatActivity implements PaymentHandler {

    private static String URL = "http://218.244.151.190/demo/charge";
    @Bind(R.id.backClickArea)
    ImageView backClickArea;
    @Bind(R.id.etName)
    EditText etName;
    @Bind(R.id.etPhone)
    EditText etPhone;
    @Bind(R.id.spinner)
    Spinner spinner;
    @Bind(R.id.etAddress)
    EditText etAddress;
    @Bind(R.id.btnBuy)
    Button btnBuy;
    String cardId;
    String name;
    String address;
    String phone;

    AddressActivity mContext;
    ArrayList<CartBean> mList = null;
    String[] ids = new String[]{};
    int rankPrice = 0;

    UserAvatarBean user;
    @Bind(R.id.tv_common_title)
    TextView tvCommonTitle;
    @Bind(R.id.currentPrice)
    TextView currentPrice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        mContext = this;
        mList = new ArrayList<>();
        initData();
        tvCommonTitle.setText("确认收货地址");

        //设置需要使用的支付方式
        PingppOne.enableChannels(new String[]{"wx", "alipay", "upacp", "bfb", "jdpay_wap"});

        // 提交数据的格式，默认格式为json
        // PingppOne.CONTENT_TYPE = "application/x-www-form-urlencoded";
        PingppOne.CONTENT_TYPE = "application/json";

        PingppLog.DEBUG = true;
    }

    private void initData() {
        cardId = getIntent().getStringExtra(I.Cart.ID);
        user = FuLiCenterApplication.getUser();
        if (cardId == null || cardId == "" || user == null) {
            finish();
        }
        ids = cardId.split(",");
        geOrderList();
    }

    private void geOrderList() {
        NetDao.downloadCarts(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                L.i("s=" + s.toString());
                if (s != null) {
                    ArrayList<CartBean> list = ResultUtils.getCartFromJson(s);
                    if (list != null && list.size() > 0) {
                        mList.addAll(list);
                        sumPrice();
                    } else {
                        finish();
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public void sumPrice() {
        rankPrice = 0;
        if (mList != null && mList.size() > 0) {
            for (CartBean c : mList) {
                for (String id : ids) {
                    if (id.equals(String.valueOf(c.getId()))) {
                        L.i("rankPrice==" + rankPrice);
                        rankPrice += getPrice(c.getGoods().getRankPrice()) * c.getCount();
                    }
                }

            }
            L.i("rankPrice====" + rankPrice);
            currentPrice.setText("合计:￥" + rankPrice);
        }
    }

    private int getPrice(String price) {
        price = price.substring(price.indexOf("￥") + 1);
        return Integer.valueOf(price);
    }


    @OnClick(R.id.btnBuy)
    public void onClick() {
        /*
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
         */
        name = etName.getText().toString().trim();
        phone = etPhone.getText().toString().trim();
        address = etAddress.getText().toString().trim();
        if (name.isEmpty()) {
            CommonUtils.showShortToast("姓名不能为空");
            etName.requestFocus();
            return;
        } else if (phone.isEmpty()) {
            CommonUtils.showShortToast("地址不能为空");
            etAddress.requestFocus();
            return;
        } else if (address.isEmpty()) {
            CommonUtils.showShortToast("手机号码不能为空");
            etPhone.requestFocus();
            return;
        }
        gotoStatements();

    }

    private void gotoStatements() {
        // 产生个订单号
        String orderNo = new SimpleDateFormat("yyyyMMddhhmmss")
                .format(new Date());

        // 构建账单json对象
        JSONObject bill = new JSONObject();

        // 自定义的额外信息 选填
        JSONObject extras = new JSONObject();
        try {
            extras.put("extra1", "extra1");
            extras.put("extra2", "extra2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            bill.put("order_no", orderNo);
            bill.put("amount", rankPrice * 100);
            bill.put("extras", extras);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //壹收款: 创建支付通道的对话框
        PingppOne.showPaymentChannels(getSupportFragmentManager(), bill.toString(), URL, this);
    }

    @Override
    public void handlePaymentResult(Intent data) {
        if (data != null) {
            /**
             * code：支付结果码  -2:服务端错误、 -1：失败、 0：取消、1：成功
             * error_msg：支付结果信息
             */
            int code = data.getExtras().getInt("code");
            String errorMsg = data.getExtras().getString("error_msg");
        }
    }
}


