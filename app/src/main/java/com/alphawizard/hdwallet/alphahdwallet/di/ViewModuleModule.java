package com.alphawizard.hdwallet.alphahdwallet.di;

import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepositoryType;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.FirstLaunchViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.ImportViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.SendViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.WalletsViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.fristLaunch.FirstLaunchViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.send.SendRouter;
import com.alphawizard.hdwallet.alphahdwallet.interact.CreateWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;
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
                                                              SendRouter sendRouter,
                                                              WalletRepositoryType walletRepositoryType){
        return  new WalletsViewModuleFactory(createWalletInteract,defaultWalletInteract,findDefaultWalletInteract,fetchWalletInteract,getBalanceInteract,sendRouter,walletRepositoryType);
    }

    @Provides
    FirstLaunchViewModuleFactory providesFirstLaunchViewModule(CreateWalletInteract interact, WalletRouter walletRouter, ImportRouter importRouter){
        return  new FirstLaunchViewModuleFactory(interact,walletRouter,importRouter);
    }

    @Provides
    SendViewModuleFactory providesSendViewModule(SendTransactionInteract interact,WalletRouter walletRouter){
        return  new SendViewModuleFactory(interact,walletRouter);
    }

    @Provides
    ImportViewModuleFactory importViewModuleFactory(ImportAccountInteract interact,WalletRouter walletRouter){
        return  new ImportViewModuleFactory(interact,walletRouter);
    }

}
