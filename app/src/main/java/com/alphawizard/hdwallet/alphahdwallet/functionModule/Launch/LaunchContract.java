package com.alphawizard.hdwallet.alphahdwallet.functionModule.Launch;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.ConfirmSend.ConfirmSendViewModule;
import com.alphawizard.hdwallet.common.presenter.BaseContract;

public interface LaunchContract {
    interface View extends BaseContract.BaseView<LaunchContract.Presenter,LaunchViewModule> {
    }

    interface Presenter extends BaseContract.BasePresenter<LaunchContract.View,LaunchViewModule> {
    }
}
