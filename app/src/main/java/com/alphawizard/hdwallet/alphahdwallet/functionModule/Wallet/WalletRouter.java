package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.alphawizard.hdwallet.alphahdwallet.App;


public class WalletRouter {
    public static  final String  RESULT_FOR_TRANSACTION ="RESULT_FOR_TRANSACTION";
    public static  final  int RESULT_CODE_FOR_TRANSACTION = 1;



    public void openWalletPage(Context context) {

        open(context);
    }

    public void open(Context context) {
        Intent intent = new Intent(context, WalletActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void openTransactionForResult(Activity context,boolean transactionResult) {

//        语言切换时 会用到这里   ， 因为WalletActivity 是singleTask 的 因此 常规的 startActivity 是不会触发 oncreate的 ，但是 SstartActivityForResule 可以。
        Intent intent = new Intent(context, WalletActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(RESULT_FOR_TRANSACTION,transactionResult);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivityForResult(intent,RESULT_CODE_FOR_TRANSACTION);
//        context.startActivity(intent);
    }
}
