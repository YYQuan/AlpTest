package com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importPrivateKey;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportActivity;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importKeyStore.ImportKeyStoreContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.ImportViewModuleFactory;
import com.alphawizard.hdwallet.common.presenter.BasePresenterFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class ImportPrivateKeyFragment extends BasePresenterFragment<ImportPrivateKeyContract.Presenter,ImportViewModule> implements  ImportPrivateKeyContract.View{

    @Inject
    ImportPrivateKeyContract.Presenter mPresenter;

    @Inject
    ImportViewModuleFactory viewModuleFactory;
    ImportViewModule viewModel;


    @BindView(R.id.ed_privatekey )
    EditText  mPrivatekey;
    @BindView(R.id.ed_wallet_name )
    EditText  mName;
    @BindView(R.id.btn_import )
    Button  mImport;


    @OnClick(R.id.btn_import)
    void onClickImport(){
        getmPresenter().importPrivateKey(mPrivatekey.getText().toString(),mName.getText().toString());
    }

    public static ImportPrivateKeyFragment create() {
        return new ImportPrivateKeyFragment();
    }

    @Override
    public ImportPrivateKeyContract.Presenter initPresenter() {
        return mPresenter;
    }

    @Override
    public ImportViewModule initViewModule() {
        return viewModel;
    }

    @Override
    public int getContentLayoutID() {
        return R.layout.fragment_import_privatekey;
    }


    String  mScanContent;



    boolean isInputPrivateKey =false;
    boolean isInputName =false;


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


        mPrivatekey.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(mPrivatekey.getText().length()>0){
                    isInputPrivateKey =true;
                    if(isInputName){
                        mImport.setEnabled(true);
                        mImport.setBackgroundResource(R.drawable.bg_gradient_blue);
                    }
                }else{
                    isInputPrivateKey =false;
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
                    if(isInputPrivateKey){

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
    }

    @Override
    public void onResume() {
        super.onResume();
        if(((ImportActivity)getActivity()).getScanContent()!=null){
            mScanContent = ((ImportActivity) getActivity()).getScanContent();
            ((ImportActivity) getActivity()).clearScanContent();
        }
        mPrivatekey.setText(mScanContent);
    }

    Wallet importWallet ;
    private void importWallet(Wallet wallet) {
        importWallet = wallet;
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
