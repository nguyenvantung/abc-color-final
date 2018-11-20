package com.color.kid.colorpaintkids.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.color.kid.colorpaintkids.R;
import com.color.kid.colorpaintkids.adapter.ChoiseFragmentAdapter;
import com.color.kid.colorpaintkids.constance.Constants;
import com.color.kid.colorpaintkids.view.ItemOffsetDecoration;

import java.io.IOException;

import butterknife.BindView;

/**
 * Created by Tung on 2/3/2017.
 */

public class SelectItemFragment extends BaseFragment {

    @BindView(R.id.titleOption)
    TextView tvTitle;

    @BindView(R.id.listItem)
    RecyclerView recyclerView;

    private int option;
    private String item = "";

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



}
