package com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.fristLaunch.FirstLaunchViewModule;
import com.alphawizard.hdwallet.alphahdwallet.interact.CreateWalletInteract;

public class FirstLaunchViewModuleFactory  implements ViewModelProvider.Factory{

    CreateWalletInteract mInteract;
    WalletRouter mWalletRouter;
    ImportRouter mImportRouter;

    public FirstLaunchViewModuleFactory(CreateWalletInteract interact,WalletRouter walletRouter,ImportRouter importRouter) {
        mInteract = interact;
        mWalletRouter = walletRouter;
        mImportRouter =  importRouter;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new FirstLaunchViewModule(mInteract,mWalletRouter,mImportRouter);
    }
}
