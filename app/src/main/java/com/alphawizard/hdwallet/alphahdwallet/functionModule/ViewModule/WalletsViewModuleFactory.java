package com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepositoryType;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.fristLaunch.FirstLaunchRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.send.SendRouter;
import com.alphawizard.hdwallet.alphahdwallet.interact.CreateWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.ExportWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.FetchWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.FindDefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.GetBalanceInteract;

public class WalletsViewModuleFactory implements ViewModelProvider.Factory {


    private CreateWalletInteract createWalletInteract;
    private DefaultWalletInteract defaultWalletInteract;
    private FindDefaultWalletInteract findDefaultWalletInteract;
    private GetBalanceInteract getBalanceInteract;
    private ExportWalletInteract exportWalletInteract;
    private WalletRepositoryType walletRepositoryType;
    private FirstLaunchRouter   firstLaunchRouter;
    private FetchWalletInteract  fetchWalletInteract;
    private SendRouter sendRouter;

    public WalletsViewModuleFactory(CreateWalletInteract createWalletInteract,
                                    DefaultWalletInteract defaultWalletInteract,
                                    FindDefaultWalletInteract findDefaultWalletInteract,
                                    FetchWalletInteract  fetchWalletInteract,
                                    GetBalanceInteract getBalanceInteract,
                                    ExportWalletInteract ExportWalletInteract,
                                    FirstLaunchRouter   firstLaunchRouter,
                                    SendRouter  sendRouter,
                                    WalletRepositoryType walletRepositoryType) {
        this.createWalletInteract = createWalletInteract;
        this.defaultWalletInteract = defaultWalletInteract;
        this.findDefaultWalletInteract = findDefaultWalletInteract;
        this.getBalanceInteract = getBalanceInteract;
        this.fetchWalletInteract = fetchWalletInteract;
        this.exportWalletInteract = ExportWalletInteract;
        this.firstLaunchRouter =  firstLaunchRouter;
        this.walletRepositoryType =  walletRepositoryType;
        this.sendRouter = sendRouter;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new WalletViewModule(createWalletInteract,defaultWalletInteract,findDefaultWalletInteract,fetchWalletInteract,getBalanceInteract,exportWalletInteract,firstLaunchRouter,sendRouter,walletRepositoryType);
    }
}
