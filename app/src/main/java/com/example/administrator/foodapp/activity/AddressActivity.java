package com.example.administrator.foodapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.administrator.foodapp.R;
import com.example.administrator.foodapp.adapter.BaseAdapter;
import com.example.administrator.foodapp.adapter.BaseViewHolder;
import com.example.administrator.foodapp.bean.Buy;
import com.example.administrator.foodapp.utils.JsonUtils;
import com.example.administrator.foodapp.utils.NetUtils;
import com.example.administrator.foodapp.utils.TokenUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2017/8/4.
 */

public class AddressActivity extends Activity {
    @BindView(R.id.buy_left_img)
    ImageView buyLeftImg;
    @BindView(R.id.address_recyclerView)
    RecyclerView addressRecyclerView;
    @BindView(R.id.add_relative)
    RelativeLayout addRelative;
    private BaseAdapter myAdapter;
    private List<Buy> list;
    private Buy b;
    private NetUtils net;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        addressRecyclerView.setLayoutManager(layoutManager);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(NetUtils.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                //.addConverterFactory(GsonConverterFactory.create())
                .build();
        net = retrofit.create(NetUtils.class);
        net.getBuyInfo(TokenUtils.getCachedToken(getApplicationContext())).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                list=JsonUtils.parseBuyListString(response.body());
                Log.i("debug","----------------"+list);
                setAdapter(list);
                addressRecyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.buy_left_img, R.id.address_recyclerView, R.id.add_relative})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.buy_left_img:
                finish();
                break;
            case R.id.address_recyclerView:
                break;
            case R.id.add_relative:
                Intent add_intent = new Intent(this, AddAddressActivity.class);
                startActivity(add_intent);
                break;
        }
    }

    private void setAdapter(List<Buy> list) {
        myAdapter = new BaseAdapter(getApplicationContext(), list, R.layout.item_buy) {
            @Override
            protected void change(BaseViewHolder viewHolder, Object o) {
                b = (Buy)o;
                viewHolder.getTextViewId(R.id.buy_address_tv).setText(b.getAddress());
                viewHolder.getTextViewId(R.id.buy_name_tv).setText(b.getName());
                viewHolder.getTextViewId(R.id.buy_text_tv).setText(b.getText());
            }
        };
        myAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                b=(Buy) myAdapter.getItemPosition(position);
                String id = b.getId();
                Log.i("debug","----------------"+id);
                net.getBuyIdInfo(id).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        b = JsonUtils.parseBuyString(response.body().trim());
                       Log.i("debug","----------------"+b);
                        Intent update_intent = new Intent(AddressActivity.this,BuyUpdateActivity.class);
                        update_intent.putExtra("b",b);
                        startActivity(update_intent);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onItemLongClick(View v, int position) {

            }
        });
    }
}
