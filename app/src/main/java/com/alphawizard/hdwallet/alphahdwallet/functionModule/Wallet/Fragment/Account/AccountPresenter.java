package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Account;


import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.common.presenter.BaseRecyclerPresenter;

import javax.inject.Inject;

public class AccountPresenter extends BaseRecyclerPresenter<Wallet,WalletViewModule,AccountContract.View> implements AccountContract.Presenter {


    @Inject
    public AccountPresenter() {
    }

    @Override
    public void getDefaultWallet() {
        getViewModule().getDefaultWallet();
    }

    @Override
    public void getBalance() {
        getViewModule().getBalance();
    }

    @Override
    public void getTransactions(){
        getViewModule().fetchTransactions();
    }
}
