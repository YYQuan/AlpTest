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
    static CreateWalletInteract  createWalletInteract(WalletRepositoryType walletRepositoryType, PasswordStore passwordStore ){
        return new CreateWalletInteract(walletRepositoryType,passwordStore);
    }

    @Provides
    static DefaultWalletInteract  defaultWalletInteract(WalletRepositoryType walletRepositoryType, PreferenceRepositoryType  preferenceRepositoryType){
        return new DefaultWalletInteract(walletRepositoryType,preferenceRepositoryType);
    }

    @Provides
    static FindDefaultWalletInteract  findDefaultWalletInteract(PreferenceRepositoryType  preferenceRepositoryType, WalletRepositoryType walletRepositoryType){
        return new FindDefaultWalletInteract(preferenceRepositoryType,walletRepositoryType);
    }

    @Provides
    static  GetBalanceInteract  getBalanceInteract(WalletRepositoryType walletRepositoryType){
        return new GetBalanceInteract(walletRepositoryType);
    }

    @Provides
    static  SendTransactionInteract  sendTransactionInteract(WalletRepositoryType walletRepositoryType,PasswordStore passwordStore){
        return new SendTransactionInteract(walletRepositoryType,passwordStore);
    }

    @Provides
    static  FetchWalletInteract  fetchWalletInteract(WalletRepositoryType walletRepositoryType){
        return new FetchWalletInteract(walletRepositoryType);
    }

    @Provides
    static  ImportAccountInteract  importAccountInteract(WalletRepositoryType walletRepositoryType,
                                                         PasswordStore passwordStore){
        return new ImportAccountInteract(walletRepositoryType,passwordStore);
    }

    @Provides
    static  ExportWalletInteract  exportWalletInteract(WalletRepositoryType walletRepositoryType,
                                                       PasswordStore passwordStore){
        return new ExportWalletInteract(walletRepositoryType,passwordStore);
    }


    /*

    Router


     */
    @Provides
    public static CreateOrImportRouter providesFirstLaunchRouter(){
        return new CreateOrImportRouter();
    }

    @Provides
    public static WalletRouter providesWalletRouter(){
        return new WalletRouter();
    }

    @Provides
    public static SendRouter providesSendRouter(){
        return new SendRouter();
    }

    @Provides
    public static ImportRouter providesImportRouter(){
        return new ImportRouter();
    }

    @Provides
    public static WalletDetailRouter walletDetailRouter(){
        return new WalletDetailRouter();
    }

    @Provides
    public static ManagerAccountsRouter managerAccountsRouter(){
        return new ManagerAccountsRouter();
    }

    @Provides
    public static BackupRouter backupRouter(){
        return new BackupRouter();
    }

    @Provides
    public static VerifyMnemonicsRouter verifyMnemonicsRouter(){
        return new VerifyMnemonicsRouter();
    }
    @Provides
    public static Web3Router web3Router(){
        return new Web3Router();
    }

    @Provides
    public static LaunchRouter launchRouter(){
        return new LaunchRouter();
    }

    @Provides
    public static ReceiverRouter receiverRouter(){
        return new ReceiverRouter();
    }

}
