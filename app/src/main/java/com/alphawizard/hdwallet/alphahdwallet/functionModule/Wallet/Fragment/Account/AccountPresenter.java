package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Account;


import android.support.v7.util.DiffUtil;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Transaction;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.di.ActivityScoped;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.common.presenter.BaseRecyclerPresenter;
import com.alphawizard.hdwallet.common.util.DiffUtilCallback;

import java.util.List;

import javax.inject.Inject;
@ActivityScoped
public class AccountPresenter extends BaseRecyclerPresenter<Transaction.TransactionBean,WalletViewModule,AccountContract.View> implements AccountContract.Presenter {

    private boolean isStartGetBalance  =false;

    @Inject
    public AccountPresenter() {
    }

    @Override
    public void getDefaultWallet() {
        getViewModule().getDefaultWallet();
    }

    @Override
    public void getBalance() {
        if(!isStartGetBalance) {
            isStartGetBalance = true;
            getViewModule().getBalance();
        }
    }

    @Override
    public void getTransactions(){
        getViewModule().fetchTransactions();
    }

    public void refresh(List<Transaction.TransactionBean> list){
        // 差异对比
        List<Transaction.TransactionBean> old = getView().getRecyclerViewAdapter().getDataList();
        DiffUtilCallback<Transaction.TransactionBean> callback = new DiffUtilCallback<>(old, list);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        refreshData(result,list);
    }
}
