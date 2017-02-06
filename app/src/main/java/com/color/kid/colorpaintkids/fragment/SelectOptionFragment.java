package com.color.kid.colorpaintkids.fragment;

import android.view.View;

import com.color.kid.colorpaintkids.R;
import com.color.kid.colorpaintkids.constance.Constants;
import com.color.kid.colorpaintkids.util.FragmentUtil;

import butterknife.OnClick;

/**
 * Created by Tung on 2/3/2017.
 */

public class SelectOptionFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_option;
    }

    @Override
    protected void initView(View root) {

    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.itemAnimal)
    void gotoListAminal(){
        FragmentUtil.pushFragment(getActivity(), SelectItemFragment.newInstance(Constants.AMINAL), null);
    }
}
