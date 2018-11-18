package com.alphawizard.hdwallet.alphahdwallet.functionModule.WalletDetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PasswordStore;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.interact.ExportWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.GetBalanceInteract;
import com.alphawizard.hdwallet.common.base.ViewModule.BaseViewModel;

public class WalletDetailViewModule extends BaseViewModel {

    GetBalanceInteract mGetBalanceInteract;
    ExportWalletInteract mExportWalletInteract;
    PasswordStore mPasswordStore;
    WalletRouter mWalletRouter;
    private final MutableLiveData<String> walletBalance = new MutableLiveData<>();

    private final MutableLiveData<String> exportPrivateKey = new MutableLiveData<>();
    private final MutableLiveData<String> exportKeyStore = new MutableLiveData<>();
    private final MutableLiveData<String> exportMnemonics = new MutableLiveData<>();
    private final MutableLiveData<String> walletName = new MutableLiveData<>();
    private final MutableLiveData<Boolean> hasMnemonics = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isFailExport = new MutableLiveData<>();
    private final MutableLiveData<String> passwordString = new MutableLiveData<>();


    public WalletDetailViewModule(GetBalanceInteract getBalanceInteract,
                                  ExportWalletInteract exportWalletInteract,
                                  WalletRouter walletRouter,
                                  PasswordStore passwordStore)
    {
        mGetBalanceInteract =  getBalanceInteract;
        mExportWalletInteract = exportWalletInteract;
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

    public LiveData<String> passwordString() {
        return passwordString;
    }


    void getBalance(String address) {
        mGetBalanceInteract
                .getBalance(new Wallet(address))
                .subscribe(walletBalance::postValue, this::onGetDefaultBalanceError);
    }

    private void onGetDefaultBalanceError(Throwable throwable) {
    }

    public void  exportPrivatekey(String address){
        mExportWalletInteract.exportPrivateKey(new Wallet(address))
                .subscribe(s->exportPrivateKey.postValue(s),this::exportError);
    }



    public void  exportKeystore(String address){
        mExportWalletInteract.exportKeystore(new Wallet(address),"123")
                .subscribe(s->exportKeyStore.postValue(s),this::exportError);
    }

    public void  hasMnemonics(String address){
        hasMnemonics.setValue(true);
        mExportWalletInteract.exportMnemonics(new Wallet(address))
                .subscribe(s->hasMnemonics.postValue(true),s->hasMnemonics.postValue(false));
    }

    public void  exportMnemonics(String address){
        mExportWalletInteract.exportMnemonics(new Wallet(address))
                .subscribe(s->exportMnemonics.postValue(s),this::exportError);
    }

    public void getWalletName(String address){
        mPasswordStore.getWalletName(new Wallet(address))
                    .subscribe(s->walletName.postValue(s) ,this::getWalletNameError);
    }

    public void saveWalletName(String address ,String name){
        progress.setValue(false);
        mPasswordStore.setWalletName(new Wallet(address),name)
                .subscribe(this::saveWalletNameSuccess ,this::saveWalletNameError);
    }

    public void getPassword(Wallet wallet){
        mPasswordStore.getPassword(wallet)
                .subscribe(s->passwordString.setValue(s),this::getPasswordError);
    }

    private void getPasswordError(Throwable throwable) {
    }


    private void saveWalletNameSuccess() {
        progress.setValue(true);
    }


    private void saveWalletNameError(Throwable throwable) {
    }

    private void getWalletNameError(Throwable throwable) {

    }

    private void exportError(Throwable throwable) {
        isFailExport.setValue(false);
    }

    public void openWallet(Context context){
        mWalletRouter.openWalletPage(context);
    }

}
