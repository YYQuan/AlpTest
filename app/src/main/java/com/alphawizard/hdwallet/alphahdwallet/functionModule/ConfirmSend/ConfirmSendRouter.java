package com.alphawizard.hdwallet.alphahdwallet.functionModule.ConfirmSend;

import android.content.Context;
import android.content.Intent;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.SendTransactionRequest;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupMnemonicsActivity;

public class ConfirmSendRouter {
    public static final  String  SEND_REQUEST_FROM ="SEND_REQUEST_FROM";
    public static final  String  SEND_REQUEST_TO ="SEND_REQUEST_TO";
    public static final  String  SEND_REQUEST_AMOUNT ="SEND_REQUEST_AMOUNT";
    public static final  String  SEND_REQUEST_GAS ="SEND_REQUEST_GAS";

    public static final  long  DEFAULT_GAS_PRICE =1L;
    public static final  long  DEFAULT_GAS_LIMIT =21000L;

    public void open(Context context, SendTransactionRequest  request) {

        Intent intent = new Intent(context,ConfirmSendActivity.class);
        intent.putExtra(SEND_REQUEST_FROM,request.getForm());
        intent.putExtra(SEND_REQUEST_TO,request.getTo());
        intent.putExtra(SEND_REQUEST_AMOUNT,request.getEthAmount());
        intent.putExtra(SEND_REQUEST_GAS,request.getGasPrice());
        context.startActivity(intent);
    }


}
