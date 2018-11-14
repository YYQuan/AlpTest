package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.CreateOrImport;

import com.alphawizard.hdwallet.alphahdwallet.di.ActivityScoped;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.dapp.DappContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.common.presenter.BasePresenter;

import javax.inject.Inject;

@ActivityScoped
public class CreateOrImportPresenter extends BasePresenter<CreateOrImportContract.View,WalletViewModule> implements CreateOrImportContract.Presenter {

    @Inject
    public CreateOrImportPresenter() {
    }
}
