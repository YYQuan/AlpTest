package com.alphawizard.hdwallet.alphahdwallet.functionModule.send;

import android.content.Context;
import android.support.annotation.MainThread;

import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.interact.SendTransactionInteract;
import com.alphawizard.hdwallet.common.base.ViewModule.BaseViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class SendViewModule extends BaseViewModel {

    SendTransactionInteract  mSendTransactionInteract;
    WalletRouter mWalletRouter;

    public SendViewModule(SendTransactionInteract sendTransactionInteract, WalletRouter walletRouter) {
        mSendTransactionInteract = sendTransactionInteract;
        mWalletRouter = walletRouter;
    }


    public  void  sendTransaction(String  to , String amount){
        mSendTransactionInteract
                .sendTransaction(to,amount)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::sendSuccess,this::sendError);
    }

    private void sendError(Throwable throwable) {
        App.showToast("send  Error");
        progress.postValue(false);
    }

    private void sendSuccess(String s) {
        App.showToast("send  Success   transaction  hashcode :" +s );
        progress.postValue(true);
    }


    void openWallet(Context context){
        mWalletRouter.open(context);
    }
}
