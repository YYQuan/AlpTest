package com.alphawizard.hdwallet.alphahdwallet.functionModule.send;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.fristLaunch.FirstLaunchContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.fristLaunch.FirstLaunchViewModule;
import com.alphawizard.hdwallet.common.presenter.BaseContract;

public class SendContract {
    interface View extends BaseContract.BaseView<SendContract.Presenter,SendViewModule> {

    }

    interface Presenter extends BaseContract.BasePresenter<SendContract.View,SendViewModule> {
        void sendTransaction(String  to , String amount);
    }
}
