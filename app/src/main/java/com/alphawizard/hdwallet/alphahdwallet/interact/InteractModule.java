package com.alphawizard.hdwallet.alphahdwallet.interact;


import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PasswordStore;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PreferenceRepositoryType;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepositoryType;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.CreateOrImport.CreateOrImportRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Launch.LaunchRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ManagerAccounts.ManagerAccountsRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.WalletDetail.WalletDetailRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.receiver.ReceiverRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.send.SendRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.verifyMnemonics.VerifyMnemonicsRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.web3.Web3Router;

import dagger.Module;
import dagger.Provides;

/**
 * provides  interact and     router
 */
@Module
public class InteractModule {

    /*

    Interact


     */

    @Provides
    CreateWalletInteract  createWalletInteract(WalletRepositoryType walletRepositoryType, PasswordStore passwordStore ){
        return new CreateWalletInteract(walletRepositoryType,passwordStore);
    }

    @Provides
    DefaultWalletInteract  defaultWalletInteract(WalletRepositoryType walletRepositoryType, PreferenceRepositoryType  preferenceRepositoryType){
        return new DefaultWalletInteract(walletRepositoryType,preferenceRepositoryType);
    }

    @Provides
    FindDefaultWalletInteract  findDefaultWalletInteract(PreferenceRepositoryType  preferenceRepositoryType, WalletRepositoryType walletRepositoryType){
        return new FindDefaultWalletInteract(preferenceRepositoryType,walletRepositoryType);
    }

    @Provides
    GetBalanceInteract  getBalanceInteract(WalletRepositoryType walletRepositoryType){
        return new GetBalanceInteract(walletRepositoryType);
    }

    @Provides
    SendTransactionInteract  sendTransactionInteract(WalletRepositoryType walletRepositoryType,PasswordStore passwordStore){
        return new SendTransactionInteract(walletRepositoryType,passwordStore);
    }

    @Provides
     FetchWalletInteract  fetchWalletInteract(WalletRepositoryType walletRepositoryType){
        return new FetchWalletInteract(walletRepositoryType);
    }

    @Provides
    ImportAccountInteract  importAccountInteract(WalletRepositoryType walletRepositoryType,
                                                         PasswordStore passwordStore){
        return new ImportAccountInteract(walletRepositoryType,passwordStore);
    }

    @Provides
    ExportWalletInteract  exportWalletInteract(WalletRepositoryType walletRepositoryType,
                                                       PasswordStore passwordStore){
        return new ExportWalletInteract(walletRepositoryType,passwordStore);
    }

    @Provides
    DeleteWalletInteract  deleteWalletInteract(WalletRepositoryType walletRepositoryType,
                                                       PasswordStore passwordStore){
        return new DeleteWalletInteract(walletRepositoryType,passwordStore);
    }

    @Provides
    LanguageInteract  languageInteract(PasswordStore passwordStore){
        return new LanguageInteract(passwordStore);
    }


    /*

    Router


     */
    @Provides
    public  CreateOrImportRouter providesFirstLaunchRouter(){
        return new CreateOrImportRouter();
    }

    @Provides
    public  WalletRouter providesWalletRouter(){
        return new WalletRouter();
    }

    @Provides
    public  SendRouter providesSendRouter(){
        return new SendRouter();
    }

    @Provides
    public  ImportRouter providesImportRouter(){
        return new ImportRouter();
    }

    @Provides
    public  WalletDetailRouter walletDetailRouter(){
        return new WalletDetailRouter();
    }

    @Provides
    public  ManagerAccountsRouter managerAccountsRouter(){
        return new ManagerAccountsRouter();
    }

    @Provides
    public  BackupRouter backupRouter(){
        return new BackupRouter();
    }

    @Provides
    public  VerifyMnemonicsRouter verifyMnemonicsRouter(){
        return new VerifyMnemonicsRouter();
    }
    @Provides
    public  Web3Router web3Router(){
        return new Web3Router();
    }

    @Provides
    public  LaunchRouter launchRouter(){
        return new LaunchRouter();
    }

    @Provides
    public  ReceiverRouter receiverRouter(){
        return new ReceiverRouter();
    }

}
