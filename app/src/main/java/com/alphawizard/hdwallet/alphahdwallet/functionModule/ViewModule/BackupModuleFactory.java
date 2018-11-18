package com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.verifyMnemonics.VerifyMnemonicsRouter;

public class BackupModuleFactory implements ViewModelProvider.Factory{

    VerifyMnemonicsRouter verifyMnemonicsRouter;
    WalletRouter walletRouter;

    public BackupModuleFactory(VerifyMnemonicsRouter verifyMnemonicsRouter,
                               WalletRouter walletRouter) {
        this.verifyMnemonicsRouter = verifyMnemonicsRouter;
        this.walletRouter = walletRouter;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new BackupViewModule( verifyMnemonicsRouter,walletRouter);
    }
}