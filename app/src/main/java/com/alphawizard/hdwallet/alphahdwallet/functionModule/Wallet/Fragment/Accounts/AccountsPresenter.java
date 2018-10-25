package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Accounts;

import android.support.v7.util.DiffUtil;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.di.ActivityScoped;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Account.AccountFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.common.presenter.BaseRecyclerPresenter;
import com.alphawizard.hdwallet.common.util.DiffUtilCallback;

import java.util.List;

import javax.inject.Inject;

@ActivityScoped
public class AccountsPresenter extends BaseRecyclerPresenter <Wallet,WalletViewModule,AccountsContract.View> implements AccountsContract.Presenter {

    private Wallet defaultWallet ;

    @Inject
    public AccountsPresenter() {
    }



    @Override
    public void getWallets() {
        WalletViewModule viewModule = getViewModule();
        viewModule.getAccounts();
    }

    @Override
    public void setDefaultWallet(Wallet wallet) {
        WalletViewModule viewModule = getViewModule();
        viewModule.setDefaultWallet(wallet);
    }

    @Override
    public void getDefaultWallet() {
        WalletViewModule viewModule = getViewModule();
        viewModule.getDefaultWallet();
    }

    @Override
    public String getDefauleWalletAddress(){
        if(defaultWallet!=null) {
            return defaultWallet.address;
        }
        else{
            return "";
        }
    }


    @Override
    public void onDefaultWalletChange(Wallet wallet) {
        defaultWallet =  wallet;
    }


    public void refresh(List<Wallet>  list) {
        // 差异对比
        List<Wallet> old = getView().getRecyclerViewAdapter().getDataList();
        DiffUtilCallback<Wallet> callback = new DiffUtilCallback<>(old, list);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        refreshData(result,list);
    }

}
