package com.color.kid.colorpaintkids.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.color.kid.colorpaintkids.R;
import com.color.kid.colorpaintkids.util.Util;


/**
 * Created by Tung on 3/10/2017.
 */

public class DialogShareImage extends Dialog {

    public DialogShareImage(Context context, final Bitmap bitmap, final ShareCallBack shareCallBack) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_share);
        Typeface font1 = Typeface.createFromAsset(context.getAssets(), "fonts/cooper_black.ttf");
        TextView tvTitle = (TextView) findViewById(R.id.titleShare);
        tvTitle.setTypeface(font1);
        ImageView imgCancel = (ImageView)findViewById(R.id.buttonCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                shareCallBack.onCallBackDialog(false);
            }
        });

        ImageView imgButtonShare = (ImageView)findViewById(R.id.buttonShare);
        imgButtonShare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                shareCallBack.onCallBackDialog(true);
                dismiss();
            }
        });

        ImageView imgShare = (ImageView)findViewById(R.id.imgShare);
        imgShare.setImageBitmap(bitmap);

    }

    public interface ShareCallBack{
        void onCallBackDialog(boolean select);
    }
}
