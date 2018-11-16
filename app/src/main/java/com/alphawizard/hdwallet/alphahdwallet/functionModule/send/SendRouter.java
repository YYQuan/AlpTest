package com.alphawizard.hdwallet.alphahdwallet.functionModule.send;

import android.content.Context;
import android.content.Intent;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletActivity;

public class SendRouter {

    public static final  String  WALLET_BALANCE = "WALLET_BALANCE";
    public void open(Context context,float value) {
        Intent intent = new Intent(context, SendActivity.class);
        intent.putExtra(WALLET_BALANCE,value);
        context.startActivity(intent);
    }

}
