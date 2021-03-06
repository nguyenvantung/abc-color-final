package com.color.kid.paint.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.color.kid.paint.R;
import com.color.kid.paint.adapter.viewHolder.ColorViewHolder;
import com.color.kid.paint.constance.ConstantSource;

/**
 * Created by TungDaiCa on 3/8/2017.
 */

public class OptionColorAdapter extends RecyclerView.Adapter<ColorViewHolder>{

    private ColorViewHolder.SelectItemColor itemColor;
    public OptionColorAdapter (){
    }

    public void setSelectItemColor(ColorViewHolder.SelectItemColor selectItemColor){
        itemColor = selectItemColor;
    }

    @Override
    public ColorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ColorViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_option_color, parent, false));
    }

    @Override
    public void onBindViewHolder(ColorViewHolder holder, int position) {
        holder.setData(ConstantSource.listColorTool[position]);
        holder.setSelectItemColor(itemColor);
    }

    @Override
    public int getItemCount() {
        return ConstantSource.listColorTool.length;
    }
}
