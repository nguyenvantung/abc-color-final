package com.color.kid.coloring.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.color.kid.colorpaintkids.R;
import com.color.kid.coloring.adapter.viewHolder.ChoiseViewholder;

import java.io.IOException;
import java.io.InputStream;


/**
 * Created by Tung Nguyen on 12/22/2016.
 */
public class ChoiseFragmentAdapter extends RecyclerView.Adapter<ChoiseViewholder>{
    private String[] listData ;
    private Context context;
    private String item;

    public ChoiseFragmentAdapter(String[] listData, Context context, String item){
        this.listData = listData;
        this.context = context;
        this.item = item;
    }

    @Override
    public ChoiseViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChoiseViewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.choiseview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ChoiseViewholder holder, int position) {
        try {
            String filePath = item + "/"+listData[position];
            InputStream inputStream = context.getAssets().open(filePath);
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            holder.setData(drawable, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return listData.length;
    }
}
