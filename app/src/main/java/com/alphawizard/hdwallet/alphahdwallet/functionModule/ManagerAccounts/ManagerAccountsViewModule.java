package com.alphawizard.hdwallet.alphahdwallet.functionModule.ManagerAccounts;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.CreateWalletEntity;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PasswordStore;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.operators.observable.ObservableFromArray;
import io.reactivex.schedulers.Schedulers;
import rx.Observable;

public class ManagerAccountsViewModule extends BaseViewModel {

    
    DefaultWalletInteract mDefaultWalletInteract;
    FindDefaultWalletInteract mFindDefaultWalletInteract;
    FetchWalletInteract mFetchWalletInteract;
    CreateWalletInteract mCreateWalletInteract;
    WalletDetailRouter mWalletDetailRouter;
    GetBalanceInteract mGetBalanceInteract;
    ImportRouter mImportRouter;
    BackupRouter mBackupRouter;
    PasswordStore mPasswordStore;

    private final MutableLiveData<Wallet[]> wallets = new MutableLiveData<>();
    private final MutableLiveData<Wallet> createdWallet = new MutableLiveData<>();
    private final MutableLiveData<Wallet> defaultWallet = new MutableLiveData<>();
    private final MutableLiveData<Wallet> newAccountDefaultWallet = new MutableLiveData<>();
    private final MutableLiveData<CreateWalletEntity> createWalletEntity = new MutableLiveData<>();
    private final MutableLiveData< HashMap<String,String>> accountsBalance = new MutableLiveData<>();
    private final MutableLiveData< HashMap<String,String>> accountsName = new MutableLiveData<>();


    CreateWalletEntity mEntity ;
    public ManagerAccountsViewModule(DefaultWalletInteract defaultWalletInteract,
                                     FindDefaultWalletInteract findDefaultWalletInteract,
                                     FetchWalletInteract fetchWalletInteract,
                                     CreateWalletInteract createWalletInteract,
                                     GetBalanceInteract getBalanceInteract,
                                     WalletDetailRouter walletDetailRouter,
                                     ImportRouter importRouter,
                                     BackupRouter backupRouter,
                                     PasswordStore passwordStore)
    {
        
        mDefaultWalletInteract = defaultWalletInteract;
        mFindDefaultWalletInteract = findDefaultWalletInteract;
        mFetchWalletInteract = fetchWalletInteract;
        mCreateWalletInteract = createWalletInteract;
        mGetBalanceInteract =  getBalanceInteract;
         mWalletDetailRouter = walletDetailRouter;
        mImportRouter =  importRouter;
        mBackupRouter =  backupRouter;
        mPasswordStore = passwordStore;
    }

    public LiveData<Wallet[]> wallets() {
        return wallets;
    }


    public LiveData<Wallet> createdWallet() {
        return createdWallet;
    }
    public LiveData<Wallet> defaultWallet() {
        return defaultWallet;
    }

    public LiveData<Wallet> newAccountDefaultWallet() {
        return newAccountDefaultWallet;
    }

    public LiveData<CreateWalletEntity> createWalletEntity() {
        return createWalletEntity;
    }

    public LiveData< HashMap<String,String>> accountsBalance() {
        return accountsBalance;
    }

    public LiveData< HashMap<String,String>> accountsName() {
        return accountsName;
    }



    public void getAccounts(){
        progress.postValue(true);
        mFetchWalletInteract
                .fetchAccounts()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(accounts->{
                    wallets.postValue(accounts);
                },this::onGetAccountsError);
    }


    private HashMap<String,String>  accountsBalanceMap = new HashMap<>();
    private HashMap<String,String>  accountsNameMap = new HashMap<>();


    public void cancelGetAccountsBalance(){
        cancel();
    }



    public void getAccountsBalance(){

        mFetchWalletInteract
                .fetchAccounts()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(this::getBalances,this::getBalanceError);

    }

    private void getBalances(Wallet[] wallets) {
        Observable.from(wallets)
                .doOnNext(wallet -> {
                    mGetBalanceInteract
                            .getBalance(wallet)
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .doAfterSuccess(m->accountsBalance.postValue(accountsBalanceMap))
                            .subscribe(value->{
                                accountsBalanceMap.put(wallet.address,value);
                            },this::getBalanceError);
                })
                .subscribe();

    }


    public void getAccountsName(){
        mFetchWalletInteract
                .fetchAccounts()
                .observeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(this::getNames,this::getNameError);
    }

    private void getNames(Wallet[] wallets) {

        Observable.from(wallets)
                .doOnNext(wallet -> {

                    mPasswordStore.getWalletName(wallet)
                                .observeOn(Schedulers.io())
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .doAfterSuccess(m->accountsName.postValue(accountsNameMap))
                                .subscribe(s -> accountsNameMap.put(wallet.address,s));
                })
                .subscribe();


    }

    private void getNameError(Throwable throwable) {
    }


    private void getBalanceError(Throwable throwable) {
    }

    private void onGetAccountsError(Throwable throwable) {
    }


    public void setDefaultWallet(Wallet wallet) {
         mDefaultWalletInteract
                .setDefaultWallet(wallet)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(() -> onDefaultWalletChanged(wallet), this::onError);
    }

    public void setNewAccountDefaultWallet(Wallet wallet) {
         mDefaultWalletInteract
                .setDefaultWallet(wallet)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(() -> newAccountDefaultWallet.postValue(wallet), this::onError);
    }

    private void onDefaultWalletChanged(Wallet wallet) {
        defaultWallet.postValue(wallet) ;
    }


    public void getDefaultWallet(){
        disposable = mFindDefaultWalletInteract
                .find()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(wallet -> onDefaultWalletChanged(wallet), this::onGetDefaultAccountsError);
    }

    private void onGetDefaultAccountsError(Throwable throwable) {
    }

    public void newWallet() {
        progress.postValue(true);

        //        CreateWalletEntity
        mCreateWalletInteract
                .generatePassword()
                .flatMap(s->
                        mPasswordStore.generateWalletName()
                                .flatMap(n ->mCreateWalletInteract.generateMnenonics(s,n))
                        )
                .flatMap(e-> {
                    mEntity = e;
                    createWalletEntity.postValue(mEntity);
                    return mCreateWalletInteract.create(e); })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(this::onCreateWallet,this::onCreateWalletError);
    }

    private void onCreateWalletError(Throwable throwable) {
    }

    private void onCreateWallet(Wallet wallet) {
        createdWallet.postValue(wallet);
    }

    private void onDefaultWallet(Wallet wallet) {
        defaultWallet.postValue(wallet);
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
