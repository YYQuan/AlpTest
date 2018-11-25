package com.alphawizard.hdwallet.alphahdwallet.functionModule.send;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.MainThread;

import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.SendTransactionRequest;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ConfirmSend.ConfirmSendRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.DeleteWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.SendTransactionInteract;
import com.alphawizard.hdwallet.alphahdwallet.utils.BalanceUtils;
import com.alphawizard.hdwallet.common.base.ViewModule.BaseViewModel;

import java.math.BigInteger;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class SendViewModule extends BaseViewModel {

    SendTransactionInteract  mSendTransactionInteract;

    ConfirmSendRouter mConfirmSendRouter;
    DefaultWalletInteract mDefaultWalletInteract;

    public SendViewModule(SendTransactionInteract sendTransactionInteract
            , DefaultWalletInteract defaultWalletInteract
            , ConfirmSendRouter confirmSendRouter) {
        mSendTransactionInteract = sendTransactionInteract;
        mConfirmSendRouter= confirmSendRouter;
        mDefaultWalletInteract =defaultWalletInteract;
    }

    private void sendError(Throwable throwable) {
//        App.showToast("send  Error");
        progress.postValue(false);
    }

    private void sendSuccess(String s) {
//        App.showToast("send  Success   transaction  hashcode :" +s );
        progress.postValue(true);
    }




    void openConfirm(Context context,String to ,String amount ,long gasPrice ,long gasLimit,String data ){
        mDefaultWalletInteract.getDefaultWalletAddress()
                .subscribe(s->openConfirm(context,s,to,amount,gasPrice,gasLimit,data));

    }

    private void openConfirm(Context context ,String from,String to,String amount,long gasPrice ,long gasLimit,String data) {
        SendTransactionRequest request = new SendTransactionRequest(from,to,amount,gasPrice,gasLimit,data);
        mConfirmSendRouter.open(context,request);
    }

    public  void  sendTransaction(String  to , String amount){
        mSendTransactionInteract
                .sendTransaction(to,amount)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::sendSuccess,this::sendError);
    }

    public  void  sendTransaction(String  to ,String amount, String  gasPrice ,String gasLimit,String dataString){

        BigInteger subunitAmount = BalanceUtils.baseToSubunit(amount, 18);

        BigInteger bigL = BalanceUtils.baseToSubunit(gasPrice, 9);

        BigInteger bigLimitL = BalanceUtils.baseToSubunit(gasLimit, 1);


        sendTransaction(to,subunitAmount,bigL,bigLimitL,0,dataString,4L);
    }

    public void  sendTransaction(String toAddress, BigInteger amount , BigInteger gasPrice, BigInteger gasLimit, long nonce, String dataString, long chainId){
        mSendTransactionInteract
                .sendTransaction(toAddress,amount,gasPrice,gasLimit,nonce,dataString,chainId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::sendSuccess,this::sendError);
    }

}
