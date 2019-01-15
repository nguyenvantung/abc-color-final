package com.color.kid.coloring.fragment;

import android.view.View;
import android.widget.ImageView;

import com.color.kid.coloring.R;
import com.color.kid.coloring.constance.Constants;
import com.color.kid.coloring.ui.MainActivity;
import com.color.kid.coloring.util.FragmentUtil;
import com.color.kid.coloring.util.SharePreferencesUtil;
import com.color.kid.coloring.util.Util;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
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
        MobileAds.initialize(getActivity(),Constants.ADMOB_ID);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
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
