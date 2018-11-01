package com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics;

import android.os.Bundle;
import android.app.Activity;

import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.BackupModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.FirstLaunchViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.fristLaunch.FirstLaunchContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.fristLaunch.FirstLaunchViewModule;
import com.alphawizard.hdwallet.common.presenter.BasePresenterActivity;

import javax.inject.Inject;

public class BackupMnemonicsActivity extends BasePresenterActivity<BackupContract.Presenter,BackupViewModule> implements BackupContract.View {

    @Inject
    BackupModuleFactory walletsViewModuleFactory;
    BackupViewModule viewModel;

    @Inject
    BackupContract.Presenter mPresenter;

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_backup_mnemonics;
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
