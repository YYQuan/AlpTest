package com.alphawizard.hdwallet.alphahdwallet.functionModule.fristLaunch;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.alphawizard.hdwallet.alphahdwallet.R;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.FirstLaunchViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.interact.CreateWalletInteract;
import com.alphawizard.hdwallet.common.presenter.BasePresenterToolbarActivity;
import com.alphawizard.hdwallet.common.util.Log;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class FirstLaunchActivity extends BasePresenterToolbarActivity<FirstLaunchContract.Presenter,FirstLaunchViewModule> implements FirstLaunchContract.View {

    @BindView(R.id.btn_create_account)
    Button btnCreate;

    @BindView(R.id.btn_import_account)
    Button btnImportAccount;

    @Inject
    FirstLaunchViewModuleFactory walletsViewModuleFactory;
    FirstLaunchViewModule viewModel;

    @Inject
    FirstLaunchContract.Presenter mPresenter;

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_first_launch;
    }

    @Override
    public FirstLaunchContract.Presenter initPresenter() {
        return mPresenter;
    }

    @Override
    public FirstLaunchViewModule initViewModule() {
        return viewModel;
    }

    @Override
    public void initData() {
        super.initData();

        viewModel = ViewModelProviders.of(this, walletsViewModuleFactory)
                .get(FirstLaunchViewModule.class);
        mPresenter.takeView(this,viewModel);

        viewModel.createdWallet().observe(this,this::onCreatedWallet);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.btn_create_account)
    void onClickBtnCreate(){
        mPresenter.createWallet();
    }

    @OnClick(R.id.btn_import_account)
    void onClickBtnImport(){
        viewModel.openImport(this);
    }

    @Override
    public void onCreatedWallet(Wallet wallet) {
        Log.d("onCreatedWallet");
        viewModel.openWallet(this);
    }
}
