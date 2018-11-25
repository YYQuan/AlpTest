package com.alphawizard.hdwallet.alphahdwallet.di;

import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PasswordStore;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepositoryType;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ConfirmSend.ConfirmSendRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ManagerAccounts.ManagerAccountsRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.BackupModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.ConfirmSendModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.FirstLaunchViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.ImportViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.LaunchViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.ManagerAccountsViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.ReceiverViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.SendViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.VerifyMnemonicsModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.WalletDetailModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.WalletsViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.Web3ViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.WalletDetail.WalletDetailRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.CreateOrImport.CreateOrImportRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.receiver.ReceiverRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.send.SendRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.verifyMnemonics.VerifyMnemonicsRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.web3.Web3Router;
import com.alphawizard.hdwallet.alphahdwallet.interact.CreateWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.DeleteWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.ExportWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.FetchWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.FindDefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.GetBalanceInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.ImportAccountInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.LanguageInteract;
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
                                                              SendTransactionInteract sendTransactionInteract,
                                                              LanguageInteract languageInteract,
                                                              CreateOrImportRouter createOrImportRouter,
                                                              SendRouter sendRouter,
                                                              ManagerAccountsRouter managerAccountsRouter,
                                                              Web3Router web3Router,
                                                              BackupRouter backupRouter,
                                                              ImportRouter importRouter,
                                                              ReceiverRouter receiverRouter,
                                                              WalletRouter walletRouter,
                                                              WalletRepositoryType walletRepositoryType){
        return  new WalletsViewModuleFactory(createWalletInteract,defaultWalletInteract,findDefaultWalletInteract,fetchWalletInteract,getBalanceInteract,
                exportWalletInteract,sendTransactionInteract,languageInteract, createOrImportRouter,sendRouter,managerAccountsRouter,web3Router,backupRouter,
                importRouter,receiverRouter,walletRouter,walletRepositoryType);
    }

    @Provides
    FirstLaunchViewModuleFactory providesFirstLaunchViewModule(CreateWalletInteract interact, DefaultWalletInteract  defaultWalletInteract, WalletRouter walletRouter, BackupRouter backupRouter, ImportRouter importRouter){
         return  new FirstLaunchViewModuleFactory(interact,defaultWalletInteract,walletRouter,backupRouter,importRouter);
    }

    @Provides
    SendViewModuleFactory providesSendViewModule(SendTransactionInteract interact, DefaultWalletInteract defaultWalletInteract, ConfirmSendRouter confirmSendRouter){
        return  new SendViewModuleFactory(interact,defaultWalletInteract,confirmSendRouter);
    }

    @Provides
    ImportViewModuleFactory importViewModuleFactory(ImportAccountInteract interact,
                                                    DefaultWalletInteract defaultWalletInteract,
                                                    WalletRouter walletRouter){
        return  new ImportViewModuleFactory(interact,defaultWalletInteract,walletRouter);
    }

    @Provides
    Web3ViewModuleFactory web3ViewModuleFactory(DefaultWalletInteract defaultWalletInteract,SendTransactionInteract sendTransactionInteract, WalletRouter walletRouter){
        return  new Web3ViewModuleFactory(defaultWalletInteract,sendTransactionInteract,walletRouter);
    }

    @Provides
    BackupModuleFactory backupModuleFactory(VerifyMnemonicsRouter verifyMnemonicsRouter,WalletRouter walletRouter,
                                            DefaultWalletInteract defaultWalletInteract,
                                            PasswordStore passwordStore){
        return  new BackupModuleFactory( verifyMnemonicsRouter,walletRouter,defaultWalletInteract,passwordStore);
    }


    @Provides
    VerifyMnemonicsModuleFactory verifyMnemonicsModuleFactory(WalletRouter walletRouter){
        return  new VerifyMnemonicsModuleFactory( walletRouter);
    }


    @Provides
    WalletDetailModuleFactory walletDetailModuleFactory(GetBalanceInteract getBalanceInteract,
                                                        ExportWalletInteract exportWalletInteract,
                                                        DeleteWalletInteract deleteWalletInteract,
                                                        FetchWalletInteract fetchWalletInteract,
                                                        DefaultWalletInteract defaultWalletInteract,
                                                        WalletRouter walletRouter,
                                                        PasswordStore passwordStore){
        return  new WalletDetailModuleFactory(getBalanceInteract,exportWalletInteract,deleteWalletInteract,fetchWalletInteract,defaultWalletInteract,walletRouter,passwordStore);
    }


    @Provides
    ManagerAccountsViewModuleFactory managerAccountsViewModuleFactory(DefaultWalletInteract defaultWalletInteract,
                                                                      FindDefaultWalletInteract findDefaultWalletInteract,
                                                                      FetchWalletInteract fetchWalletInteract,
                                                                      CreateWalletInteract createWalletInteract,
                                                                      GetBalanceInteract getBalanceInteract,
                                                                      WalletDetailRouter walletDetailRouter,
                                                                      ImportRouter importRouter,
                                                                      BackupRouter backupRouter,
                                                                      PasswordStore passwordStore){
        return  new ManagerAccountsViewModuleFactory(defaultWalletInteract,findDefaultWalletInteract,fetchWalletInteract,
                createWalletInteract,getBalanceInteract,walletDetailRouter,importRouter,backupRouter,passwordStore);
    }



    @Provides
    LaunchViewModuleFactory launchViewModuleFactory(WalletRouter walletRouter){
        return  new LaunchViewModuleFactory(walletRouter);
    }

    @Provides
    ReceiverViewModuleFactory receiverViewModuleFactory( DefaultWalletInteract defaultWalletInteract){
        return  new ReceiverViewModuleFactory( defaultWalletInteract);
    }

    @Provides
    ConfirmSendModuleFactory confirmSendModuleFactory(SendTransactionInteract sendTransactionInteract ,WalletRouter walletRouter){
        return  new ConfirmSendModuleFactory( sendTransactionInteract,walletRouter);
    }



}
