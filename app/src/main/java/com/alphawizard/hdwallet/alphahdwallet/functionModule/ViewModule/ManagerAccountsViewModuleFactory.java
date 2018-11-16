package com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PasswordStore;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ManagerAccounts.ManagerAccountsViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.WalletDetail.WalletDetailRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.web3.Web3ViewModule;
import com.alphawizard.hdwallet.alphahdwallet.interact.CreateWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.FetchWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.FindDefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.GetBalanceInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.SendTransactionInteract;

public class ManagerAccountsViewModuleFactory implements ViewModelProvider.Factory {

    DefaultWalletInteract mDefaultWalletInteract;
    FindDefaultWalletInteract mFindDefaultWalletInteract;
    FetchWalletInteract mFetchWalletInteract;
    CreateWalletInteract mCreateWalletInteract;
    GetBalanceInteract mGetBalanceInteract;
    WalletDetailRouter mWalletDetailRouter;
    ImportRouter mImportRouter;
    BackupRouter mBackupRouter;
    PasswordStore mPasswordStore;
    public ManagerAccountsViewModuleFactory(DefaultWalletInteract defaultWalletInteract,
                                            FindDefaultWalletInteract findDefaultWalletInteract,
                                            FetchWalletInteract fetchWalletInteract,
                                            CreateWalletInteract createWalletInteract,
                                            GetBalanceInteract getBalanceInteract,
                                            WalletDetailRouter walletDetailRouter,
                                            ImportRouter importRouter,
                                            BackupRouter backupRouter,
                                            PasswordStore passwordStore) {
        mDefaultWalletInteract = defaultWalletInteract;
        mFindDefaultWalletInteract = findDefaultWalletInteract;
        mFetchWalletInteract = fetchWalletInteract;
        mWalletDetailRouter =  walletDetailRouter;
        mCreateWalletInteract = createWalletInteract;
        mGetBalanceInteract = getBalanceInteract;
        mImportRouter= importRouter;
        mBackupRouter = backupRouter;
        mPasswordStore =  passwordStore;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ManagerAccountsViewModule(mDefaultWalletInteract,mFindDefaultWalletInteract,mFetchWalletInteract,mCreateWalletInteract,mGetBalanceInteract,mWalletDetailRouter,mImportRouter,mBackupRouter,mPasswordStore);
    }
}