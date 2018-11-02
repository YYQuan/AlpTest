package com.alphawizard.hdwallet.alphahdwallet.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.alphawizard.hdwallet.alphahdwallet.R;


public class BackupView extends FrameLayout {

    private ImageView  mAddresssCode;
    private int mLayoutId;
    Bitmap mBitmap;
    public BackupView(@NonNull Context context ,Bitmap bitmap) {
        super(context);
        mLayoutId = R.layout.layout_dialog_backup;
        mBitmap = bitmap;
        init();
    }

    public BackupView(@NonNull Context context,int layoutId) {
        super(context);
        mLayoutId = layoutId;
        init();
    }

    private void init() {
        LayoutInflater.from(getContext())
                .inflate(mLayoutId, this, true);
        mAddresssCode = findViewById(R.id.iv_address_code);

        mAddresssCode.setImageBitmap(mBitmap);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }
}
