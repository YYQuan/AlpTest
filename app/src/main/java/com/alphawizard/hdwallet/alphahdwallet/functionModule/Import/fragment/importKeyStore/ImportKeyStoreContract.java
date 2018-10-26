package com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importKeyStore;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Transaction;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.common.presenter.BaseContract;

import java.util.List;

public interface ImportKeyStoreContract {

//     adapter 的泛型应该是 交易记录 ，而不是wallet  由于交易记录后面搞， 所以先放放。
    interface View extends BaseContract.BaseView<ImportKeyStoreContract.Presenter,ImportViewModule> {

    }

    interface Presenter extends BaseContract.BasePresenter<ImportKeyStoreContract.View,ImportViewModule> {
        void  importKeyStore(String keyStore,String password);
    }
}
