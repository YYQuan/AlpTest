package com.alphawizard.hdwallet.alphahdwallet.functionModule.ConfirmSend;

import android.content.Context;
import android.content.Intent;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.SendTransactionRequest;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupMnemonicsActivity;

public class ConfirmSendRouter {
    public static final  String  SEND_REQUEST_FROM ="SEND_REQUEST_FROM";
    public static final  String  SEND_REQUEST_TO ="SEND_REQUEST_TO";
    public static final  String  SEND_REQUEST_AMOUNT ="SEND_REQUEST_AMOUNT";
    public static final  String  SEND_REQUEST_GAS_PRICE ="SEND_REQUEST_GAS_PRICE";
    public static final  String  SEND_REQUEST_GAS_LIMIT ="SEND_REQUEST_GAS_LIMIT";
    public static final  String  SEND_REQUEST_DATA ="SEND_REQUEST_DATA";
    public static final  String  ACCOUNT_BALANCE ="ACCOUNT_BALANCE";

    public static final  long  DEFAULT_GAS_PRICE =1L;
    public static final  long  DEFAULT_GAS_LIMIT =21000L;

    public void open(Context context, SendTransactionRequest  request) {

        Intent intent = new Intent(context,ConfirmSendActivity.class);
        intent.putExtra(SEND_REQUEST_FROM,request.getForm());
        intent.putExtra(SEND_REQUEST_TO,request.getTo());
        intent.putExtra(SEND_REQUEST_AMOUNT,request.getEthAmount());
        intent.putExtra(SEND_REQUEST_GAS_PRICE,request.getGasPrice());
        intent.putExtra(SEND_REQUEST_GAS_LIMIT,request.getGaslimit());
        intent.putExtra(SEND_REQUEST_DATA,request.getData());

        context.startActivity(intent);
    }

    public void open(Context context, String form ,String to ,String balance ,  String amount ,long gasPrice , long gasLimit ,String data ) {

        Intent intent = new Intent(context,ConfirmSendActivity.class);
        intent.putExtra(ACCOUNT_BALANCE,balance);

        intent.putExtra(SEND_REQUEST_FROM,form);
        intent.putExtra(SEND_REQUEST_TO,to);
        intent.putExtra(SEND_REQUEST_AMOUNT,amount);
        intent.putExtra(SEND_REQUEST_GAS_PRICE,gasPrice);
        intent.putExtra(SEND_REQUEST_GAS_LIMIT,gasLimit);
        intent.putExtra(SEND_REQUEST_DATA,data);

        context.startActivity(intent);
    }


}
