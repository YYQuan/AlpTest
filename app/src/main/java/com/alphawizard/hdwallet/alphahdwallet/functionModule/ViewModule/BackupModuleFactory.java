package com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PasswordStore;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.verifyMnemonics.VerifyMnemonicsRouter;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;

public class BackupModuleFactory implements ViewModelProvider.Factory{

    VerifyMnemonicsRouter verifyMnemonicsRouter;
    WalletRouter walletRouter;
    DefaultWalletInteract defaultWalletInteract;
    PasswordStore passwordStore;

    public BackupModuleFactory(VerifyMnemonicsRouter verifyMnemonicsRouter,
                               WalletRouter walletRouter,
                                DefaultWalletInteract defaultWalletInteract,
                               PasswordStore passwordStore) {
        this.verifyMnemonicsRouter = verifyMnemonicsRouter;
        this.walletRouter = walletRouter;
        this.defaultWalletInteract =  defaultWalletInteract;
        this.passwordStore =  passwordStore;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new BackupViewModule( verifyMnemonicsRouter,walletRouter,defaultWalletInteract,passwordStore);
    }
}