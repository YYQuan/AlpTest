package com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.Launch.LaunchViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.receiver.ReceiverViewModule;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;

public class ReceiverViewModuleFactory implements ViewModelProvider.Factory {

    DefaultWalletInteract defaultWalletInteract;
    public ReceiverViewModuleFactory(DefaultWalletInteract defaultWalletInteract) {
        this.defaultWalletInteract =  defaultWalletInteract;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ReceiverViewModule(defaultWalletInteract);
    }
}