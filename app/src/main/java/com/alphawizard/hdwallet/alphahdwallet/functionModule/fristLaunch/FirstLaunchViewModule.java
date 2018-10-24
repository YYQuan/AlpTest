package com.alphawizard.hdwallet.alphahdwallet.functionModule.fristLaunch;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.widget.Toast;

import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.data.Local;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.interact.CreateWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;
import com.alphawizard.hdwallet.common.base.ViewModule.BaseViewModel;
import com.alphawizard.hdwallet.common.base.ViewModule.entity.C;
import com.alphawizard.hdwallet.common.base.ViewModule.entity.ErrorEnvelope;
import com.alphawizard.hdwallet.common.util.Log;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class FirstLaunchViewModule extends BaseViewModel {

    CreateWalletInteract createWalletInteract ;
    WalletRouter  walletRouter;
    ImportRouter importRouter;
    DefaultWalletInteract defaultWalletInteract;

    private final MutableLiveData<Wallet> defaultWallet = new MutableLiveData<>();

    public FirstLaunchViewModule(CreateWalletInteract createWalletInteract,
                                 DefaultWalletInteract defaultWalletInteract,
                                 WalletRouter  walletRouter,
                                 ImportRouter importRouter)
    {
        this.createWalletInteract = createWalletInteract;
        this.defaultWalletInteract = defaultWalletInteract;
        this.walletRouter = walletRouter;
        this.importRouter = importRouter;
    }

    public LiveData<Wallet> defaultWallet() {
        return defaultWallet;
    }

    private final MutableLiveData<Wallet> createdWallet = new MutableLiveData<>();

    public LiveData<Wallet> createdWallet() {
        return createdWallet;
    }

    public void newWallet() {
        progress.setValue(true);
        createWalletInteract
                .create()
//				create 过程中没有throw 就回调success ,如果有throw  异常的话，那么就会掉error
                .subscribe(account -> {
//                    fetchWallets();
                    createdWallet.postValue(account);
                }, this::onCreateWalletError);
    }

    public void getDefaultWallet(){
        defaultWalletInteract
                .getDefaultWallet()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetDefaultWallet,this::onGetDefaultWalleterror);
    }

    private void onGetDefaultWalleterror(Throwable throwable) {
        App.showToast("onGetDefaultWallet error");
    }

    private void onGetDefaultWallet(Wallet wallet) {
        defaultWallet.postValue(wallet);
    }

    public void openWallet(Context context){
        walletRouter.open(context);
    }

    public void openImport(Context context){
        importRouter.open(context);
    }

    private void onCreateWalletError(Throwable throwable) {
        Log.d("onCreateWalletError" +throwable.getMessage());

//        createWalletError.postValue(new ErrorEnvelope(C.ErrorCode.UNKNOWN, null));
    }
}
