package com.example.administrator.foodapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.foodapp.R;
import com.example.administrator.foodapp.utils.GlideUtils;
import com.example.administrator.foodapp.utils.NetUtils;
import com.example.administrator.foodapp.utils.TokenUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2017/8/2.
 */

public class SetActivity extends Activity {
    @BindView(R.id.setHead_img)
    ImageView setHeadImg;
    @BindView(R.id.setName_tv)
    TextView setNameTv;
    @BindView(R.id.setPhone_tv)
    TextView setPhoneTv;
    @BindView(R.id.left_img)
    ImageView leftImg;
    @BindView(R.id.phone_relative)
    RelativeLayout phoneRelative;
    @BindView(R.id.exit_relative)
    RelativeLayout exitRelative;
    private Intent intent;
    private NetUtils net;
    private Uri uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        ButterKnife.bind(this);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(NetUtils.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        net = retrofit.create(NetUtils.class);
        intent = getIntent();
        String name = intent.getStringExtra("loginName");
        String phone = intent.getStringExtra("loginPhone");
        String picture = intent.getStringExtra("loginPicture");
        setNameTv.setText(name);
        setPhoneTv.setText(phone);
        String path = NetUtils.URL + "/foodApp/image/" + picture;
        GlideUtils.roundPicture(getApplicationContext(), path, setHeadImg);
    }

    @OnClick(R.id.setHead_img)
    public void onViewClicked() {
    }

    @OnClick({R.id.left_img, R.id.setHead_img, R.id.phone_relative, R.id.exit_relative})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_img:
                finish();
                break;
            case R.id.setHead_img:
                Intent intent = new Intent();
            /* 开启Pictures画面Type设定为image */
                intent.setType("image/*");
            /* 使用Intent.ACTION_GET_CONTENT这个Action */
                intent.setAction(Intent.ACTION_GET_CONTENT);
            /* 取得相片后返回本画面 */
                startActivityForResult(intent, 1);
                break;
            case R.id.phone_relative:
                final EditText inputServer = new EditText(this);
                inputServer.setInputType(InputType.TYPE_CLASS_PHONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("修改手机号").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String phone = inputServer.getText().toString();
                        net.getAccountPhone(TokenUtils.getCachedToken(getApplicationContext()), phone).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Toast.makeText(getApplicationContext(), response.body().trim(), Toast.LENGTH_SHORT).show();
                                Intent phone_intent = new Intent(SetActivity.this, MainActivity.class);
                                phone_intent.putExtra("login", true);
                                startActivity(phone_intent);
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                    }
                });
                builder.show();
                break;
            case R.id.exit_relative:
                TokenUtils.cachedToken(getApplicationContext(), null);
                Intent exit=new Intent(this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(exit);
                break;
        }
    }

    /**
     * 返回后调用该方法获得相片路径
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case 1:
                uri = data.getData();
                startImageZoom(uri);
                break;
            case 2:
                if (data == null) {
                    return;
                } else {
                    String path = getRealPathFromUri(this, data.getData());
                    Log.i("debug", path);
                    GlideUtils.roundPicture(getApplicationContext(), path, setHeadImg);
                    upload(path);
                }
                break;
        }
    }

    public void upload(String path) {
        File file = new File(path);
        Log.i("debug", "------------" + file);
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        net.getAccountPicture(TokenUtils.getCachedToken(getApplicationContext()), requestBody).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(getApplicationContext(), response.body().trim(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    /**
     * 通过Uri传递图像信息以供裁剪
     *
     * @param uri
     */
    private void startImageZoom(Uri uri) {
        //构建隐式Intent来启动裁剪程序
        Intent intent = new Intent("com.android.camera.action.CROP");
        //设置数据uri和类型为图片类型
        intent.setDataAndType(uri, "image/*");
        //显示View为可裁剪的
        intent.putExtra("crop", true);
        //裁剪的宽高的比例为1:1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //输出图片的宽高均为150
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        //裁剪之后的数据是通过Intent返回
        intent.putExtra("return-data", false);
        startActivityForResult(intent, 2);
    }

    /**
     * 将content://media/external/images/media/转为/storage/sdcard/xxx
     */
    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null,
                    null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
