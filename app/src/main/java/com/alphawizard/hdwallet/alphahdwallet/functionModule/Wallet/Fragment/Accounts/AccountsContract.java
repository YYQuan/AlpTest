package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Accounts;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Account.AccountContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.common.presenter.BaseContract;

import java.util.List;

public interface AccountsContract {
    interface View extends BaseContract.BaseRecyclerView<AccountsContract.Presenter,WalletViewModule,Wallet> {
        void defaultWalletChange(Wallet wallet);
        void openFirstLaunch();
    }

    interface Presenter extends BaseContract.BasePresenter<AccountsContract.View,WalletViewModule> {
        void  getWallets();
        void  setDefaultWallet(Wallet wallet);
        void  getDefaultWallet();
        String getDefauleWalletAddress();
        void onDefaultWalletChange(Wallet wallet);
        void refresh(List<Wallet>  list);
    }
}
