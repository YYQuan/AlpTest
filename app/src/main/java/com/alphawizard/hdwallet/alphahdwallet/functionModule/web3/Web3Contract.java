package com.alphawizard.hdwallet.alphahdwallet.functionModule.web3;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.send.SendViewModule;
import com.alphawizard.hdwallet.common.presenter.BaseContract;

public class Web3Contract {
    interface View extends BaseContract.BaseView<Web3Contract.Presenter,Web3ViewModule> {

    }

    interface Presenter extends BaseContract.BasePresenter<Web3Contract.View,Web3ViewModule> {

    }
}
