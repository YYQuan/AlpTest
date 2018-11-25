package com.alphawizard.hdwallet.alphahdwallet.functionModule.Import;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.WalletExistException;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PasswordStore;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.ImportAccountInteract;
import com.alphawizard.hdwallet.common.base.ViewModule.BaseViewModel;
import com.alphawizard.hdwallet.common.util.Log;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class ImportViewModule extends BaseViewModel {

    ImportAccountInteract  mImportAccountInteract ;
    DefaultWalletInteract defaultWalletInteract ;
    WalletRouter walletRouter;

    private final MutableLiveData<Boolean> isOkchangeDefaultWallet = new MutableLiveData<>();
    private final MutableLiveData<Wallet> importWallet = new MutableLiveData<>();

    public ImportViewModule(ImportAccountInteract mImportAccountInteract,
                            DefaultWalletInteract defaultWalletInteract,
                            WalletRouter walletRouter
                            ) {
        this.mImportAccountInteract = mImportAccountInteract;
        this.walletRouter = walletRouter;
        this.defaultWalletInteract = defaultWalletInteract;
    }

    public LiveData< Boolean> changeDefaultWallet() {
        return isOkchangeDefaultWallet;
    }
    public LiveData< Wallet> importWallet() {
        return importWallet;
    }

    public void setDefaultWallet(Wallet wallet){
        defaultWalletInteract.setDefaultWallet(wallet)
                .subscribe(()->isOkchangeDefaultWallet.postValue(true),this::changeDefaultWalletError);
    }

    private void changeDefaultWalletError(Throwable throwable) {

    }

    public  void importKeystore(String keystore,String password,String name){

        mImportAccountInteract
                .importKeystore(keystore,password,name)
//                必须 在主线程回调  否则 不能够  执行 progress.setValue(true);
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onWallet, this::onImportError);

    }

    public  void importPrivateKey(String privateKey,String name){
        mImportAccountInteract
                .importPrivateKey(privateKey,name)
//                必须 在主线程回调  否则 不能够  执行 progress.setValue(true);
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onWallet, this::onImportError);

    }

    public  void importMnenonics(String mnenonics ,String name){
        mImportAccountInteract
                .importMnenonics(mnenonics,name)
//                必须 在主线程回调  否则 不能够  执行 progress.setValue(true);
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onWallet, this::onImportError);
    }

    private void onImportError(Throwable throwable) {
        Log.d("import  fail ");

        if(WalletExistException.DEFAULT_ALL_INFO.equalsIgnoreCase(throwable.getMessage())){
            App.showToast("账户已经被导入");
        }else{
            App.showToast("填入信息有误");
        }



    }

    private void onWallet(Wallet wallet) {
        importWallet.setValue(wallet);
        progress.setValue(true);
        App.showToast("导入成功");
    }

    public void  openWallet(Context context){
        walletRouter.openWalletPage(context);
    }
}
