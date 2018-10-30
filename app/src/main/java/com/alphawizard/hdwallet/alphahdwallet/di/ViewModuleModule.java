package com.alphawizard.hdwallet.alphahdwallet.di;

import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepositoryType;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.FirstLaunchViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.ImportViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.SendViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.WalletsViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.Web3ViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.fristLaunch.FirstLaunchRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.fristLaunch.FirstLaunchViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.send.SendRouter;
import com.alphawizard.hdwallet.alphahdwallet.interact.CreateWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.ExportWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.FetchWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.FindDefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.GetBalanceInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.ImportAccountInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.SendTransactionInteract;

import dagger.Module;
import dagger.Provides;

@Module
public class ViewModuleModule {

    @Provides
    WalletsViewModuleFactory providesWalletsViewModuleFactory(CreateWalletInteract createWalletInteract,
                                                              DefaultWalletInteract defaultWalletInteract,
                                                              FindDefaultWalletInteract findDefaultWalletInteract,
                                                              FetchWalletInteract  fetchWalletInteract,
                                                              GetBalanceInteract getBalanceInteract,
                                                              ExportWalletInteract exportWalletInteract,
                                                              FirstLaunchRouter firstLaunchRouter,
                                                              SendRouter sendRouter,
                                                              WalletRepositoryType walletRepositoryType){
        return  new WalletsViewModuleFactory(createWalletInteract,defaultWalletInteract,findDefaultWalletInteract,fetchWalletInteract,getBalanceInteract,exportWalletInteract,firstLaunchRouter,sendRouter,walletRepositoryType);
    }

    @Provides
    FirstLaunchViewModuleFactory providesFirstLaunchViewModule(CreateWalletInteract interact,DefaultWalletInteract  defaultWalletInteract, WalletRouter walletRouter, ImportRouter importRouter){
        return  new FirstLaunchViewModuleFactory(interact,defaultWalletInteract,walletRouter,importRouter);
    }

    @Provides
    SendViewModuleFactory providesSendViewModule(SendTransactionInteract interact,WalletRouter walletRouter){
        return  new SendViewModuleFactory(interact,walletRouter);
    }

    @Provides
    ImportViewModuleFactory importViewModuleFactory(ImportAccountInteract interact,WalletRouter walletRouter){
        return  new ImportViewModuleFactory(interact,walletRouter);
    }

    @Provides
    Web3ViewModuleFactory web3ViewModuleFactory(DefaultWalletInteract defaultWalletInteract,SendTransactionInteract sendTransactionInteract, WalletRouter walletRouter){
        return  new Web3ViewModuleFactory(defaultWalletInteract,sendTransactionInteract,walletRouter);
    }

}
