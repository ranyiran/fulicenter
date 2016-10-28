package cn.ran.flicenter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

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
public class AddressActivity extends AppCompatActivity {

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
        name = etName.getText().toString().trim();
        phone = etPhone.getText().toString().trim();
        address = etAddress.getText().toString().trim();
        if (name == null && name.length() < 0) {
            CommonUtils.showShortToast("姓名不能为空");
            etName.requestFocus();
            return;
        } else if (address == null && address.length() < 0) {
            CommonUtils.showShortToast("地址不能为空");
            etAddress.requestFocus();
        } else if (phone == null && phone.length() < 0) {
            CommonUtils.showShortToast("手机号码不能为空");
            etPhone.requestFocus();
        } else {
            L.i("提交订单");
        }
    }


}
