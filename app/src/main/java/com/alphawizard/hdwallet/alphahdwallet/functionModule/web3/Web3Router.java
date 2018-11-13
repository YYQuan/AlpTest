package com.alphawizard.hdwallet.alphahdwallet.functionModule.web3;

import android.content.Context;
import android.content.Intent;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.send.SendActivity;

public class Web3Router {

    public void open(Context context) {
        Intent intent = new Intent(context, Web3Activity.class);
        context.startActivity(intent);
    }

}
