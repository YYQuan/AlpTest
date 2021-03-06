package com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PasswordStore;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ConfirmSend.ConfirmSendModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ConfirmSend.ConfirmSendViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.verifyMnemonics.VerifyMnemonicsRouter;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.SendTransactionInteract;

public class ConfirmSendModuleFactory implements ViewModelProvider.Factory{


    SendTransactionInteract sendTransactionInteract;
    WalletRouter  walletRouter;

    public ConfirmSendModuleFactory(SendTransactionInteract sendTransactionInteract, WalletRouter walletRouter) {
        this.walletRouter = walletRouter;
        this.sendTransactionInteract = sendTransactionInteract;

    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new ConfirmSendViewModule( sendTransactionInteract,walletRouter);
    }
}