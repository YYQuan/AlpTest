package com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PasswordStore;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.WalletDetail.WalletDetailViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.verifyMnemonics.VerifyMnemonicsViewModule;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.DeleteWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.ExportWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.FetchWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.FindDefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.GetBalanceInteract;

public class WalletDetailModuleFactory implements ViewModelProvider.Factory{

    GetBalanceInteract getBalanceInteract;
    ExportWalletInteract exportWalletInteract;
    DeleteWalletInteract deleteWalletInteract;
    DefaultWalletInteract defaultWalletInteract;
    FetchWalletInteract fetchWalletInteract;
    PasswordStore passwordStore;
    WalletRouter walletRouter;
    public WalletDetailModuleFactory(GetBalanceInteract getBalanceInteract,
                                     ExportWalletInteract exportWalletInteract,
                                     DeleteWalletInteract deleteWalletInteract,
                                     FetchWalletInteract fetchWalletInteract,
                                     DefaultWalletInteract defaultWalletInteract,
                                     WalletRouter walletRouter,
                                     PasswordStore passwordStore) {
        this.getBalanceInteract =  getBalanceInteract;
        this.exportWalletInteract = exportWalletInteract;
        this.deleteWalletInteract = deleteWalletInteract;
        this.fetchWalletInteract = fetchWalletInteract;
        this.defaultWalletInteract = defaultWalletInteract;
        this.passwordStore = passwordStore;
        this.walletRouter =  walletRouter;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new WalletDetailViewModule(getBalanceInteract,exportWalletInteract,deleteWalletInteract,fetchWalletInteract,defaultWalletInteract,walletRouter,passwordStore);
    }
}