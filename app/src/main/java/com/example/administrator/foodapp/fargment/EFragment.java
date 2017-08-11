package com.example.administrator.foodapp.fargment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidkun.PullToRefreshRecyclerView;
import com.androidkun.callback.PullToRefreshListener;
import com.bumptech.glide.Glide;
import com.example.administrator.foodapp.R;
import com.example.administrator.foodapp.activity.BuyActivity;
import com.example.administrator.foodapp.adapter.BaseAdapter;
import com.example.administrator.foodapp.adapter.BaseViewHolder;
import com.example.administrator.foodapp.bean.Food;
import com.example.administrator.foodapp.utils.NetUtils;
import com.example.administrator.foodapp.utils.PullXML;
import com.example.administrator.foodapp.utils.TokenUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class EFragment extends Fragment {
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    Unbinder unbinder;
    @BindView(R.id.e_pullToRefreshRV)
    PullToRefreshRecyclerView ePullToRefreshRV;

    private File cache;
    private BaseAdapter myAdapter;
    private List<Food> list;
    private Food f;
    private NetUtils net;
    private boolean login = false;//是否登录
    private File localFile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_e, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolBar);
        setHasOptionsMenu(true);
        toolBar.setTitle(R.string.app_name);
        toolBar.setTitleTextColor(getResources().getColor(android.R.color.white));
        if (TokenUtils.getCachedToken(getContext()) != null) {//获取登录状态
            login = true;
        }
        cache = new File(Environment.getExternalStorageDirectory(), "cache");
        if (!cache.exists()) {
            cache.mkdirs();// 创建缓存文件夹
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ePullToRefreshRV.setLayoutManager(layoutManager);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        eRecycler.setLayoutManager(layoutManager);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(NetUtils.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        net = retrofit.create(NetUtils.class);
        Log.d("debug", NetUtils.URL + "/foodApp/food/food.xml");
        getXmlInfo();
        //是否开启下拉刷新功能
        ePullToRefreshRV.setPullRefreshEnabled(true);
        //设置是否显示上次刷新的时间
        ePullToRefreshRV.displayLastRefreshTime(true);
        //是否开启上拉加载功能
        ePullToRefreshRV.setLoadingMoreEnabled(true);
        //设置刷新回调
        ePullToRefreshRV.setPullToRefreshListener(new PullToRefreshListener() {
            @Override
            public void onRefresh() {
                ePullToRefreshRV.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("refresh","====");
                        getXmlInfo();
                        myAdapter.notifyDataSetChanged();
                        ePullToRefreshRV.setRefreshComplete();
                    }
                },3000);

            }

            @Override
            public void onLoadMore() {
                ePullToRefreshRV.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("onLoadMore","----------");
                        myAdapter.notifyDataSetChanged();
                        ePullToRefreshRV.setLoadMoreComplete();
                    }
                },3000);

            }
        });
        return view;
    }

    /**
     * 获取后台xml数据
     */
    private void getXmlInfo() {
        final PullXML pullXML = new PullXML(list, f);
        Call<ResponseBody> call = net.downloadFile(NetUtils.URL + "/foodApp/food/food.xml");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    InputStream is = new ByteArrayInputStream(response.body().string().getBytes());
                    list = pullXML.getNewsListFromInputStream(is);
                    Log.d("debug", list.toString());
                    setAdapter(list);
                    // eRecycler.setAdapter(myAdapter);
                    ePullToRefreshRV.setAdapter(myAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("debug", "error");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * @param list  适配器
     */
    private void setAdapter(List<Food> list) {

        myAdapter = new BaseAdapter(getActivity(), list, R.layout.item_e) {
            @Override
            protected void change(BaseViewHolder viewHolder, Object o) {
                f = (Food) o;
                final ImageView foodImg = viewHolder.getImageView(R.id.food_img);
                Log.d("debug", NetUtils.URL + "/foodApp/" + f.getImg());
                final String path = NetUtils.URL + "/foodApp/" + f.getImg();
                localFile = new File(cache, path.substring(path
                        .lastIndexOf(File.separator) + 1));
                if (localFile.exists()) {
                    Glide.with(getContext()).load(Uri.fromFile(localFile)).override(60, 60).into(foodImg);
                } else {
                    Observable.create(new ObservableOnSubscribe<Uri>() {
                        @Override
                        public void subscribe(@NonNull ObservableEmitter<Uri> e) throws Exception {
                            FileOutputStream outStream = new FileOutputStream(localFile);
                            InputStream input = net.downloadFile(path).execute().body().byteStream();
                            BufferedInputStream bis = new BufferedInputStream(input);
                            byte[] buffer = new byte[1024];
                            int len = -1;
                            while ((len = bis.read(buffer)) != -1) {
                                outStream.write(buffer, 0, len);
                            }
                            input.close();
                            outStream.close();
                            Uri uri = Uri.fromFile(localFile);
                            e.onNext(uri);
                        }
                    }).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<Uri>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {

                                }

                                @Override
                                public void onNext(@NonNull Uri uri) {
                                    Log.d("debug", "-------------------" + uri);
                                    Glide.with(getContext()).load(uri).override(60, 60).into(foodImg);
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onComplete() {
                                }
                            });
                }
                //     Glide.with(getContext()).load(LocalFile.getImage( NetUtils.URL + "/foodApp/" + f.getImg())).override(60, 60).into(foodImg);
                viewHolder.getTextViewId(R.id.food_title).setText(f.getTitle());
                viewHolder.getTextViewId(R.id.food_cost).setText(f.getCost());
            }
        };
        myAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if (login) {
                    f = (Food) myAdapter.getItemPosition(position);
                    Intent intent = new Intent(getActivity(), BuyActivity.class);
                    intent.putExtra("food", f.getTitle());
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(View v, int position) {
                Log.i("ddd",position+"");
                f = (Food) myAdapter.getItemPosition(position);
                Dialog dialog = new AlertDialog.Builder(getActivity()).setTitle("收藏")
                        .setIcon(R.drawable.love)
                        .setMessage("是否收藏？")
                        .setPositiveButton("确定",  new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                net.getAddLoveInfo(TokenUtils.getCachedToken(getContext()),f.getTitle()).enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        Toast.makeText(getContext(),response.body().trim(),Toast.LENGTH_SHORT).show();
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