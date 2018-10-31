package com.alphawizard.hdwallet.alphahdwallet.functionModule.web3;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.SendTransactionInteract;
import com.alphawizard.hdwallet.common.base.ViewModule.BaseViewModel;

import java.math.BigInteger;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class Web3ViewModule extends BaseViewModel {

    WalletRouter mWalletRouter;
    SendTransactionInteract sendTransactionInteract;
    DefaultWalletInteract defaultWalletInteract;

    private final MutableLiveData<String> transactionHash = new MutableLiveData<>();

    public Web3ViewModule(DefaultWalletInteract defaultWalletInteract, SendTransactionInteract sendTransactionInteract, WalletRouter walletRouter) {
        this.sendTransactionInteract = sendTransactionInteract;
        this.defaultWalletInteract =  defaultWalletInteract;
        mWalletRouter = walletRouter;
    }

    public LiveData<String> transactionHash() {
        return transactionHash;
    }


    public String  getDefaultWalletAddress(){
        return defaultWalletInteract.getDefaultWalletAddress().blockingGet();
    }

    public void  sendTransaction(String toAddress, BigInteger amount, BigInteger gasPrice, long gasLimit, long nonce){

         sendTransactionInteract
                .sendTransaction(toAddress,amount,gasPrice,gasLimit,nonce,null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::sendSuccess,this::sendError);
    }



    public void  sendTransaction(String toAddress,BigInteger amount , BigInteger gasPrice, BigInteger gasLimit,long nonce,String dataString,long chainId){

        sendTransactionInteract
                .sendTransaction(toAddress,amount,gasPrice,gasLimit,nonce,dataString,chainId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::sendSuccess,this::sendError);
    }


    private void sendError(Throwable throwable) {
        App.showToast("send fail");
    }

    private void sendSuccess(String s) {
        transactionHash.postValue(s);
        App.showToast("send success");
    }


}
