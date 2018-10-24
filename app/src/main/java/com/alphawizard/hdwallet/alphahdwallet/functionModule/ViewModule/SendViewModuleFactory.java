package com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.send.SendViewModule;
import com.alphawizard.hdwallet.alphahdwallet.interact.SendTransactionInteract;

public class SendViewModuleFactory implements ViewModelProvider.Factory {

    SendTransactionInteract  sendTransactionInteract;
    WalletRouter walletRouter;
    public SendViewModuleFactory(SendTransactionInteract sendTransactionInteract,WalletRouter walletRouter) {
        this.sendTransactionInteract = sendTransactionInteract;
        this.walletRouter =  walletRouter;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SendViewModule(sendTransactionInteract,walletRouter);
    }
}
