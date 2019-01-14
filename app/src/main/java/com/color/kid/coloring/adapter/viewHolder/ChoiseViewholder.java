package com.color.kid.coloring.adapter.viewHolder;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;


import com.color.kid.coloring.constance.Constants;
import com.color.kid.colorpaintkids.R;
import com.color.kid.coloring.ui.ColorActivity;
import com.color.kid.coloring.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tung Nguyen on 12/22/2016.
 */
public class ChoiseViewholder extends RecyclerView.ViewHolder implements View.OnClickListener{
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
        Intent intent = new Intent(itemView.getContext(), ColorActivity.class);
        intent.putExtra(Constants.KEY_DRAWABLE, fileImage);
        itemView.getContext().startActivity(intent);
        Util.playSong(itemView.getContext(), R.raw.sf_0);
    }
}
