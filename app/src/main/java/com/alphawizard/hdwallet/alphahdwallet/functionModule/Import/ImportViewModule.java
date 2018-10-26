package com.alphawizard.hdwallet.alphahdwallet.functionModule.Import;

import android.content.Context;

import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.interact.ImportAccountInteract;
import com.alphawizard.hdwallet.common.base.ViewModule.BaseViewModel;
import com.alphawizard.hdwallet.common.util.Log;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class ImportViewModule extends BaseViewModel {

    ImportAccountInteract  mImportAccountInteract ;
    WalletRouter walletRouter;

    public ImportViewModule(ImportAccountInteract mImportAccountInteract, WalletRouter walletRouter) {
        this.mImportAccountInteract = mImportAccountInteract;
        this.walletRouter = walletRouter;
    }

    public  void importKeystore(String keystore ,String password){
        mImportAccountInteract
                .importKeystore(keystore,password)
//                必须 在主线程回调  否则 不能够  执行 progress.setValue(true);
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onWallet, this::onImportError);

    }

    public  void importPrivateKey(String privateKey ){
        mImportAccountInteract
                .importPrivateKey(privateKey,"123")
//                必须 在主线程回调  否则 不能够  执行 progress.setValue(true);
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onWallet, this::onImportError);

    }

    public  void importMnenonics(String mnenonics ){
        mImportAccountInteract
                .importMnenonics(mnenonics)
//                必须 在主线程回调  否则 不能够  执行 progress.setValue(true);
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onWallet, this::onImportError);

    }

    private void onImportError(Throwable throwable) {
        Log.d("import  fail ");
        App.showToast("import  fail");
    }

    private void onWallet(Wallet wallet) {
        progress.setValue(true);
    }

    public void  openWallet(Context context){
        walletRouter.open(context);
    }
}
