package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.CreateOrImport;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.common.presenter.BaseContract;

public interface CreateOrImportContract {
    interface View extends BaseContract.BaseView<CreateOrImportContract.Presenter,WalletViewModule> {

    }

    interface Presenter extends BaseContract.BasePresenter<CreateOrImportContract.View,WalletViewModule> {

    }
}
