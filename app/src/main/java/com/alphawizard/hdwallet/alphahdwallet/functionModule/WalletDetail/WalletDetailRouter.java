package com.alphawizard.hdwallet.alphahdwallet.functionModule.WalletDetail;

import android.content.Context;
import android.content.Intent;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupMnemonicsActivity;

public class WalletDetailRouter {

    public static  final String  WALLET_DETAIL_STRING  ="WALLET_DETAIL_STRING";


    public void open(Context context,String address) {
        Intent intent = new Intent(context, WalletDetailActivity.class);
        intent.putExtra(WALLET_DETAIL_STRING,address);
        context.startActivity(intent);
    }
}
