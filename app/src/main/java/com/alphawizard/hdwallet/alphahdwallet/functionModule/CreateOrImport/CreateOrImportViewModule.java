package com.alphawizard.hdwallet.alphahdwallet.functionModule.CreateOrImport;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.CreateWalletEntity;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupRouter;
import com.alphawizard.hdwallet.alphahdwallet.interact.CreateWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;
import com.alphawizard.hdwallet.common.base.ViewModule.BaseViewModel;
import com.alphawizard.hdwallet.common.util.Log;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class CreateOrImportViewModule extends BaseViewModel {

    CreateWalletInteract createWalletInteract ;
    WalletRouter  walletRouter;
    ImportRouter importRouter;
    DefaultWalletInteract defaultWalletInteract;
    BackupRouter backupRouter;



    private final MutableLiveData<Wallet> defaultWallet = new MutableLiveData<>();
    private final MutableLiveData<Wallet> createdWallet = new MutableLiveData<>();
    private final MutableLiveData<CreateWalletEntity> createWalletEntity = new MutableLiveData<>();

    public CreateOrImportViewModule(CreateWalletInteract createWalletInteract,
                                    DefaultWalletInteract defaultWalletInteract,
                                    WalletRouter  walletRouter,
                                    BackupRouter backupRouter,
                                    ImportRouter importRouter)
    {
        this.createWalletInteract = createWalletInteract;
        this.defaultWalletInteract = defaultWalletInteract;
        this.walletRouter = walletRouter;
        this.backupRouter =  backupRouter;
        this.importRouter = importRouter;
    }

    public LiveData<Wallet> defaultWallet() {
        return defaultWallet;
    }

    public LiveData<CreateWalletEntity> createWalletEntity() {
        return createWalletEntity;
    }

    public LiveData<Wallet> createdWallet() {
        return createdWallet;
    }

    public void newWallet(String name) {
        progress.setValue(true);

        //        CreateWalletEntity
        createWalletInteract
                        .generatePassword()
                        .flatMap(s->createWalletInteract.generateMnenonics(s,name))
                        .flatMap(e-> {
                            createWalletEntity.postValue(e);
                            return createWalletInteract.create(e); })
                    .subscribe(this::onCreateWallet,this::onCreateWalletError);
    }

    private void onCreateWallet(Wallet wallet) {
        createdWallet.postValue(wallet);
    }

    public void getDefaultWallet(){
        defaultWalletInteract
                .getDefaultWallet()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetDefaultWallet,this::onGetDefaultWalleterror);
    }

    private void onGetDefaultWalleterror(Throwable throwable) {
//        App.showToast("onGetDefaultWallet error");
    }

    private void onGetDefaultWallet(Wallet wallet) {
        defaultWallet.postValue(wallet);
    }

    public void openWallet(Context context){
        walletRouter.openWalletPage(context);
    }

    public void openImport(Context context){
        importRouter.open(context);
    }

    public void openBackup(Context context,ArrayList<String> strings){
        backupRouter.open(context,strings);
    }


    private void onGeneratePasswordError(Throwable throwable) {
        Log.d("onGeneratePasswordError" +throwable.getMessage());
    }

    private void onGenerateMnenonicsError(Throwable throwable) {
        Log.d("onGenerateMnenonicsError" +throwable.getMessage());
    }

    private void onCreateWalletError(Throwable throwable) {
        Log.d("onCreateWalletError" +throwable.getMessage());

//        createWalletError.postValue(new ErrorEnvelope(C.ErrorCode.UNKNOWN, null));
    }


}
