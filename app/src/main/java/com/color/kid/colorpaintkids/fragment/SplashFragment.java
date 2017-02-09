package com.color.kid.colorpaintkids.fragment;

import android.os.Handler;
import android.view.View;

import com.color.kid.colorpaintkids.R;
import com.color.kid.colorpaintkids.util.FragmentUtil;

/**
 * Created by TungDaiCa on 2/6/2017.
 */

public class SplashFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView(View root) {

    }

    @Override
    protected void initData() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextScreen();
            }
        }, 2000);
    }

    public void nextScreen() {
        FragmentUtil.replaceFragment(getActivity(), new SelectOptionFragment(), null);
    }
}