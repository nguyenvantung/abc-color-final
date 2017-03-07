package com.color.kid.colorpaintkids.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.color.kid.colorpaintkids.R;
import com.color.kid.colorpaintkids.adapter.ChoiseFragmentAdapter;
import com.color.kid.colorpaintkids.constance.ConstantSource;
import com.color.kid.colorpaintkids.constance.Constants;
import com.color.kid.colorpaintkids.ui.MainActivity;
import com.color.kid.colorpaintkids.view.ItemOffsetDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Tung on 2/3/2017.
 */

public class SelectItemFragment extends BaseFragment {

    @Bind(R.id.titleOption)
    TextView tvTitle;

    @Bind(R.id.listItem)
    RecyclerView recyclerView;

    private int option;
    private int[] listDataItem ;

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
        setTitle(option);
        ChoiseFragmentAdapter adapter = new ChoiseFragmentAdapter(getDataList(option));
        recyclerView.setAdapter(adapter);
    }

    public void setTitle(int option){
        switch (option){
            case Constants.AMINAL:
                tvTitle.setText("Aminal");
                break;
            default:
                tvTitle.setText("Aminal");
        }
    }

    private int[] getDataList(int position){
        int[] integerList = null;
        switch (position){
            case Constants.AMINAL:
                integerList = ConstantSource.listAminalDraw;
                break;
            case Constants.CARS:
                integerList = ConstantSource.listCarDraw;
                break;

        }
        return integerList;
    }



}
