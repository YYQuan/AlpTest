package com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PasswordStore;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.verifyMnemonics.VerifyMnemonicsRouter;
import com.alphawizard.hdwallet.alphahdwallet.interact.CreateWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.utils.rx.Operators;
import com.alphawizard.hdwallet.common.base.ViewModule.BaseViewModel;
import com.alphawizard.hdwallet.common.util.Log;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BackupViewModule extends BaseViewModel {

    VerifyMnemonicsRouter verifyMnemonicsRouter;
    WalletRouter walletRouter;
    PasswordStore  passwordStore;
    DefaultWalletInteract defaultWalletInteract;


    private final MutableLiveData<Boolean> isOkSaveName = new MutableLiveData<>();

    public BackupViewModule(VerifyMnemonicsRouter verifyMnemonicsRouter,
                            WalletRouter walletRouter,
                            DefaultWalletInteract defaultWalletInteract,
                            PasswordStore  passwordStore)
    {
        this.verifyMnemonicsRouter =verifyMnemonicsRouter;
        this.walletRouter = walletRouter;
        this.defaultWalletInteract =  defaultWalletInteract;
        this.passwordStore =   passwordStore;
    }


    public LiveData<Boolean> getIsOkSaveName() {
        return isOkSaveName;
    }


    public void saveWalletName(String name){
        defaultWalletInteract.getDefaultWallet()
                .compose(Operators.saveWalletName(passwordStore, name))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::saveWalletNameSuccess ,this::saveWalletNameError);
    }

    private void saveWalletNameSuccess(Wallet wallet) {
        isOkSaveName.setValue(true);
    }




    private void saveWalletNameError(Throwable throwable) {
        isOkSaveName.setValue(false);
    }

    public void openVerify(Context context, ArrayList<String> strings){
        verifyMnemonicsRouter.open(context,strings);
    }


    public void openWalletPage(Context context){
//        walletRouter.openWalletPage(context);
        walletRouter.open(context);
    }

}
