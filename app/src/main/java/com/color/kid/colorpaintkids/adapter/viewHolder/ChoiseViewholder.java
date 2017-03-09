package com.color.kid.colorpaintkids.adapter.viewHolder;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;


import com.color.kid.colorpaintkids.R;
import com.color.kid.colorpaintkids.constance.Constants;
import com.color.kid.colorpaintkids.ui.ColorActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Tung Nguyen on 12/22/2016.
 */
public class ChoiseViewholder extends RecyclerView.ViewHolder implements View.OnClickListener{
    @Bind(R.id.imgItem)
    ImageView imageView;
    private int drawableData;

    public ChoiseViewholder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    public void setData(int drawable) {
        drawableData = drawable;
        imageView.setImageDrawable(itemView.getContext().getDrawable(drawable));
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent((FragmentActivity)itemView.getContext(), ColorActivity.class);
        intent.putExtra(Constants.KEY_DRAWABLE, drawableData);
        itemView.getContext().startActivity(intent);
    }
}
