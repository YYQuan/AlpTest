package com.alphawizard.hdwallet.alphahdwallet.functionModule.WalletDetail;

import android.os.Bundle;
import android.app.Activity;

import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.VerifyMnemonicsModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.WalletDetailModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.verifyMnemonics.VerifyMnemonicsContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.verifyMnemonics.VerifyMnemonicsViewModule;
import com.alphawizard.hdwallet.common.presenter.BasePresenterActivity;

import javax.inject.Inject;

public class WalletDetailActivity extends BasePresenterActivity<WalletDetailContract.Presenter,WalletDetailViewModule> implements WalletDetailContract.View {

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
        return null;
    }

    @Override
    public WalletDetailViewModule initViewModule() {
        return null;
    }

}
