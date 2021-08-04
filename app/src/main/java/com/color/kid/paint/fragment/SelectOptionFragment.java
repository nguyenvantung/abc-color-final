package com.color.kid.paint.fragment;

import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.color.kid.paint.R;
import com.color.kid.paint.constance.Constants;
import com.color.kid.paint.ui.MainActivity;
import com.color.kid.paint.util.FragmentUtil;
import com.color.kid.paint.util.SharePreferencesUtil;
import com.color.kid.paint.util.Util;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Tung on 2/3/2017.
 */

public class SelectOptionFragment extends BaseFragment {
    SharePreferencesUtil sharePreferencesUtil;

    @BindView(R.id.adView)
    AdView mAdView;

    @BindView(R.id.title)
    TextView tvTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_option;
    }

    @Override
    protected void initView(View root) {
        sharePreferencesUtil = new SharePreferencesUtil(getActivity());
        Typeface font1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/cooper_black.ttf");
        tvTitle.setTypeface(font1);
    }

    @Override
    protected void initData() {
        showAdViewBanner();
    }

    private void showAdViewBanner(){
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.e("load ads", "onAdFailedToLoad:" + errorCode);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }

    @OnClick(R.id.itemAnimal)
    void gotoListAminal(){
        FragmentUtil.pushFragment(getActivity(), SelectItemFragment.newInstance(Constants.AMINAL), null);
        Util.playSong(getActivity(), R.raw.z_textures_menu);
    }

    @OnClick(R.id.itemCar)
    void gotoListCar(){
        FragmentUtil.pushFragment(getActivity(), SelectItemFragment.newInstance(Constants.CARS), null);
        Util.playSong(getActivity(), R.raw.z_textures_menu);
    }

    @OnClick(R.id.itemFruit)
    void gotoListDinosaur(){
        FragmentUtil.pushFragment(getActivity(), SelectItemFragment.newInstance(Constants.FOOD), null);
        Util.playSong(getActivity(), R.raw.z_textures_menu);
    }


    @OnClick(R.id.itemMickey)
    void gotoListParty(){
        FragmentUtil.pushFragment(getActivity(), SelectItemFragment.newInstance(Constants.MICKEY), null);
        Util.playSong(getActivity(), R.raw.z_textures_menu);
    }

    @OnClick(R.id.itemSatan)
    void gotoListSatan(){
        FragmentUtil.pushFragment(getActivity(), SelectItemFragment.newInstance(Constants.SANTA), null);
        Util.playSong(getActivity(), R.raw.z_textures_menu);
    }


    @OnClick(R.id.itemMermaids)
    void gotoListMermaids(){
        FragmentUtil.pushFragment(getActivity(), SelectItemFragment.newInstance(Constants.MERMAIDS), null);
        Util.playSong(getActivity(), R.raw.z_textures_menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }


}
