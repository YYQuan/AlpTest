package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Account;

import android.graphics.Bitmap;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Transaction;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletActivityContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.common.presenter.BaseContract;

import java.util.List;

public interface AccountContract {

//     adapter 的泛型应该是 交易记录 ，而不是wallet  由于交易记录后面搞， 所以先放放。
    interface View extends BaseContract.BaseRecyclerView<AccountContract.Presenter,WalletViewModule,Transaction.TransactionBean> {

    }

    interface Presenter extends BaseContract.BasePresenter<AccountContract.View,WalletViewModule> {
        void getDefaultWallet();
        void getBalance();
        void getTransactions();
        void refresh(List<Transaction.TransactionBean> list);
        Bitmap createQRImage(String address, int imageSize);
    }
}
