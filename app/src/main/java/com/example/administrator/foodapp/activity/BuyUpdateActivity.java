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
import com.example.administrator.foodapp.bean.Buy;
import com.example.administrator.foodapp.utils.NetUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2017/8/5.
 */

public class BuyUpdateActivity extends Activity {
    @BindView(R.id.update_left_img)
    ImageView updateLeftImg;
    @BindView(R.id.update_name_edit)
    EditText updateNameEdit;
    @BindView(R.id.update_address_edit)
    EditText updateAddressEdit;
    @BindView(R.id.update_text_edit)
    EditText updateTextEdit;
    @BindView(R.id.update_ok_btn)
    Button updateOkBtn;
    @BindView(R.id.update_delete_img)
    ImageView updateDeleteImg;
    private Intent intent;
    private Buy b;
    private NetUtils net;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_update);
        ButterKnife.bind(this);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(NetUtils.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        net = retrofit.create(NetUtils.class);
        intent = getIntent();
        b = (Buy) intent.getSerializableExtra("b");
        updateAddressEdit.setText(b.getAddress());
        updateNameEdit.setText(b.getName());
        updateTextEdit.setText(b.getText());
    }

    @OnClick({R.id.update_left_img, R.id.update_ok_btn,R.id.update_delete_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.update_left_img:
                finish();
                break;
            case R.id.update_ok_btn:
                String name = updateNameEdit.getText().toString().trim();
                String address = updateAddressEdit.getText().toString().trim();
                String text = updateTextEdit.getText().toString().trim();
                net.getBuyUpdateInfo(b.getId(), address, text, name).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Toast.makeText(getApplicationContext(), response.body().trim(), Toast.LENGTH_SHORT).show();
                        intent = new Intent(BuyUpdateActivity.this, AddressActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
                break;
            case R.id.update_delete_img:
                    net.getBuyDeleteInfo(b.getId()).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Toast.makeText(getApplicationContext(), response.body().trim(), Toast.LENGTH_SHORT).show();
                            intent = new Intent(BuyUpdateActivity.this, AddressActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
