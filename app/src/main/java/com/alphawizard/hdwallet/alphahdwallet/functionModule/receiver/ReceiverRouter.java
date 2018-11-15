package com.alphawizard.hdwallet.alphahdwallet.functionModule.receiver;

import android.content.Context;
import android.content.Intent;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.Launch.LaunchActivity;

public class ReceiverRouter {

    public void open(Context context) {
        Intent intent = new Intent(context, ReceiverActivity.class);
        context.startActivity(intent);
    }
}
