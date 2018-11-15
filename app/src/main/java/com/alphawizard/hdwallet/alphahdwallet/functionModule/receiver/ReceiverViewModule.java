package com.alphawizard.hdwallet.alphahdwallet.functionModule.receiver;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;
import com.alphawizard.hdwallet.common.base.ViewModule.BaseViewModel;

import io.reactivex.Single;

public class ReceiverViewModule extends BaseViewModel {


    DefaultWalletInteract defaultWalletInteract;

    private final MutableLiveData<String> defaultWalletAddress = new MutableLiveData<>();

    public ReceiverViewModule(DefaultWalletInteract defaultWalletInteract){
        this.defaultWalletInteract =  defaultWalletInteract;
    }

    public LiveData<String> defaultWalletAddress() {
        return defaultWalletAddress;
    }


    public void getDefaultWallet(){
        defaultWalletInteract.getDefaultWalletAddress()
                .subscribe(wallet -> onDefaultWalletChanged(wallet), this::onGetDefaultAccountsError);
    }

    private void onGetDefaultAccountsError(Throwable throwable) {
    }

    private void onDefaultWalletChanged(String wallet) {

        defaultWalletAddress.postValue(wallet);

    }



}
