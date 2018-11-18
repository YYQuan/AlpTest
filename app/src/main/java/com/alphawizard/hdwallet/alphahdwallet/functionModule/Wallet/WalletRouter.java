package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.alphawizard.hdwallet.alphahdwallet.App;


public class WalletRouter {
    public static  final String  RESULT_FOR_TRANSACTION ="RESULT_FOR_TRANSACTION";
    public static  final  int RESULT_CODE_FOR_TRANSACTION = 1;
    public static  final String  OPEN_WALLET_PAGE ="OPEN_WALLET_PAGE";
    public static  final  int WALLET_PAGE_MY_WALLET = 0;


    public void openWalletPage(Context context) {
        Intent intent = new Intent(context, WalletActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(OPEN_WALLET_PAGE,WALLET_PAGE_MY_WALLET);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
//        context.startActivity(intent);
    }

    public void open(Context context) {
        Intent intent = new Intent(context, WalletActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void openTransactionForResult(Activity context,boolean transactionResult) {
        Intent intent = new Intent(context, WalletActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(RESULT_FOR_TRANSACTION,transactionResult);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivityForResult(intent,RESULT_CODE_FOR_TRANSACTION);
//        context.startActivity(intent);
    }
}
