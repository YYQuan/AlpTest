package com.alphawizard.hdwallet.alphahdwallet.functionModule.fristLaunch;

import android.content.Context;
import android.content.Intent;

public class FirstLaunchRouter {

    public void open(Context context) {
        open(context,false);
    }


    public void open(Context context,boolean isFirstOpen) {
        Intent intent = new Intent(context, FirstLaunchActivity.class);
        intent.putExtra(FirstLaunchActivity.FIRST_OPEN,isFirstOpen);
        context.startActivity(intent);
    }
}
