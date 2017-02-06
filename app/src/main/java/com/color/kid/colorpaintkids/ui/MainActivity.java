package com.color.kid.colorpaintkids.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.color.kid.colorpaintkids.R;
import com.color.kid.colorpaintkids.fragment.SplashFragment;
import com.color.kid.colorpaintkids.util.FragmentUtil;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentUtil.pushFragment(this, new SplashFragment(), null);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            super.onBackPressed(); //replaced
        }
    }
}
