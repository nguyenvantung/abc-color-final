package com.color.kid.colorpaintkids.fragment;

import android.view.View;

import com.color.kid.colorpaintkids.R;
import com.color.kid.colorpaintkids.constance.Constants;
import com.color.kid.colorpaintkids.util.FragmentUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Tung on 2/3/2017.
 */

public class SelectOptionFragment extends BaseFragment {
    private InterstitialAd mInterstitialAd;

    @Bind(R.id.adView)
    AdView adView;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_option;
    }

    @Override
    protected void initView(View root) {

    }

    @Override
    protected void initData() {
      showAdView();
    }

    public void showAdView(){
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        /*mNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    //Begin Game, continue with app
                }
            }
        });*/

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {

                //Begin Game, continue with app
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        mInterstitialAd.loadAd(adRequest);
    }

    @OnClick(R.id.itemAnimal)
    void gotoListAminal(){
        FragmentUtil.pushFragment(getActivity(), SelectItemFragment.newInstance(Constants.AMINAL), null);
    }

    @OnClick(R.id.itemCar)
    void gotoListCar(){
        FragmentUtil.pushFragment(getActivity(), SelectItemFragment.newInstance(Constants.CARS), null);
    }

    @OnClick(R.id.itemFruit)
    void gotoListDinosaur(){
        FragmentUtil.pushFragment(getActivity(), SelectItemFragment.newInstance(Constants.FOOD), null);
    }

    @OnClick(R.id.itemDolphins)
    void gotoListDolphins(){
        FragmentUtil.pushFragment(getActivity(), SelectItemFragment.newInstance(Constants.AMINAL), null);
    }

    @OnClick(R.id.itemFly)
    void gotoListHorse(){
        FragmentUtil.pushFragment(getActivity(), SelectItemFragment.newInstance(Constants.AMINAL), null);
    }


    @OnClick(R.id.itemMickey)
    void gotoListParty(){
        FragmentUtil.pushFragment(getActivity(), SelectItemFragment.newInstance(Constants.MICKEY), null);
    }

    @OnClick(R.id.itemSatan)
    void gotoListSatan(){
        FragmentUtil.pushFragment(getActivity(), SelectItemFragment.newInstance(Constants.AMINAL), null);
    }


    @OnClick(R.id.itemMermaids)
    void gotoListMermaids(){
        FragmentUtil.pushFragment(getActivity(), SelectItemFragment.newInstance(Constants.AMINAL), null);
    }

}
