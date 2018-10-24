package com.alphawizard.hdwallet.alphahdwallet.functionModule.Import;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;

import com.alphawizard.hdwallet.alphahdwallet.R;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.FirstLaunchViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.ImportViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.fristLaunch.FirstLaunchContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.fristLaunch.FirstLaunchViewModule;
import com.alphawizard.hdwallet.common.presenter.BasePresenterToolbarActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class ImportActivity extends BasePresenterToolbarActivity<ImportContract.Presenter,ImportViewModule> implements ImportContract.View  {

    @Inject
    ImportViewModuleFactory walletsViewModuleFactory;
    ImportViewModule viewModel;

    @Inject
    ImportContract.Presenter mPresenter;

    @BindView(R.id.ed_keystore)
    EditText mKeystore;

    @BindView(R.id.ed_password)
    EditText mPassword;

    @BindView(R.id.btn_import)
    Button mImport;

    @OnClick(R.id.btn_import)
    void onClickImport(){
        String password = mPassword.getText().toString();
        String keystore = mKeystore.getText().toString();
        mPresenter.importKeystore(keystore,password);
    }


    @Override
    public int getContentLayoutID() {
        return R.layout.activity_import;
    }

    @Override
    public ImportContract.Presenter initPresenter() {
        return mPresenter;
    }

    @Override
    public ImportViewModule initViewModule() {
        return viewModel;
    }

    @Override
    public void initData() {
        super.initData();

        viewModel = ViewModelProviders.of(this, walletsViewModuleFactory)
                .get(ImportViewModule.class);
        mPresenter.takeView(this,viewModel);

        viewModel.progress().observe(this,this::importCallback);
    }

    private void importCallback(Boolean aBoolean) {
        if(aBoolean){
            viewModel.openWallet(this);
        }
    }


}
