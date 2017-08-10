package com.example.administrator.foodapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

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
 * Created by Administrator on 2017/8/6.
 */

public class BuyActivity extends Activity {
    @BindView(R.id.food_left_img)
    ImageView foodLeftImg;
    @BindView(R.id.buy_address_recyclerView)
    RecyclerView buyAddressRecyclerView;
    private BaseAdapter myAdapter;
    private List<Buy> list;
    private Buy b;
    private NetUtils net;
    private Intent intent;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        ButterKnife.bind(this);
        intent = getIntent();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        buyAddressRecyclerView.setLayoutManager(layoutManager);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(NetUtils.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                //.addConverterFactory(GsonConverterFactory.create())
                .build();
        net = retrofit.create(NetUtils.class);
        net.getBuyInfo(TokenUtils.getCachedToken(getApplicationContext())).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                list= JsonUtils.parseBuyListString(response.body());
                Log.i("debug","----------------"+list);
                setAdapter(list);
                buyAddressRecyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
        handler = new Handler(){
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        String buyId= (String)msg.obj;
                        String loginName=  TokenUtils.getCachedToken(getApplicationContext());
                        String food = intent.getStringExtra("food");
                        Log.i("debug",loginName+"----------------======"+buyId+food);
                        net.getOrderAddInfo(loginName,buyId,food).enqueue(new retrofit2.Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                            //   Toast.makeText(getApplicationContext(),response.body().trim(),Toast.LENGTH_SHORT).show();
                                intent = new Intent(BuyActivity.this,MainActivity.class);
                                intent.putExtra("buy",response.body().trim());
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                        break;
                }
            }
        };
    }

    @OnClick({R.id.food_left_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.food_left_img:
                finish();
                break;

        }
    }

    private void setAdapter(List<Buy> list) {
        myAdapter = new BaseAdapter(getApplicationContext(), list, R.layout.item_buy) {
            @Override
            protected void change(BaseViewHolder viewHolder, Object o) {
                b = (Buy) o;
                viewHolder.getTextViewId(R.id.buy_address_tv).setText(b.getAddress());
                viewHolder.getTextViewId(R.id.buy_name_tv).setText(b.getName());
                viewHolder.getTextViewId(R.id.buy_text_tv).setText(b.getText());
            }
        };
        myAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                b = (Buy) myAdapter.getItemPosition(position);
                String id = b.getId();
                Log.i("debug", "----------------" + id);
                net.getBuyIdInfo(id).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        b = JsonUtils.parseBuyString(response.body().trim());
                        Log.i("debug", "----------------" + b);
                        Message message = new Message();
                        message.what = 1;
                        message.obj = b.getId();
                        handler.sendMessage(message);
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
