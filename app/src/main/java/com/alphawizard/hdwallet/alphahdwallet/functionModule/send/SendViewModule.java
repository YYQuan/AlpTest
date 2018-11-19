package com.alphawizard.hdwallet.alphahdwallet.functionModule.send;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.MainThread;

import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.interact.SendTransactionInteract;
import com.alphawizard.hdwallet.common.base.ViewModule.BaseViewModel;

import java.math.BigInteger;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class SendViewModule extends BaseViewModel {

    SendTransactionInteract  mSendTransactionInteract;
    WalletRouter mWalletRouter;

    public SendViewModule(SendTransactionInteract sendTransactionInteract, WalletRouter walletRouter) {
        mSendTransactionInteract = sendTransactionInteract;
        mWalletRouter = walletRouter;
    }

    private void sendError(Throwable throwable) {
//        App.showToast("send  Error");
        progress.postValue(false);
    }

    private void sendSuccess(String s) {
//        App.showToast("send  Success   transaction  hashcode :" +s );
        progress.postValue(true);
    }


    void openWallet(Context context){
        mWalletRouter.open(context);
    }
    public  void  sendTransaction(String  to , String amount){
        mSendTransactionInteract
                .sendTransaction(to,amount)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::sendSuccess,this::sendError);
    }

    public void  sendTransaction(String toAddress, BigInteger amount , BigInteger gasPrice, BigInteger gasLimit, long nonce, String dataString, long chainId){
        mSendTransactionInteract
                .sendTransaction(toAddress,amount,gasPrice,gasLimit,nonce,dataString,chainId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::sendSuccess,this::sendError);
    }
    void openWalletForTransactionResult(Activity context, boolean result){
        mWalletRouter.openTransactionForResult(context,result);
    }
}
