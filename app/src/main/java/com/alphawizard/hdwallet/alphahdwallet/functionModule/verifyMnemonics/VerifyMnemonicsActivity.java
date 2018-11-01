package com.alphawizard.hdwallet.alphahdwallet.functionModule.verifyMnemonics;

import android.os.Bundle;
import android.app.Activity;

import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.BackupModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.VerifyMnemonicsModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupViewModule;
import com.alphawizard.hdwallet.common.presenter.BasePresenterActivity;

import javax.inject.Inject;

public class VerifyMnemonicsActivity extends BasePresenterActivity<BackupContract.Presenter,BackupViewModule> implements BackupContract.View {

    @Inject
    VerifyMnemonicsModuleFactory walletsViewModuleFactory;
    VerifyMnemonicsViewModule viewModel;

    @Inject
    VerifyMnemonicsContract.Presenter mPresenter;


    @Override
    public int getContentLayoutID() {
        return R.layout.activity_verify_mnemonics;
    }

    @Override
    public BackupContract.Presenter initPresenter() {
        return null;
    }

    @Override
    public BackupViewModule initViewModule() {
        return null;
    }

}
