package com.color.kid.paint.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.color.kid.paint.R;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Tung Nguyen on 12/22/2016.
 */
public class ChoiseFragmentAdapter extends RecyclerView.Adapter<ChoiseFragmentAdapter.ChoiseViewholder> {
    private String[] listData;
    private Context context;
    private String item;

    private OnClickItem onClickItem;

    public ChoiseFragmentAdapter(String[] listData, Context context, String item) {
        this.listData = listData;
        this.context = context;
        this.item = item;
    }

    public void setOnClickItem(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    @Override
    public ChoiseViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChoiseViewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.choiseview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ChoiseViewholder holder, int position) {
        try {
            String filePath = item + "/" + listData[position];
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

    public interface OnClickItem {
        void onClick(String fileName);
    }

    public class ChoiseViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imgItem)
        ImageView imageView;
        private String fileImage;

        public ChoiseViewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }


        public void setData(Drawable drawable, String file) {
            fileImage = file;
            imageView.setImageDrawable(drawable);

        }

        @Override
        public void onClick(View view) {
            if (onClickItem != null){
                onClickItem.onClick(fileImage);
            }
        }
    }

}
