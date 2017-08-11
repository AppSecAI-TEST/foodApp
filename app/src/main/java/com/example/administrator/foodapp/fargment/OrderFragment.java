package com.example.administrator.foodapp.fargment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.administrator.foodapp.R;

import com.example.administrator.foodapp.adapter.BaseAdapter;
import com.example.administrator.foodapp.adapter.BaseViewHolder;

import com.example.administrator.foodapp.bean.AccountOrder;
import com.example.administrator.foodapp.bean.Buy;
import com.example.administrator.foodapp.bean.Order;
import com.example.administrator.foodapp.utils.JsonUtils;
import com.example.administrator.foodapp.utils.NetUtils;
import com.example.administrator.foodapp.utils.TokenUtils;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class OrderFragment extends Fragment {
    @BindView(R.id.toolBar_order)
    Toolbar toolBarOrder;
    Unbinder unbinder;
    @BindView(R.id.order_recyclerView)
    RecyclerView orderRecyclerView;
    private BaseAdapter myAdapter;
    private List<Order> orderList;
    private List<AccountOrder> list = new ArrayList<>();
    private Order o;
    private NetUtils net;
    private String loginName;
    private Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolBarOrder);
        setHasOptionsMenu(true);
        toolBarOrder.setTitle(R.string.order);
        toolBarOrder.setTitleTextColor(getResources().getColor(android.R.color.white));
        loginName = TokenUtils.getCachedToken(getContext());
        if (loginName != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            orderRecyclerView.setLayoutManager(layoutManager);
            Retrofit retrofit = new Retrofit.Builder().baseUrl(NetUtils.URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            net = retrofit.create(NetUtils.class);
            net.getOrderInfo(loginName).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(myAdapter!=null){
                        myAdapter.clear();
                    }
                    orderList = JsonUtils.parseOrderListString(response.body().trim());
                    Message message = new Message();
                    message.what = 1;
                    message.obj = orderList;
                    handler.sendMessage(message);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
            handler = new Handler() {
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 1:
                            orderList = (List<Order>) msg.obj;
                            getInfo(orderList);
                            break;
                    }
                }
            };
        }
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setAdapter(List<AccountOrder> list) {
        myAdapter = new BaseAdapter(getActivity(), list, R.layout.item_order) {
            @Override
            protected void change(BaseViewHolder viewHolder, Object o) {
                AccountOrder accountOrder = (AccountOrder) o;
                viewHolder.getTextViewId(R.id.order_name).setText(accountOrder.getBuy_name());
                viewHolder.getTextViewId(R.id.order_address).setText(accountOrder.getAddress());
                viewHolder.getTextViewId(R.id.order_food_name).setText(accountOrder.getFood());
            }
        };
        myAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
            }

            @Override
            public void onItemLongClick(View v, int position) {

            }
        });
    }

    private int i = 0;

    private void getInfo(final List<Order> oList) {
        if (oList.size() != 0) {
            o = oList.get(i);
            net.getBuyIdInfo(o.getBuy_id()).enqueue(new retrofit2.Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Buy b = JsonUtils.parseBuyString(response.body().trim());
                    list.add(new AccountOrder(b.getAddress(), b.getName(), o.getFood()));
                    Log.i("debug", list.toString());
                    if (i < oList.size() - 1) {
                        i++;
                        getInfo(oList);
                    } else {
                        setAdapter(list);
                        orderRecyclerView.setAdapter(myAdapter);
                        i = 0;
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        }
    }
}