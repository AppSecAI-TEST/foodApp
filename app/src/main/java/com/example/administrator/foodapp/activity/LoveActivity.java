package com.example.administrator.foodapp.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.example.administrator.foodapp.bean.Love;
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
 * Created by Administrator on 2017/8/11.
 */

public class LoveActivity extends Activity {
    @BindView(R.id.love_left_img)
    ImageView loveLeftImg;
    @BindView(R.id.love_recyclerView)
    RecyclerView loveRecyclerView;
    private NetUtils net;
    private BaseAdapter myAdapter;
    private List<Love> list;
    private Love l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        loveRecyclerView.setLayoutManager(layoutManager);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(NetUtils.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                //.addConverterFactory(GsonConverterFactory.create())
                .build();
        net = retrofit.create(NetUtils.class);
        net.getLoveInfo(TokenUtils.getCachedToken(getApplicationContext())).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                list = JsonUtils.parseLoveListString(response.body().trim());
                setAdapter(list);
                loveRecyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.love_left_img)
    public void onViewClicked() {
        finish();
    }

    private void setAdapter(List<Love> list) {
        myAdapter = new BaseAdapter(getApplicationContext(), list, R.layout.item_love) {
            @Override
            protected void change(BaseViewHolder viewHolder, Object o) {
                l = (Love)o;
                viewHolder.getTextViewId(R.id.love_tv).setText(l.getLove());
            }
        };
        myAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                l =  (Love)myAdapter.getItemPosition(position);
                Intent intent = new Intent(LoveActivity.this, BuyActivity.class);
                intent.putExtra("food", l.getLove());
                startActivity(intent);
            }

            /**
             * @param v
             * @param position
             */
            @Override
            public void onItemLongClick(View v, int position) {
                l =  (Love)myAdapter.getItemPosition(position);
                Dialog dialog = new AlertDialog.Builder(LoveActivity.this).setTitle("取消收藏")
                        .setIcon(R.drawable.love)
                        .setMessage("是否取消收藏？")
                        .setPositiveButton("确定",  new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               net.getDeleteLoveInfo(l.getId()).enqueue(new Callback<String>() {
                                   @Override
                                   public void onResponse(Call<String> call, Response<String> response) {
                                       Toast.makeText(getApplicationContext(),response.body().trim(),Toast.LENGTH_SHORT).show();
                                       finish();
                                   }

                                   @Override
                                   public void onFailure(Call<String> call, Throwable t) {

                                   }
                               });
                            }
                        })
                        .setNeutralButton("取消", null)
                        .create();dialog.show();
            }
        });
    }
}
