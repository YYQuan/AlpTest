package com.alphawizard.hdwallet.alphahdwallet.functionModule.Import;

import android.content.Context;
import android.content.Intent;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.fristLaunch.FirstLaunchActivity;

public class ImportRouter {

    public void open(Context context) {
        Intent intent = new Intent(context, ImportActivity.class);
        context.startActivity(intent);
    }
}
