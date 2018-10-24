package com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.interact.ImportAccountInteract;

public class ImportViewModuleFactory  implements ViewModelProvider.Factory{

    ImportAccountInteract importAccountInteract;
    WalletRouter walletRouter;

    public ImportViewModuleFactory(ImportAccountInteract importAccountInteract,
                                   WalletRouter walletRouter) {
        this.importAccountInteract =  importAccountInteract;
        this.walletRouter = walletRouter;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ImportViewModule(importAccountInteract,walletRouter);
    }
}
