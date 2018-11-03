package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.dapp;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Accounts.AccountsContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.common.presenter.BaseContract;

import java.util.List;

public interface DappContract {
    interface View extends BaseContract.BaseView<DappContract.Presenter,WalletViewModule> {

    }

    interface Presenter extends BaseContract.BasePresenter<DappContract.View,WalletViewModule> {

    }
}
