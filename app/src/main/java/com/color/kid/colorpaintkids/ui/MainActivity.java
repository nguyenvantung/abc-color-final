package com.color.kid.colorpaintkids.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.color.kid.colorpaintkids.R;
import com.color.kid.colorpaintkids.fragment.ColorFragment;
import com.color.kid.colorpaintkids.util.FragmentUtil;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentUtil.replaceFragment(this, new ColorFragment(), null);
    }

}
