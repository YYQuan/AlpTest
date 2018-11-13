package com.alphawizard.hdwallet.alphahdwallet.functionModule.Launch;

import android.content.Context;
import android.content.Intent;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.CreateOrImport.CreateOrImportActivity;

public class LaunchRouter {

    public void open(Context context) {
        Intent intent = new Intent(context, LaunchActivity.class);
        context.startActivity(intent);
    }
}
