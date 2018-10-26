package com.alphawizard.hdwallet.alphahdwallet.functionModule.Import;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.fristLaunch.FirstLaunchContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.fristLaunch.FirstLaunchViewModule;
import com.alphawizard.hdwallet.common.presenter.BaseContract;

public interface ImportContract {
    interface View extends BaseContract.BaseView<ImportContract.Presenter,ImportViewModule> {

    }

    interface Presenter extends BaseContract.BasePresenter<ImportContract.View,ImportViewModule> {
        void importKeystore(String keystore ,String password);
        void importPrivateKey(String privateKey);

    }
}
