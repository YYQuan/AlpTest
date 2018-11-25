package com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.ConfirmSend.ConfirmSendRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.send.SendViewModule;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.SendTransactionInteract;

public class SendViewModuleFactory implements ViewModelProvider.Factory {

    SendTransactionInteract  sendTransactionInteract;
    ConfirmSendRouter confirmSendRouter;
    DefaultWalletInteract defaultWalletInteract;
    public SendViewModuleFactory(SendTransactionInteract sendTransactionInteract , DefaultWalletInteract defaultWalletInteract, ConfirmSendRouter confirmSendRouter) {
        this.sendTransactionInteract = sendTransactionInteract;
        this.confirmSendRouter =  confirmSendRouter;
        this.defaultWalletInteract= defaultWalletInteract;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SendViewModule(sendTransactionInteract,defaultWalletInteract,confirmSendRouter);
    }
}
