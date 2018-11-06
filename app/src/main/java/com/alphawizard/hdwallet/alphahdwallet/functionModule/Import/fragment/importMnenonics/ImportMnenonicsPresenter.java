package com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importMnenonics;


import com.alphawizard.hdwallet.alphahdwallet.di.ActivityScoped;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importKeyStore.ImportKeyStoreContract;
import com.alphawizard.hdwallet.common.presenter.BasePresenter;

import javax.inject.Inject;

@ActivityScoped
public class ImportMnenonicsPresenter extends BasePresenter<ImportMnenonicsContract.View,ImportViewModule> implements ImportMnenonicsContract.Presenter {

    @Inject
    public ImportMnenonicsPresenter() {
    }

    @Override
    public void importMnenonics(String mnenonics,String name) {
        getViewModule().importMnenonics(mnenonics,name);
    }
}
