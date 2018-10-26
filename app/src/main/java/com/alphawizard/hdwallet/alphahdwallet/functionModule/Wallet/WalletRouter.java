package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet;

import android.content.Context;
import android.content.Intent;



public class WalletRouter {
    public void open(Context context) {
        Intent intent = new Intent(context, WalletActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
