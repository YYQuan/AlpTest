package com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepositoryType;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ManagerAccounts.ManagerAccountsRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.CreateOrImport.CreateOrImportRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.send.SendRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.web3.Web3Router;
import com.alphawizard.hdwallet.alphahdwallet.interact.CreateWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.ExportWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.FetchWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.FindDefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.GetBalanceInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.SendTransactionInteract;

public class WalletsViewModuleFactory implements ViewModelProvider.Factory {


    private CreateWalletInteract createWalletInteract;
    private DefaultWalletInteract defaultWalletInteract;
    private FindDefaultWalletInteract findDefaultWalletInteract;
    private GetBalanceInteract getBalanceInteract;
    private ExportWalletInteract exportWalletInteract;
    private WalletRepositoryType walletRepositoryType;
    private CreateOrImportRouter createOrImportRouter;
    private FetchWalletInteract  fetchWalletInteract;
    private SendTransactionInteract sendTransactionInteract;
    private SendRouter sendRouter;
    private ManagerAccountsRouter managerAccountsRouter;
    private Web3Router web3Router;

    public WalletsViewModuleFactory(CreateWalletInteract createWalletInteract,
                                    DefaultWalletInteract defaultWalletInteract,
                                    FindDefaultWalletInteract findDefaultWalletInteract,
                                    FetchWalletInteract  fetchWalletInteract,
                                    GetBalanceInteract getBalanceInteract,
                                    ExportWalletInteract exportWalletInteract,
                                    SendTransactionInteract sendTransactionInteract,
                                    CreateOrImportRouter createOrImportRouter,
                                    SendRouter  sendRouter,
                                    ManagerAccountsRouter managerAccountsRouter,
                                    Web3Router web3Router,
                                    WalletRepositoryType walletRepositoryType) {
        this.createWalletInteract = createWalletInteract;
        this.defaultWalletInteract = defaultWalletInteract;
        this.findDefaultWalletInteract = findDefaultWalletInteract;
        this.getBalanceInteract = getBalanceInteract;
        this.fetchWalletInteract = fetchWalletInteract;
        this.exportWalletInteract = exportWalletInteract;
        this.sendTransactionInteract =  sendTransactionInteract;
        this.createOrImportRouter = createOrImportRouter;
        this.walletRepositoryType =  walletRepositoryType;
        this.sendRouter = sendRouter;
        this.web3Router = web3Router;
        this.managerAccountsRouter =  managerAccountsRouter;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new WalletViewModule(createWalletInteract,defaultWalletInteract,findDefaultWalletInteract,
                fetchWalletInteract,getBalanceInteract,
                exportWalletInteract,sendTransactionInteract,
                createOrImportRouter,sendRouter,
                managerAccountsRouter,web3Router,walletRepositoryType);
    }
}
