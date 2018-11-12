package com.alphawizard.hdwallet.alphahdwallet.functionModule.Import;

import com.alphawizard.hdwallet.common.presenter.BaseContract;

public interface ImportContract {
    interface View extends BaseContract.BaseView<ImportContract.Presenter,ImportViewModule> {

    }

    interface Presenter extends BaseContract.BasePresenter<ImportContract.View,ImportViewModule> {

    }
}
