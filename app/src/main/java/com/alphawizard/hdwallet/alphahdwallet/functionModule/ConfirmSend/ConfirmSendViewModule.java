package com.alphawizard.hdwallet.alphahdwallet.functionModule.ConfirmSend;

import android.app.Activity;
import android.content.Context;

import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.interact.SendTransactionInteract;
import com.alphawizard.hdwallet.alphahdwallet.utils.BalanceUtils;
import com.alphawizard.hdwallet.common.base.ViewModule.BaseViewModel;

import java.math.BigInteger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ConfirmSendViewModule   extends BaseViewModel {
    SendTransactionInteract mSendTransactionInteract;
    WalletRouter mWalletRouter;

    public ConfirmSendViewModule(SendTransactionInteract sendTransactionInteract, WalletRouter walletRouter) {
        mSendTransactionInteract = sendTransactionInteract;
        mWalletRouter = walletRouter;
    }

    private void sendError(Throwable throwable) {
//        App.showToast("send  Error");
        progress.postValue(false);
        App.showToast("Send Error");
    }

    private void sendSuccess(String s) {
//        App.showToast("send  Success   transaction  hashcode :" +s );
        progress.postValue(true);
        App.showToast("Send Success");
    }


    void openWallet(Activity context,boolean result){
        mWalletRouter.open(context);
//        mWalletRouter.openTransactionForResult(context,result);
    }

    public  void  sendTransaction(String  to , String amount,long gasPrice,long gasLimit,String data){
        mSendTransactionInteract
                .sendTransaction(to,amount,gasPrice,gasLimit,data)
//                .sendTransaction(to,amount,gasPrice,gasLimit)
                .observeOn(Schedulers.io())
                .subscribe(this::sendSuccess,this::sendError);
    }

    public  void  sendTransaction(String  to ,String amount, String  gasPrice ,String gasLimit,String dataString){

        gasPrice = "7";
        gasLimit = "1000000";
        BigInteger subunitAmount = BalanceUtils.baseToSubunit(amount, 18);

        BigInteger bigL = BalanceUtils.baseToSubunit(gasPrice, 9);

        BigInteger bigLimitL = BalanceUtils.baseToSubunit(gasLimit, 1);

        String limitString = String.valueOf(bigLimitL.longValue());
        String bigLString = String.valueOf(bigL.longValue());

        sendTransaction(to,subunitAmount,bigL,bigLimitL,0,dataString,4L);
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
