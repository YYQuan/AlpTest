package com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.fristLaunch.FirstLaunchViewModule;
import com.alphawizard.hdwallet.alphahdwallet.interact.CreateWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;

public class FirstLaunchViewModuleFactory  implements ViewModelProvider.Factory{

    CreateWalletInteract mInteract;
    WalletRouter mWalletRouter;
    ImportRouter mImportRouter;
    BackupRouter mBackupRouter;
    DefaultWalletInteract  mDefaultWalletInteract;

    public FirstLaunchViewModuleFactory(CreateWalletInteract interact,
                                        DefaultWalletInteract  defaultWalletInteract,
                                        WalletRouter walletRouter,
                                        BackupRouter backupRouter,
                                        ImportRouter importRouter) {
        mInteract = interact;
        mWalletRouter = walletRouter;
        mImportRouter =  importRouter;
        mBackupRouter =  backupRouter;
        mDefaultWalletInteract = defaultWalletInteract;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new FirstLaunchViewModule(mInteract,mDefaultWalletInteract,mWalletRouter,mBackupRouter,mImportRouter);
    }
}
