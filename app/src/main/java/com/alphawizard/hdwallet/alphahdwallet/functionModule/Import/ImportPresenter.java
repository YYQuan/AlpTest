package com.alphawizard.hdwallet.alphahdwallet.functionModule.Import;

import com.alphawizard.hdwallet.alphahdwallet.di.ActivityScoped;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.send.SendContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.send.SendViewModule;
import com.alphawizard.hdwallet.common.presenter.BasePresenter;

import javax.inject.Inject;

@ActivityScoped
public class ImportPresenter extends BasePresenter<ImportContract.View,ImportViewModule> implements ImportContract.Presenter {


    @Inject
    public ImportPresenter() {
    }


}
