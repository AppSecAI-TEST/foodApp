package com.example.administrator.foodapp.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.foodapp.R;
import com.example.administrator.foodapp.adapter.MyFragmentPagerAdapter;
import com.example.administrator.foodapp.fargment.AccountFragment;
import com.example.administrator.foodapp.fargment.EFragment;
import com.example.administrator.foodapp.fargment.FindFragment;
import com.example.administrator.foodapp.fargment.OrderFragment;
import com.example.administrator.foodapp.utils.TokenUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragments;
    @BindView(R.id.e_img)
    ImageView eImg;
    @BindView(R.id.e_tv)
    TextView eTv;
    @BindView(R.id.find_img)
    ImageView findImg;
    @BindView(R.id.find_tv)
    TextView findTv;
    @BindView(R.id.order_img)
    ImageView orderImg;
    @BindView(R.id.order_tv)
    TextView orderTv;
    @BindView(R.id.account_img)
    ImageView accountImg;
    @BindView(R.id.account_tv)
    TextView accountTv;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.e_layout)
    LinearLayout eLayout;
    @BindView(R.id.find_layout)
    LinearLayout findLayout;
    @BindView(R.id.order_layout)
    LinearLayout orderLayout;
    @BindView(R.id.account_layout)
    LinearLayout accountLayout;
    private Intent intent;
    private boolean login = false;
    private String buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mFragments = new ArrayList<>();
        Fragment mTab01 = new EFragment();
        Fragment mTab02 = new FindFragment();
        Fragment mTab03 = new OrderFragment();
        Fragment mTab04 = new AccountFragment();
        mFragments.add(mTab01);
        mFragments.add(mTab02);
        mFragments.add(mTab03);
        mFragments.add(mTab04);
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments);
        viewpager.setAdapter(mAdapter);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setTab(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        intent = getIntent();
        login = intent.getBooleanExtra("login", false);
        buy = intent.getStringExtra("buy");
        if (login) {
            setSelect(3);
        } else {
            setSelect(0);
        }
        if(buy!=null){
            Snackbar snackBar=  Snackbar.make(viewpager,buy, Snackbar.LENGTH_LONG);
            snackBar.setAction("点击", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelect(2);
                }
            });
            snackBar.show();
        }
    }

    /**
     * 调转到对应的页面
     *
     * @param i 对应的页面索引
     */
    private void setSelect(int i) {
        setTab(i);
        viewpager.setCurrentItem(i);
    }

    /**
     * @param position 页面选择
     */
    private void setTab(int position) {
        resetImg();
        switch (position) {
            case 0:
                eImg.setImageResource(R.drawable.e_light);
                eTv.setTextColor(this.getResources().getColor(R.color.colorPrimary));
                break;
            case 1:
                findImg.setImageResource(R.drawable.find_light);
                findTv.setTextColor(this.getResources().getColor(R.color.colorPrimary));
                break;
            case 2:
                orderImg.setImageResource(R.drawable.order_light);
                orderTv.setTextColor(this.getResources().getColor(R.color.colorPrimary));
                break;
            case 3:
                accountImg.setImageResource(R.drawable.account_light);
                accountTv.setTextColor(this.getResources().getColor(R.color.colorPrimary));
                break;
        }
    }

    /**
     * 隐藏fragment
     */
    private void resetImg() {
        eImg.setImageResource(R.drawable.e);
        eTv.setTextColor(this.getResources().getColor(R.color.colorText));
        findImg.setImageResource(R.drawable.find);
        findTv.setTextColor(this.getResources().getColor(R.color.colorText));
        orderImg.setImageResource(R.drawable.order);
        orderTv.setTextColor(this.getResources().getColor(R.color.colorText));
        accountImg.setImageResource(R.drawable.account);
        accountTv.setTextColor(this.getResources().getColor(R.color.colorText));
    }

    @OnClick({R.id.e_layout, R.id.find_layout, R.id.order_layout, R.id.account_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.e_layout:
                setSelect(0);
                break;
            case R.id.find_layout:
                setSelect(1);
                break;
            case R.id.order_layout:
                setSelect(2);
                break;
            case R.id.account_layout:
                setSelect(3);
                break;
        }
    }
}
