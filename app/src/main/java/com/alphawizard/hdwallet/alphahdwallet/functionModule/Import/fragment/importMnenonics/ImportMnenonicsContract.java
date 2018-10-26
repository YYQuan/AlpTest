package com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importMnenonics;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportViewModule;
import com.alphawizard.hdwallet.common.presenter.BaseContract;

public interface ImportMnenonicsContract {

//     adapter 的泛型应该是 交易记录 ，而不是wallet  由于交易记录后面搞， 所以先放放。
    interface View extends BaseContract.BaseView<ImportMnenonicsContract.Presenter,ImportViewModule> {

    }

    interface Presenter extends BaseContract.BasePresenter<ImportMnenonicsContract.View,ImportViewModule> {
        void  importMnenonics(String mnenonics);
    }
}
