package com.example.administrator.foodapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.administrator.foodapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends Activity {

    @BindView(R.id.welcome_image)
    ImageView welcomeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去除标题
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        AlphaAnimation animation = new AlphaAnimation(0.5f, 1.0f);
        animation.setDuration(2000);
        animation.setStartOffset(500);
        animation.setAnimationListener(new RemoveAnimation());
        welcomeImage.startAnimation(animation);
    }


    private class RemoveAnimation implements Animation.AnimationListener {

        //执行完动画后调用
        public void onAnimationEnd(Animation arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent();
            intent.setClass(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            WelcomeActivity.this.finish();
        }

        @Override
        public void onAnimationRepeat(Animation arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationStart(Animation arg0) {
            // TODO Auto-generated method stub

        }

    }

}