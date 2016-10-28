package cn.ran.flicenter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ran.flicenter.I;
import cn.ran.flicenter.R;

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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        cardId = getIntent().getStringExtra(I.Cart.ID);
    }

    @OnClick(R.id.btnBuy)
    public void onClick() {
    }
}
