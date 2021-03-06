package com.alphawizard.hdwallet.alphahdwallet.functionModule.WalletDetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PasswordStore;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.DeleteWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.ExportWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.FetchWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.FindDefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.GetBalanceInteract;
import com.alphawizard.hdwallet.common.base.ViewModule.BaseViewModel;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class WalletDetailViewModule extends BaseViewModel {


    GetBalanceInteract mGetBalanceInteract;
    ExportWalletInteract mExportWalletInteract;
    PasswordStore mPasswordStore;
    WalletRouter mWalletRouter;
    DeleteWalletInteract mDeleteWalletInteract;
    FetchWalletInteract mFetchWalletInteract;
    DefaultWalletInteract mDefaultWalletInteract;
    private final MutableLiveData<String> walletBalance = new MutableLiveData<>();

    private final MutableLiveData<String> exportPrivateKey = new MutableLiveData<>();
    private final MutableLiveData<String> exportKeyStore = new MutableLiveData<>();
    private final MutableLiveData<String> exportMnemonics = new MutableLiveData<>();
    private final MutableLiveData<String> walletName = new MutableLiveData<>();
    private final MutableLiveData<Boolean> hasMnemonics = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isFailExport = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isOkDelete = new MutableLiveData<>();
    private final MutableLiveData<String> passwordString = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isDefaultWallet = new MutableLiveData<>();


    public WalletDetailViewModule(GetBalanceInteract getBalanceInteract,
                                  ExportWalletInteract exportWalletInteract,
                                  DeleteWalletInteract deleteWalletInteract,
                                  FetchWalletInteract fetchWalletInteract,
                                  DefaultWalletInteract defaultWalletInteract,
                                  WalletRouter walletRouter,
                                  PasswordStore passwordStore)
    {
        mGetBalanceInteract =  getBalanceInteract;
        mExportWalletInteract = exportWalletInteract;
        mDeleteWalletInteract = deleteWalletInteract;
        mFetchWalletInteract =  fetchWalletInteract;
        mDefaultWalletInteract =  defaultWalletInteract;
        mPasswordStore = passwordStore;
        mWalletRouter = walletRouter;
    }

    public LiveData<String> walletBalance() {
        return walletBalance;
    }
    public LiveData<String> exportPrivateKeyString() {
        return exportPrivateKey;
    }
    public LiveData<String> exportKeyStoreString() {
        return exportKeyStore;
    }
    public LiveData<String> exportMnemonicsString() {
        return exportMnemonics;
    }
    public LiveData<String> walletNameString() {
        return walletName;
    }

    public LiveData<Boolean> hasMnemonicsString() {
        return hasMnemonics;
    }
    public LiveData<Boolean> isFailExportContent() {
        return isFailExport;
    }

    public LiveData<Boolean> isOkDeleteContent() {
        return isOkDelete;
    }
    public LiveData<String> passwordString() {
        return passwordString;
    }

    public LiveData<Boolean> isDefaultWallet() {
        return isDefaultWallet;
    }

    void getBalance(String address) {
        mGetBalanceInteract
                .getBalance(new Wallet(address))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(walletBalance::postValue, this::onGetDefaultBalanceError);
    }

    void deleteWallet(Wallet wallet ,String password){
//        mDeleteWalletInteract.deleteWallet(wallet,password)
//                .observeOn(Schedulers.io())
//                .subscribe(()->deleteWalletSuccess(wallet),this::deleteWalletError);
        mDeleteWalletInteract.delete(wallet,password)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(this::onFetchWallets, this::onDeleteError);
    }

    private void onDeleteError(Throwable throwable) {
    }

    private void onFetchWallets(Wallet[] items) {

        if(items.length<=0){
            mDefaultWalletInteract.clearDefaultWallet();
            isOkDelete.postValue(true);
        }else{
            isOkDelete.postValue(true);
        }

    }









    private void onGetDefaultBalanceError(Throwable throwable) {
    }

    public void  exportPrivatekey(String address){
        mExportWalletInteract.exportPrivateKey(new Wallet(address))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(s->exportPrivateKey.postValue(s),this::exportError);
    }



    public void  exportKeystore(String address){
        mExportWalletInteract.exportKeystore(new Wallet(address),"123")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(s->exportKeyStore.postValue(s),this::exportError);
    }

    public void  getDefaultWallet(String address){
        mDefaultWalletInteract.getDefaultWallet()
                .flatMap(s ->{
                    if(s.address.equalsIgnoreCase(address)) {
                        return Single.just(true);
                    }
                    return Single.just(false);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(s->isDefaultWallet.postValue(s),this::getDefaultWalletError);
    }

    private void getDefaultWalletError(Throwable throwable) {
    }

    public void  hasMnemonics(String address){
        hasMnemonics.postValue(true);
        mExportWalletInteract.exportMnemonics(new Wallet(address))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(s->hasMnemonics.postValue(true),s->hasMnemonics.postValue(false));
    }

    public void  exportMnemonics(String address){
        mExportWalletInteract.exportMnemonics(new Wallet(address))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(s->exportMnemonics.postValue(s),this::exportError);
    }

    public void getWalletName(String address){
        mPasswordStore.getWalletName(new Wallet(address))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                    .subscribe(s->walletName.postValue(s) ,this::getWalletNameError);
    }

    public void saveWalletName(String address ,String name){
        progress.postValue(false);
        mPasswordStore.setWalletName(new Wallet(address),name)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(this::saveWalletNameSuccess ,this::saveWalletNameError);
    }

    public void getPassword(Wallet wallet){
        mPasswordStore.getPassword(wallet)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(s->passwordString.postValue(s),this::getPasswordError);
    }

    private void getPasswordError(Throwable throwable) {
    }


    private void saveWalletNameSuccess() {
        progress.postValue(true);
    }


    private void saveWalletNameError(Throwable throwable) {
    }

    private void getWalletNameError(Throwable throwable) {

    }

    private void exportError(Throwable throwable) {
        isFailExport.postValue(false);
    }

    public void openWallet(Context context){
        mWalletRouter.openWalletPage(context);
    }

}
