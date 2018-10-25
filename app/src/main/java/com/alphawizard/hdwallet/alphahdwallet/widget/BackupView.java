package com.alphawizard.hdwallet.alphahdwallet.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.alphawizard.hdwallet.alphahdwallet.R;


public class BackupView extends FrameLayout {
    private EditText password;
    private int mLayoutId;
    public BackupView(@NonNull Context context) {
        super(context);
        mLayoutId = R.layout.layout_dialog_backup;
        init();
    }

    public BackupView(@NonNull Context context,int layoutId) {
        super(context);
        mLayoutId = layoutId;
        init();
    }

    private void init() {
        LayoutInflater.from(getContext())
                .inflate(R.layout.layout_dialog_backup, this, true);
        password = findViewById(R.id.password);
    }

    public String getPassword() {
        return password.getText().toString();
    }

    public void showKeyBoard() {
        password.requestFocus();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        showKeyBoard();
    }
}
