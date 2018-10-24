package com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.send.SendViewModule;
import com.alphawizard.hdwallet.alphahdwallet.interact.SendTransactionInteract;

public class SendViewModuleFactory implements ViewModelProvider.Factory {

    SendTransactionInteract  sendTransactionInteract;

    public SendViewModuleFactory(SendTransactionInteract sendTransactionInteract) {
        this.sendTransactionInteract = sendTransactionInteract;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SendViewModule(sendTransactionInteract);
    }
}
