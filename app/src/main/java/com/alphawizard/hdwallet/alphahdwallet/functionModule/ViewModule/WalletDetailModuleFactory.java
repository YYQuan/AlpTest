package com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PasswordStore;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.WalletDetail.WalletDetailViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.verifyMnemonics.VerifyMnemonicsViewModule;
import com.alphawizard.hdwallet.alphahdwallet.interact.ExportWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.GetBalanceInteract;

public class WalletDetailModuleFactory implements ViewModelProvider.Factory{

    GetBalanceInteract getBalanceInteract;
    ExportWalletInteract exportWalletInteract;
    PasswordStore passwordStore;

    public WalletDetailModuleFactory(GetBalanceInteract getBalanceInteract,
                                     ExportWalletInteract exportWalletInteract,
                                     PasswordStore passwordStore) {
        this.getBalanceInteract =  getBalanceInteract;
        this.exportWalletInteract = exportWalletInteract;
        this.passwordStore = passwordStore;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new WalletDetailViewModule(getBalanceInteract,exportWalletInteract,passwordStore);
    }
}