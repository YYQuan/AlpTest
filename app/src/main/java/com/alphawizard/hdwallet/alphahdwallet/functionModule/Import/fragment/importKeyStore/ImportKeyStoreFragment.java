package com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importKeyStore;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportActivity;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.InterfaceUtil.ScanContentProvider;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importPrivateKey.ImportPrivateKeyFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.ImportViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.WalletsViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Account.AccountContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.common.base.Layout.PlaceHolder.EmptyLayout;
import com.alphawizard.hdwallet.common.presenter.BasePresenterFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class ImportKeyStoreFragment extends BasePresenterFragment<ImportKeyStoreContract.Presenter,ImportViewModule> implements  ImportKeyStoreContract.View{

    @Inject
    ImportKeyStoreContract.Presenter mPresenter;

    @Inject
    ImportViewModuleFactory viewModuleFactory;
    ImportViewModule viewModel;

    @BindView(R.id.ed_keystore )
    EditText mKeystore;

    @BindView(R.id.ed_wallet_password)
    EditText mPassword;


    @BindView(R.id.ed_wallet_name)
    EditText mName;


    @BindView(R.id.btn_import )
    Button mImport;


    @OnClick(R.id.btn_import)
    void onClickImport(){
        getmPresenter().importKeyStore(mKeystore.getText().toString(),mPassword.getText().toString(),mName.getText().toString());
    }

    public static ImportKeyStoreFragment create() {
        return new ImportKeyStoreFragment();
    }

    @Override
    public ImportKeyStoreContract.Presenter initPresenter() {
        return mPresenter;
    }

    @Override
    public ImportViewModule initViewModule() {
        return viewModel;
    }

    @Override
    public int getContentLayoutID() {
        return R.layout.fragment_import_keystore;
    }



    String  mScanContent = "";
    boolean isInputKeystore =false;
    boolean isInputName =false;
    boolean isInputPassword =false;



    @Override
    public void initData() {
        super.initData();
        viewModel = ViewModelProviders.of(this, viewModuleFactory)
                .get(ImportViewModule.class);
        getmPresenter().takeView(this,viewModel);
        viewModel.progress().observe(this,this::importCallback);
        viewModel.changeDefaultWallet().observe(this,this::defaultWalletChange);
        viewModel.importWallet().observe(this,this::importWallet);
        mImport.setEnabled(false);
        mImport.setBackgroundResource(R.drawable.bg_color_dae6ff);




        mKeystore.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(mKeystore.getText().length()>0){
                    isInputKeystore =true;
                    if(isInputName&&isInputPassword){
                        mImport.setEnabled(true);
                        mImport.setBackgroundResource(R.drawable.bg_gradient_blue);
                    }
                }else{
                    isInputKeystore =false;
                    mImport.setEnabled(false);
                    mImport.setBackgroundResource(R.drawable.bg_color_dae6ff);
                }



            }
        });
        mName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(mName.getText().length()>0){
                    isInputName =true;
                    if(isInputKeystore&&isInputPassword){

                        mImport.setEnabled(true);
                        mImport.setBackgroundResource(R.drawable.bg_gradient_blue);
                    }
                }else{
                    isInputName =false;

                    mImport.setEnabled(false);
                    mImport.setBackgroundResource(R.drawable.bg_color_dae6ff);
                }
            }
        });

        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(mPassword.getText().length()>0){
                    isInputPassword =true;
                    if(isInputKeystore&&isInputName){

                        mImport.setEnabled(true);
                        mImport.setBackgroundResource(R.drawable.bg_gradient_blue);
                    }
                }else{
                    isInputPassword =false;

                    mImport.setEnabled(false);
                    mImport.setBackgroundResource(R.drawable.bg_color_dae6ff);
                }
            }
        });
    }

    Wallet importWallet ;
    private void importWallet(Wallet wallet) {
        importWallet = wallet;
    }

    @Override
    public void onResume() {
        super.onResume();


        if(((ImportActivity) getActivity()).getScanContent()!= null) {
            mScanContent = ((ImportActivity) getActivity()).getScanContent();
            ((ImportActivity) getActivity()).clearScanContent();
        }
        mKeystore.setText(mScanContent);
    }
    private void defaultWalletChange(Boolean aBoolean) {
        if(aBoolean){
            viewModel.openWallet(getActivity());
            getActivity().finish();
        }
    }

    private void importCallback(Boolean aBoolean) {
        if(aBoolean){
            if(importWallet!=null) {
                viewModel.setDefaultWallet(importWallet);
            }
        }
    }

}
