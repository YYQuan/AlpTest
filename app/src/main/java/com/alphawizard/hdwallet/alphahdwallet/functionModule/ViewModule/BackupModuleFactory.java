package com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.verifyMnemonics.VerifyMnemonicsRouter;

public class BackupModuleFactory implements ViewModelProvider.Factory{

    VerifyMnemonicsRouter verifyMnemonicsRouter;

    public BackupModuleFactory(VerifyMnemonicsRouter verifyMnemonicsRouter) {
        this.verifyMnemonicsRouter = verifyMnemonicsRouter;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new BackupViewModule( verifyMnemonicsRouter);
    }
}