package com.alphawizard.hdwallet.alphahdwallet.functionModule.ManagerAccounts;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.CreateWalletEntity;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportActivity;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.WalletDetail.WalletDetailRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupRouter;
import com.alphawizard.hdwallet.alphahdwallet.interact.CreateWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.FetchWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.FindDefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.GetBalanceInteract;
import com.alphawizard.hdwallet.common.base.ViewModule.BaseViewModel;
import com.alphawizard.hdwallet.common.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ManagerAccountsViewModule extends BaseViewModel {

    
    DefaultWalletInteract mDefaultWalletInteract;
    FindDefaultWalletInteract mFindDefaultWalletInteract;
    FetchWalletInteract mFetchWalletInteract;
    CreateWalletInteract mCreateWalletInteract;
    WalletDetailRouter mWalletDetailRouter;
    GetBalanceInteract mGetBalanceInteract;
    ImportRouter mImportRouter;
    BackupRouter mBackupRouter;

    private final MutableLiveData<Wallet[]> wallets = new MutableLiveData<>();
    private final MutableLiveData<Wallet> createdWallet = new MutableLiveData<>();
    private final MutableLiveData<CreateWalletEntity> createWalletEntity = new MutableLiveData<>();
    private final MutableLiveData< HashMap<String,String>> accountsBalance = new MutableLiveData<>();


    CreateWalletEntity mEntity ;
    public ManagerAccountsViewModule(DefaultWalletInteract defaultWalletInteract,
                                     FindDefaultWalletInteract findDefaultWalletInteract,
                                     FetchWalletInteract fetchWalletInteract,
                                     CreateWalletInteract createWalletInteract,
                                     GetBalanceInteract getBalanceInteract,
                                     WalletDetailRouter walletDetailRouter,
                                     ImportRouter importRouter,
                                     BackupRouter backupRouter)
    {
        
        mDefaultWalletInteract = defaultWalletInteract;
        mFindDefaultWalletInteract = findDefaultWalletInteract;
        mFetchWalletInteract = fetchWalletInteract;
        mCreateWalletInteract = createWalletInteract;
        mGetBalanceInteract =  getBalanceInteract;
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

    public LiveData<CreateWalletEntity> createWalletEntity() {
        return createWalletEntity;
    }

    public LiveData< HashMap<String,String>> accountsBalance() {
        return accountsBalance;
    }



    public void getAccounts(){
        progress.setValue(true);
        mFetchWalletInteract
                .fetchAccounts()
                .subscribe(accounts->{
                    wallets.postValue(accounts);
                },this::onGetAccountsError);
    }


    private HashMap<String,String>  accountsBalanceMap = new HashMap<>();


    public void getAccountsBalance(){
        mFetchWalletInteract
                .fetchAccounts()
                .flatMap(w ->{
                    int i = 0;
                    for (;i<w.length;i++){
                        int finalI = i;

                        mGetBalanceInteract
                                .getBalance(w[i])
                                .subscribe(value->{
                                    accountsBalanceMap.put(w[finalI].address,value);
                                    },this::getBalanceError);
//                        accountsBalance.setValue(accountsBalanceMap);
                    }
                    while (accountsBalanceMap.get(w[w.length-1].address)==null){
                            Log.d("getAccountsBalance  running");
                    }
                    return Single.just(accountsBalanceMap);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(map->
                        accountsBalance.setValue(map),this::getBalanceError);
    }

    private void getBalanceError(Throwable throwable) {
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
