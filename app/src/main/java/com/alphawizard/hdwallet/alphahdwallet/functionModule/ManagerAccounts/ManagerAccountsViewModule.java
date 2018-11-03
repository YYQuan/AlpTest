package com.alphawizard.hdwallet.alphahdwallet.functionModule.ManagerAccounts;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.WalletDetail.WalletDetailRouter;
import com.alphawizard.hdwallet.alphahdwallet.interact.CreateWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.FetchWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.FindDefaultWalletInteract;
import com.alphawizard.hdwallet.common.base.ViewModule.BaseViewModel;

public class ManagerAccountsViewModule extends BaseViewModel {

    
    DefaultWalletInteract mDefaultWalletInteract;
    FindDefaultWalletInteract mFindDefaultWalletInteract;
    FetchWalletInteract mFetchWalletInteract;
    WalletDetailRouter mWalletDetailRouter;

    private final MutableLiveData<Wallet[]> wallets = new MutableLiveData<>();

    public ManagerAccountsViewModule(DefaultWalletInteract defaultWalletInteract,
                                     FindDefaultWalletInteract findDefaultWalletInteract,
                                     FetchWalletInteract fetchWalletInteract,
                                     WalletDetailRouter walletDetailRouter)
    {
        
        mDefaultWalletInteract = defaultWalletInteract;
        mFindDefaultWalletInteract = findDefaultWalletInteract;
        mFetchWalletInteract = fetchWalletInteract;
         mWalletDetailRouter = walletDetailRouter;
    }

    public LiveData<Wallet[]> wallets() {
        return wallets;
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



    public void  openWalletDetail(Context context ){
        mWalletDetailRouter.open(context);
    }

}
