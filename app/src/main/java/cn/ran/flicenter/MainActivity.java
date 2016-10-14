package cn.ran.flicenter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import cn.ran.flicenter.utils.L;

public class MainActivity extends AppCompatActivity {
    RadioButton mRadNewGoods, mRadBoutique, mRadCategory, mRadCart, mRadpersonal;


    boolean isChecked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        L.i("MainActivity.onCreate");
        initView();
    }

    private void initView() {
        mRadNewGoods = (RadioButton) findViewById(R.id.rad_btn_newGoods);
        mRadBoutique = (RadioButton) findViewById(R.id.rad_btn_boutique);
        mRadCategory = (RadioButton) findViewById(R.id.rad_btn_category);
        mRadCart = (RadioButton) findViewById(R.id.rad_btn_cart);
        mRadpersonal = (RadioButton) findViewById(R.id.rad_btn_personal);
    }


    public void onCheckedChange(View view) {

        switch (view.getId()) {
            case R.id.rad_btn_newGoods:
                changeImageView((RadioButton) view);
                break;
            case R.id.rad_btn_boutique:
                changeImageView((RadioButton) view);
                break;
            case R.id.rad_btn_category:
                changeImageView((RadioButton) view);
                break;
            case R.id.rad_btn_cart:
                changeImageView((RadioButton) view);
                break;
            case R.id.rad_btn_personal:
                changeImageView((RadioButton) view);
                break;
        }
    }

    public void changeImageView(RadioButton radioButton) {
        if (radioButton != mRadNewGoods) {
            mRadNewGoods.setChecked(false);
        }
        if (radioButton != mRadBoutique) {
            mRadBoutique.setChecked(false);
        }
        if (radioButton != mRadCart) {
            mRadCart.setChecked(false);
        }
        if (radioButton != mRadCategory) {
            mRadCategory.setChecked(false);
        }
        if (radioButton != mRadpersonal) {
            mRadpersonal.setChecked(false);
        }

    }
}
