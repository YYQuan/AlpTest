package com.alphawizard.hdwallet.alphahdwallet.functionModule.send;

import android.content.Context;
import android.content.Intent;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Transaction;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletActivity;

public class SendRouter {

    public static final  String  WALLET_BALANCE = "WALLET_BALANCE";
    public static final  String  WALLET_SEND2ADDRESS = "WALLET_SEND2ADDRESS";
    public static final  String  WALLET_SEND_AMOUNT = "WALLET_SEND_AMOUNT";
    public static final  String  WALLET_SEND_TRANSACTION = "WALLET_SEND_TRANSACTION";
    public void open(Context context,float value) {
        Intent intent = new Intent(context, SendActivity.class);
        intent.putExtra(WALLET_BALANCE,value);
        context.startActivity(intent);
    }

    public void open(Context context,String address ,String amount ,float value) {
        Intent intent = new Intent(context, SendActivity.class);
        intent.putExtra(WALLET_BALANCE,value);
        intent.putExtra(WALLET_SEND2ADDRESS,address);
        intent.putExtra(WALLET_SEND_AMOUNT,amount);
        context.startActivity(intent);
    }

    public void open(Context context,String address ,String amount ,float value, trust.core.entity.Transaction transaction) {
        Intent intent = new Intent(context, SendActivity.class);
        intent.putExtra(WALLET_SEND_TRANSACTION,transaction);
        intent.putExtra(WALLET_BALANCE,value);
        intent.putExtra(WALLET_SEND2ADDRESS,address);
        intent.putExtra(WALLET_SEND_AMOUNT,amount);
        context.startActivity(intent);
    }
}
