package com.alphawizard.hdwallet.alphahdwallet.functionModule.ConfirmSend;

import com.alphawizard.hdwallet.alphahdwallet.di.ActivityScoped;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupViewModule;
import com.alphawizard.hdwallet.common.presenter.BasePresenter;

import javax.inject.Inject;

import dagger.Binds;
@ActivityScoped
public class ConfirmSendPresenter extends BasePresenter<ConfirmSendContract.View,ConfirmSendViewModule> implements ConfirmSendContract.Presenter {

    @Inject
    public ConfirmSendPresenter() {
    }


}
