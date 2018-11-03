package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.setting;

import android.support.v7.util.DiffUtil;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.di.ActivityScoped;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Accounts.AccountsContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.common.presenter.BasePresenter;
import com.alphawizard.hdwallet.common.presenter.BaseRecyclerPresenter;
import com.alphawizard.hdwallet.common.util.DiffUtilCallback;

import java.util.List;

import javax.inject.Inject;

@ActivityScoped
public class SettingPresenter extends BasePresenter <SettingContract.View,WalletViewModule> implements SettingContract.Presenter {



    @Inject
    public SettingPresenter() {
    }






}
