package com.color.kid.paint.adapter.viewHolder;

import android.graphics.PorterDuff;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.color.kid.paint.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tung on 3/9/2017.
 */

public class ColorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    @BindView(R.id.itemColor)
    ImageView imgColor;
    private SelectItemColor selectItemColor;
    private int color;

    public void setSelectItemColor(SelectItemColor selectItemColor){
        this.selectItemColor = selectItemColor;
    }

    public ColorViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    public void setData(int color){
        this.color = color;
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            imgColor.setColorFilter(ContextCompat.getColor(itemView.getContext(), color),PorterDuff.Mode.MULTIPLY);
        }else {
            imgColor.setColorFilter(itemView.getContext().getResources().getColor(color), PorterDuff.Mode.MULTIPLY);
        }

    }

    @Override
    public void onClick(View v) {
        if (selectItemColor != null){
            selectItemColor.onSelectColor(color);
        }
    }

    public interface SelectItemColor{
        void onSelectColor(int color);
    }
}
