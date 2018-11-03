package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.setting;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Accounts.AccountsContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.common.presenter.BaseContract;

import java.util.List;

public interface SettingContract {
    interface View extends BaseContract.BaseView<SettingContract.Presenter,WalletViewModule> {

    }

    interface Presenter extends BaseContract.BasePresenter<SettingContract.View,WalletViewModule> {

    }
}
