package com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importPrivateKey;


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
public class ImportPrivateKeyPresenter extends BasePresenter<ImportPrivateKeyContract.View,ImportViewModule> implements ImportPrivateKeyContract.Presenter {



    @Inject
    public ImportPrivateKeyPresenter() {
    }


    @Override
    public void importPrivateKey(String privateKey,String  name) {
        getViewModule().importPrivateKey(privateKey,name);
    }
}
