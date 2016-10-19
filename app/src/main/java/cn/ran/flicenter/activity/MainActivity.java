package cn.ran.flicenter.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ran.flicenter.R;
import cn.ran.flicenter.fragment.BoutiqueFragment;
import cn.ran.flicenter.fragment.NewGoodsFragment_02;
import cn.ran.flicenter.utils.L;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.mBtnNewGoods)
    RadioButton mBtnNewGoods;
    @Bind(R.id.mBtnBoutique)
    RadioButton mBtnBoutique;
    @Bind(R.id.mBtnCategory)
    RadioButton mBtnCategory;
    @Bind(R.id.mBtnCart)
    RadioButton mBtnCart;
    @Bind(R.id.tvCartHint)
    TextView tvCartHint;
    @Bind(R.id.radRelative)
    RelativeLayout radRelative;
    @Bind(R.id.mBtnPersonal)
    RadioButton mBtnPersonal;
    @Bind(R.id.radGroup)
    LinearLayout radGroup;
    @Bind(R.id.vLine)
    View vLine;

    RadioButton[] rbs = {};
    Fragment[] mFragment;

    int index;
    int currentIndex;

    // NewGoodsFragment goodsFragment;
    BoutiqueFragment boutiqueFragment;
    NewGoodsFragment_02 goodsFragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        L.i("MainActivity.onCreate");
        initData();
        initFragment();

    }

    private void initFragment() {
        mFragment = new Fragment[5];
        goodsFragment2 = new NewGoodsFragment_02();
        boutiqueFragment = new BoutiqueFragment();
        mFragment[0] = goodsFragment2;
        mFragment[1] = boutiqueFragment;

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_layout, boutiqueFragment)
                .add(R.id.fragment_layout, goodsFragment2)
                .show(goodsFragment2)
                .hide(boutiqueFragment)
                .commit();

    }

    public void initData() {
        rbs = new RadioButton[5];
        rbs[0] = mBtnNewGoods;
        rbs[1] = mBtnBoutique;
        rbs[2] = mBtnCategory;
        rbs[3] = mBtnCart;
        rbs[4] = mBtnPersonal;


    }

    private void setRadioButtonStatus() {
        for (int i = 0; i < rbs.length; i++) {
            if (i == index) {
                rbs[i].setChecked(true);
            } else {
                rbs[i].setChecked(false);
            }
        }
    }

    @OnClick({R.id.mBtnNewGoods, R.id.mBtnBoutique, R.id.mBtnCategory, R.id.mBtnCart, R.id.mBtnPersonal})

    public void onCheckedChange(View view) {
        switch (view.getId()) {
            case R.id.mBtnNewGoods:
                index = 0;
                break;
            case R.id.mBtnBoutique:
                index = 1;

                break;
            case R.id.mBtnCategory:
                index = 2;
                L.i("3");
                break;
            case R.id.mBtnCart:
                index = 3;
                L.i("4");
                break;
            case R.id.mBtnPersonal:
                index = 4;
                L.i("5");
                break;
        }
        setRadioButtonStatus();
        setFragment();
    }

    private void setFragment() {
        if (index != currentIndex) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.hide(mFragment[currentIndex]);
            if (!mFragment[index].isAdded()) {
                ft.add(R.id.fragment_layout, mFragment[index]);
            }
            ft.show(mFragment[index]).commit();
        }
        setRadioButtonStatus();
        currentIndex = index;
    }
}
