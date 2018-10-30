package com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.web3.Web3ViewModule;
import com.alphawizard.hdwallet.alphahdwallet.interact.CreateWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.SendTransactionInteract;

public class Web3ViewModuleFactory  implements ViewModelProvider.Factory {

    CreateWalletInteract mInteract;
    WalletRouter mWalletRouter;
    ImportRouter mImportRouter;
    DefaultWalletInteract mDefaultWalletInteract;
    SendTransactionInteract mSendTransactionInteract;
    public Web3ViewModuleFactory(DefaultWalletInteract defaultWalletInteract,
                                 SendTransactionInteract sendTransactionInteract,
                                 WalletRouter walletRouter) {
        mSendTransactionInteract =  sendTransactionInteract;
        mWalletRouter = walletRouter;
        mDefaultWalletInteract = defaultWalletInteract;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new Web3ViewModule(mDefaultWalletInteract,mSendTransactionInteract,mWalletRouter);
    }
}