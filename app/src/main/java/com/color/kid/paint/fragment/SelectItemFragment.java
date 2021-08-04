package com.color.kid.paint.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.color.kid.paint.constance.Constants;
import com.color.kid.paint.R;
import com.color.kid.paint.adapter.ChoiseFragmentAdapter;
import com.color.kid.paint.ui.ColorActivity;
import com.color.kid.paint.util.Util;
import com.color.kid.paint.view.ItemOffsetDecoration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.IOException;

import butterknife.BindView;

/**
 * Created by Tung on 2/3/2017.
 */

public class SelectItemFragment extends BaseFragment {

    @BindView(R.id.title)
    TextView tvTitle;

    @BindView(R.id.listItem)
    RecyclerView recyclerView;

    @BindView(R.id.bannerAds)
    AdView adView;

    private int option;
    private String item = "";

    private boolean isShowADS = false;
    private int isShowFirst = 0;
    private InterstitialAd interstitialAd;

    public static SelectItemFragment newInstance(int option){
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.KEY_OPTION, option);
        SelectItemFragment selectItemFragment = new SelectItemFragment();
        selectItemFragment.setArguments(bundle);
        return selectItemFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_choise;
    }

    @Override
    protected void initView(View root) {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_image);
        recyclerView.addItemDecoration(itemDecoration);
        Typeface font1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/cooper_black.ttf");
        tvTitle.setTypeface(font1);
    }

    @Override
    protected void initData() {
        option = this.getArguments().getInt(Constants.KEY_OPTION);
        ChoiseFragmentAdapter adapter = new ChoiseFragmentAdapter(getDataList(option), getActivity(), item);
        recyclerView.setAdapter(adapter);
        adapter.setOnClickItem(fileName -> {
            Intent intent = new Intent(getActivity(), ColorActivity.class);
            intent.putExtra(Constants.KEY_DRAWABLE, fileName);
            startActivity(intent);
            Util.playSong(getActivity(), R.raw.sf_0);
            isShowADS = true;
        });
        setUpAdmob();
        showAdViewFull();
    }

    public void showAdViewFull(){
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
                if (isShowFirst > 0){
                    interstitialAd.show();
                }
            }

        });
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (interstitialAd != null && interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }

    private String[] getDataList(int position){
        String[] integerList = null;
        switch (position){
            case Constants.AMINAL:
                item = "animal";
                integerList = getFileItem("animal");
                tvTitle.setText("Aminal");
                break;
            case Constants.CARS:
                item = "car";
                integerList = getFileItem("car");
                tvTitle.setText("Cars");
                break;
            case Constants.FOOD:
                item = "food";
                integerList = getFileItem("food");
                tvTitle.setText("Food");
                break;
            case Constants.MICKEY:
                item = "mickey";
                integerList = getFileItem("mickey");
                tvTitle.setText("Mickey");
                break;
            case Constants.MERMAIDS:
                item = "princesses";
                integerList = getFileItem("princesses");
                tvTitle.setText("People");
                break;
            case Constants.SANTA:
                item = "santa";
                integerList = getFileItem("santa");
                tvTitle.setText("Santa");
                break;
            default:
                tvTitle.setText("Aminal");
                break;


        }
        return integerList;
    }

    public String[] getFileItem(String path){
        String [] list = null;
        try {
            list = getActivity().getAssets().list(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void setUpAdmob(){
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
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


    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (adView != null) {
            adView.resume();
        }
        if (isShowADS){
            isShowADS = false;
            isShowFirst = 1;
            showInterstitial();
        }*/
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

}
