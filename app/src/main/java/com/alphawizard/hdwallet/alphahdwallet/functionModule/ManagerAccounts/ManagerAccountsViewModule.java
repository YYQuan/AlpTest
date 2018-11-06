package com.alphawizard.hdwallet.alphahdwallet.functionModule.ManagerAccounts;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportActivity;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.WalletDetail.WalletDetailRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupRouter;
import com.alphawizard.hdwallet.alphahdwallet.interact.CreateWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.FetchWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.FindDefaultWalletInteract;
import com.alphawizard.hdwallet.common.base.ViewModule.BaseViewModel;

import java.util.ArrayList;

public class ManagerAccountsViewModule extends BaseViewModel {

    
    DefaultWalletInteract mDefaultWalletInteract;
    FindDefaultWalletInteract mFindDefaultWalletInteract;
    FetchWalletInteract mFetchWalletInteract;
    CreateWalletInteract mCreateWalletInteract;
    WalletDetailRouter mWalletDetailRouter;
    ImportRouter mImportRouter;
    BackupRouter mBackupRouter;

    private final MutableLiveData<Wallet[]> wallets = new MutableLiveData<>();
    private final MutableLiveData<Wallet> createdWallet = new MutableLiveData<>();
    private final MutableLiveData<CreateWalletInteract.CreateWalletEntity> createWalletEntity = new MutableLiveData<>();


    CreateWalletInteract.CreateWalletEntity mEntity ;
    public ManagerAccountsViewModule(DefaultWalletInteract defaultWalletInteract,
                                     FindDefaultWalletInteract findDefaultWalletInteract,
                                     FetchWalletInteract fetchWalletInteract,
                                     CreateWalletInteract createWalletInteract,
                                     WalletDetailRouter walletDetailRouter,
                                     ImportRouter importRouter,
                                     BackupRouter backupRouter)
    {
        
        mDefaultWalletInteract = defaultWalletInteract;
        mFindDefaultWalletInteract = findDefaultWalletInteract;
        mFetchWalletInteract = fetchWalletInteract;
        mCreateWalletInteract = createWalletInteract;
         mWalletDetailRouter = walletDetailRouter;
        mImportRouter =  importRouter;
        mBackupRouter =  backupRouter;
    }

    public LiveData<Wallet[]> wallets() {
        return wallets;
    }


    public LiveData<Wallet> createdWallet() {
        return createdWallet;
    }

    public LiveData<CreateWalletInteract.CreateWalletEntity> createWalletEntity() {
        return createWalletEntity;
    }

    public void getAccounts(){
        progress.setValue(true);
        mFetchWalletInteract
                .fetchAccounts()
                .subscribe(accounts->{
                    wallets.postValue(accounts);
                },this::onGetAccountsError);
    }

    private void onGetAccountsError(Throwable throwable) {
    }


    public void setDefaultWallet(Wallet wallet) {
        disposable = mDefaultWalletInteract
                .setDefaultWallet(wallet)
                .subscribe(() -> onDefaultWalletChanged(wallet), this::onError);
    }

    private void onDefaultWalletChanged(Wallet wallet) {
    }


    public void getDefaultWallet(){
        disposable = mFindDefaultWalletInteract
                .find()
                .subscribe(wallet -> onDefaultWalletChanged(wallet), this::onGetDefaultAccountsError);
    }

    private void onGetDefaultAccountsError(Throwable throwable) {
    }

    public void newWallet(String name) {
        progress.setValue(true);

        //        CreateWalletEntity
        mCreateWalletInteract
                .generatePassword()
                .flatMap(s->mCreateWalletInteract.generateMnenonics(s,name))
                .flatMap(e-> {
                    mEntity = e;
                    createWalletEntity.postValue(mEntity);
                    return mCreateWalletInteract.create(e); })
                .subscribe(this::onCreateWallet,this::onCreateWalletError);
    }

    private void onCreateWalletError(Throwable throwable) {
    }

    private void onCreateWallet(Wallet wallet) {
        createdWallet.postValue(wallet);
    }

    public void  openWalletDetail(Context context,String address ){
        mWalletDetailRouter.open(context,address);
    }

    public void  openImport(Context context ){
        mImportRouter.open(context);
    }


    public void  openBackup(Context context, ArrayList<String> list){
        mBackupRouter.open(context,list);
    }
}
