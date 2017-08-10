package com.example.administrator.foodapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.foodapp.R;
import com.example.administrator.foodapp.utils.NetUtils;
import com.example.administrator.foodapp.utils.TokenUtils;

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

public class AddAddressActivity extends Activity {
    @BindView(R.id.add_left_img)
    ImageView addLeftImg;
    @BindView(R.id.add_name_edit)
    EditText addNameEdit;
    @BindView(R.id.add_address_edit)
    EditText addAddressEdit;
    @BindView(R.id.add_text_edit)
    EditText addTextEdit;
    @BindView(R.id.add_ok_btn)
    Button addOkBtn;
    private NetUtils net;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addaddress);
        ButterKnife.bind(this);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(NetUtils.URL)
               .addConverterFactory(ScalarsConverterFactory.create())
               // .addConverterFactory(GsonConverterFactory.create())
                .build();
        net = retrofit.create(NetUtils.class);
    }

    @OnClick({R.id.add_left_img, R.id.add_ok_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_left_img:
                finish();
                break;
            case R.id.add_ok_btn:
                String name = addNameEdit.getText().toString().trim();
                String address = addAddressEdit.getText().toString().trim();
                String text = addTextEdit.getText().toString().trim();
                net.getAddBuyInfo(address, TokenUtils.getCachedToken(getApplicationContext()),text,name).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Toast.makeText(getApplicationContext(),response.body().trim(),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddAddressActivity.this,AddressActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
                break;
        }
    }
}
