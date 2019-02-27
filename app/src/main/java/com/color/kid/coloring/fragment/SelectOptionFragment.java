package com.color.kid.coloring.fragment;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.color.kid.coloring.R;
import com.color.kid.coloring.constance.Constants;
import com.color.kid.coloring.ui.MainActivity;
import com.color.kid.coloring.util.FragmentUtil;
import com.color.kid.coloring.util.SharePreferencesUtil;
import com.color.kid.coloring.util.Util;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Tung on 2/3/2017.
 */

public class SelectOptionFragment extends BaseFragment {
    SharePreferencesUtil sharePreferencesUtil;
    @BindView(R.id.itemSound)
    ImageView imgSound;

    @BindView(R.id.adView)
    AdView mAdView;

    private boolean isShowADS;
    private InterstitialAd interstitialAd;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_option;
    }

    @Override
    protected void initView(View root) {
        sharePreferencesUtil = new SharePreferencesUtil(getActivity());
        imgSound.setSelected(sharePreferencesUtil.getSoundPlayed());
    }

    @Override
    protected void initData() {
        showAdView();
    }


    public void showAdView(){
        // Create the InterstitialAd and set the adUnitId.
        interstitialAd = new InterstitialAd(getActivity());
        // Defined in res/values/strings.xml
        interstitialAd.setAdUnitId(Constants.ADMOB_UNIT_ID);
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                interstitialAd.show();
            }
        });
    }

    @OnClick(R.id.itemAnimal)
    void gotoListAminal(){
        isShowADS = true;
        FragmentUtil.pushFragment(getActivity(), SelectItemFragment.newInstance(Constants.AMINAL), null);
        Util.playSong(getActivity(), R.raw.z_textures_menu);
    }

    @OnClick(R.id.itemCar)
    void gotoListCar(){
        isShowADS = true;
        FragmentUtil.pushFragment(getActivity(), SelectItemFragment.newInstance(Constants.CARS), null);
        Util.playSong(getActivity(), R.raw.z_textures_menu);
    }

    @OnClick(R.id.itemFruit)
    void gotoListDinosaur(){
        isShowADS = true;
        FragmentUtil.pushFragment(getActivity(), SelectItemFragment.newInstance(Constants.FOOD), null);
        Util.playSong(getActivity(), R.raw.z_textures_menu);
    }


    @OnClick(R.id.itemMickey)
    void gotoListParty(){
        isShowADS = true;
        FragmentUtil.pushFragment(getActivity(), SelectItemFragment.newInstance(Constants.MICKEY), null);
        Util.playSong(getActivity(), R.raw.z_textures_menu);
    }

    @OnClick(R.id.itemSatan)
    void gotoListSatan(){
        isShowADS = true;
        FragmentUtil.pushFragment(getActivity(), SelectItemFragment.newInstance(Constants.SANTA), null);
        Util.playSong(getActivity(), R.raw.z_textures_menu);
    }


    @OnClick(R.id.itemMermaids)
    void gotoListMermaids(){
        isShowADS = true;
        FragmentUtil.pushFragment(getActivity(), SelectItemFragment.newInstance(Constants.MERMAIDS), null);
        Util.playSong(getActivity(), R.raw.z_textures_menu);
    }

    @OnClick(R.id.itemSound)
    void onPlaySound(){
        if (imgSound.isSelected()){
            ((MainActivity)getActivity()).playSound(false);
            sharePreferencesUtil.setSoundPlayed(false);
            imgSound.setSelected(false);
        }else {
            ((MainActivity)getActivity()).playSound(true);
            sharePreferencesUtil.setSoundPlayed(true);
            imgSound.setSelected(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isShowADS){
            isShowADS = false;
            showInterstitial();
        }
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (interstitialAd != null && interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }
}
