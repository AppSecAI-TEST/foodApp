package com.example.administrator.foodapp.fargment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.administrator.foodapp.R;
import com.example.administrator.foodapp.activity.AddressActivity;
import com.example.administrator.foodapp.activity.LoveActivity;
import com.example.administrator.foodapp.activity.MainActivity;
import com.example.administrator.foodapp.activity.SetActivity;
import com.example.administrator.foodapp.bean.Account;
import com.example.administrator.foodapp.utils.GlideUtils;
import com.example.administrator.foodapp.utils.JsonUtils;
import com.example.administrator.foodapp.utils.NetUtils;
import com.example.administrator.foodapp.utils.TokenUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AccountFragment extends Fragment {
    @BindView(R.id.toolBar_account)
    Toolbar toolBarAccount;
    Unbinder unbinder;
    @BindView(R.id.account_relative)
    RelativeLayout accountRelative;
    @BindView(R.id.address_relative)
    RelativeLayout addressRelative;
    @BindView(R.id.love_relative)
    RelativeLayout loveRelative;
    @BindView(R.id.account_linear)
    LinearLayout accountLinear;
    @BindView(R.id.loginName_tv)
    TextView loginNameTv;
    @BindView(R.id.phone_tv)
    TextView phoneTv;
    @BindView(R.id.head_img)
    ImageView headImg;
    private NetUtils net;
    private boolean login = false;
    private Account a;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolBarAccount);
        setHasOptionsMenu(true);
        toolBarAccount.setTitle(R.string.account);
        toolBarAccount.setTitleTextColor(getResources().getColor(android.R.color.white));
        Retrofit retrofit = new Retrofit.Builder().baseUrl(NetUtils.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                //.addConverterFactory(GsonConverterFactory.create())
                .build();
        net = retrofit.create(NetUtils.class);
        System.out.println(TokenUtils.getCachedToken(getActivity()));
        if (TokenUtils.getCachedToken(getActivity()) != null) {
            login = true;
            net.getAccountInfo(TokenUtils.getCachedToken(getActivity())).enqueue(new Callback<String>() {
                /**
                 * @param call
                 * @param response
                 */
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    a = JsonUtils.parseAccountString(response.body());
                    System.out.println(a);
                    loginNameTv.setText(a.getName());
                    phoneTv.setText(a.getPhone());
                    String path = NetUtils.URL + "/foodApp/image/" + a.getPicture();
                    Log.i("debug", path);
                    GlideUtils.roundPicture(getActivity(), path, headImg);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        } else {
            login = false;
            loginNameTv.setText(R.string.name);
            phoneTv.setText(R.string.phone);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.account_relative, R.id.address_relative, R.id.love_relative})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.account_relative:
                if (!login) {
                    final View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindow_login, null);
                    final PopupWindow mPopWindow = new PopupWindow(contentView);
                    mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                    mPopWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                    mPopWindow.setFocusable(true);
                    ColorDrawable dw = new ColorDrawable(0x00000000);
                    mPopWindow.setBackgroundDrawable(dw);
                    mPopWindow.showAtLocation(accountLinear, Gravity.CENTER, 0, 0);
                    contentView.findViewById(R.id.register_tv).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            register(contentView);
                        }
                    });
                    contentView.findViewById(R.id.login_ok).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            login(contentView, mPopWindow);
                        }
                    });
                } else {
                    Intent set_intent = new Intent(getActivity(), SetActivity.class);
                    set_intent.putExtra("loginName", a.getName());
                    set_intent.putExtra("loginPhone", a.getPhone());
                    set_intent.putExtra("loginPicture", a.getPicture());
                    startActivity(set_intent);
                }
                break;
            case R.id.address_relative:
                if (login) {
                    Intent buy_intent = new Intent(getActivity(), AddressActivity.class);
                    startActivity(buy_intent);
                }
                break;
            case R.id.love_relative:
                if (login) {
                    Intent buy_intent = new Intent(getActivity(), LoveActivity.class);
                    startActivity(buy_intent);
                }
                break;
        }
    }

    private void login(View contentView, final PopupWindow mPopWindow) {
        final String loginName = ((EditText) contentView.findViewById(R.id.login_name_et)).getText().toString().trim();
        final String loginPassword = ((EditText) contentView.findViewById(R.id.login_password_et)).getText().toString().trim();
        net.getLoginInfo(loginName, loginPassword).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(getActivity(), response.body().toString().trim(), Toast.LENGTH_SHORT).show();
                TokenUtils.cachedToken(getActivity(), loginName);
                Log.d("debug", TokenUtils.getCachedToken(getActivity()));
                if ("登录成功".equals(response.body().toString().trim())) {
                    mPopWindow.dismiss();
                    login = true;
                    Intent login_intent = new Intent(getActivity(), MainActivity.class);
                    login_intent.putExtra("login", login);
                    startActivity(login_intent);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void register(View contentView) {
        String loginName = ((EditText) contentView.findViewById(R.id.login_name_et)).getText().toString().trim();
        String loginPassword = ((EditText) contentView.findViewById(R.id.login_password_et)).getText().toString().trim();
        net.getRegisterInfo(loginName, loginPassword, "暂无", "暂无").enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(getActivity(), response.body().toString().trim(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


}