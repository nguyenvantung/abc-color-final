package com.color.kid.colorpaintkids.fragment;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;

import com.color.kid.colorpaintkids.R;
import com.color.kid.colorpaintkids.constance.Constants;
import com.color.kid.colorpaintkids.ui.MainActivity;
import com.color.kid.colorpaintkids.util.DebugLog;
import com.color.kid.colorpaintkids.util.FragmentUtil;
import com.color.kid.colorpaintkids.util.SharePreferencesUtil;
import com.color.kid.colorpaintkids.util.Util;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.IOException;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Tung on 2/3/2017.
 */

public class SelectOptionFragment extends BaseFragment {
    SharePreferencesUtil sharePreferencesUtil;
    @Bind(R.id.itemSound)
    ImageView imgSound;

    @Bind(R.id.adView)
    AdView adView;
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
       /* mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(Constants.ADMOB_ID);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });*/
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);
        //mInterstitialAd.loadAd(adRequest);
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

}
