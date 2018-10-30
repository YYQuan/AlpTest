package com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importKeyStore;


import android.support.v7.util.DiffUtil;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Transaction;
import com.alphawizard.hdwallet.alphahdwallet.di.ActivityScoped;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Account.AccountContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.common.presenter.BasePresenter;
import com.alphawizard.hdwallet.common.presenter.BaseRecyclerPresenter;
import com.alphawizard.hdwallet.common.util.DiffUtilCallback;

import java.util.List;

import javax.inject.Inject;

@ActivityScoped
public class ImportKeyStorePresenter extends BasePresenter<ImportKeyStoreContract.View,ImportViewModule> implements ImportKeyStoreContract.Presenter {


    @Inject
    public ImportKeyStorePresenter() {
    }

    @Override
    public void importKeyStore(String keyStore,String password) {
        getViewModule().importKeystore(keyStore,password);
    }
}
