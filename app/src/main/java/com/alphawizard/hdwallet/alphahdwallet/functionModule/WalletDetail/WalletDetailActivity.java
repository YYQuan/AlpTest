package com.alphawizard.hdwallet.alphahdwallet.functionModule.WalletDetail;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.VerifyMnemonicsModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.WalletDetailModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.verifyMnemonics.VerifyMnemonicsContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.verifyMnemonics.VerifyMnemonicsViewModule;
import com.alphawizard.hdwallet.common.presenter.BasePresenterActivity;
import com.alphawizard.hdwallet.common.presenter.BasePresenterToolbarActivity;

import javax.inject.Inject;

public class WalletDetailActivity extends BasePresenterToolbarActivity<WalletDetailContract.Presenter,WalletDetailViewModule> implements WalletDetailContract.View {

    @Inject
    WalletDetailModuleFactory walletsViewModuleFactory;
    WalletDetailViewModule viewModel;

    @Inject
    WalletDetailContract.Presenter mPresenter;

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_wallet_detail;
    }

    @Override
    public WalletDetailContract.Presenter initPresenter() {
        return mPresenter;
    }

    @Override
    public WalletDetailViewModule initViewModule() {
        return viewModel;
    }

    @Override
    public void initData() {
        super.initData();


        viewModel = ViewModelProviders.of(this, walletsViewModuleFactory)
                .get(WalletDetailViewModule.class);
//        viewModel.createdWallet().observe(this,this::onCreatedWallet);
    }




}
