package com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PasswordStore;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepositoryType;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ConfirmSend.ConfirmSendRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ManagerAccounts.ManagerAccountsRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.CreateOrImport.CreateOrImportRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.receiver.ReceiverRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.send.SendRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.web3.Web3Router;
import com.alphawizard.hdwallet.alphahdwallet.interact.CreateWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.ExportWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.FetchWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.FindDefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.GetBalanceInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.LanguageInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.SendTransactionInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.SignPersonInteract;

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
    private SignPersonInteract signPersonInteract;
    private SendRouter sendRouter;
    private ReceiverRouter receiverRouter;
    private ManagerAccountsRouter managerAccountsRouter;
    private LanguageInteract languageInteract;
    private Web3Router web3Router;
    private PasswordStore passwordStore;
    ImportRouter importRouter;
    BackupRouter backupRouter;
    WalletRouter walletRouter;
    ConfirmSendRouter  confirmSendRouter ;

    public WalletsViewModuleFactory(CreateWalletInteract createWalletInteract,
                                    DefaultWalletInteract defaultWalletInteract,
                                    FindDefaultWalletInteract findDefaultWalletInteract,
                                    FetchWalletInteract  fetchWalletInteract,
                                    GetBalanceInteract getBalanceInteract,
                                    ExportWalletInteract exportWalletInteract,
                                    SendTransactionInteract sendTransactionInteract,
                                    LanguageInteract languageInteract,
                                    SignPersonInteract signPersonInteract,
                                    CreateOrImportRouter createOrImportRouter,
                                    SendRouter  sendRouter,
                                    ManagerAccountsRouter managerAccountsRouter,
                                    Web3Router web3Router,
                                    BackupRouter backupRouter,
                                    ImportRouter importRouter,
                                    ReceiverRouter receiverRouter,
                                    WalletRouter walletRouter,
                                    ConfirmSendRouter confirmSendRouter ,
                                    WalletRepositoryType walletRepositoryType,
                                    PasswordStore passwordStore) {
        this.createWalletInteract = createWalletInteract;
        this.defaultWalletInteract = defaultWalletInteract;
        this.findDefaultWalletInteract = findDefaultWalletInteract;
        this.getBalanceInteract = getBalanceInteract;
        this.fetchWalletInteract = fetchWalletInteract;
        this.exportWalletInteract = exportWalletInteract;
        this.sendTransactionInteract =  sendTransactionInteract;
        this.languageInteract = languageInteract;
        this.createOrImportRouter = createOrImportRouter;
        this.walletRepositoryType =  walletRepositoryType;
        this.signPersonInteract = signPersonInteract;
        this.sendRouter = sendRouter;
        this.web3Router = web3Router;
        this.backupRouter = backupRouter;
        this.importRouter = importRouter;
        this.receiverRouter = receiverRouter;
        this.walletRouter =  walletRouter;
        this.confirmSendRouter  = confirmSendRouter ;
        this.managerAccountsRouter =  managerAccountsRouter;
        this.passwordStore = passwordStore ;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new WalletViewModule(createWalletInteract,defaultWalletInteract,findDefaultWalletInteract,
                fetchWalletInteract,getBalanceInteract,
                exportWalletInteract,sendTransactionInteract,languageInteract,
                signPersonInteract,
                createOrImportRouter,sendRouter,
                managerAccountsRouter,web3Router,backupRouter,importRouter,
                receiverRouter,walletRouter,confirmSendRouter,walletRepositoryType,passwordStore);
    }
}
