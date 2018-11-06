package com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importPrivateKey;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Transaction;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.common.presenter.BaseContract;

import java.util.List;

public interface ImportPrivateKeyContract {

//     adapter 的泛型应该是 交易记录 ，而不是wallet  由于交易记录后面搞， 所以先放放。
    interface View extends BaseContract.BaseView<ImportPrivateKeyContract.Presenter, ImportViewModule> {

    }

    interface Presenter extends BaseContract.BasePresenter<ImportPrivateKeyContract.View,ImportViewModule> {
        void importPrivateKey(String privateKey,String  name);
    }
}
