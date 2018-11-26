package com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importMnenonics;

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
import com.alphawizard.hdwallet.common.util.Log;

import net.qiujuer.genius.ui.widget.Loading;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class ImportMnenonicsFragment extends BasePresenterFragment<ImportMnenonicsContract.Presenter,ImportViewModule> implements  ImportMnenonicsContract.View{

    @Inject
    ImportMnenonicsContract.Presenter mPresenter;

    @Inject
    ImportViewModuleFactory viewModuleFactory;
    ImportViewModule viewModel;

    @BindView(R.id.ed_mnemonics )
    EditText mMnenonics;

    @BindView(R.id.ed_wallet_name )
    EditText mName;

    @BindView(R.id.btn_import )
    Button mImport;

    @BindView(R.id.loading)
    Loading loading;

    @OnClick(R.id.btn_import)
    void onClickImport(){
        if(isLoading){
            return;
        }
        isLoading = true;
        loading.start();
        getmPresenter().importMnenonics(mMnenonics.getText().toString(),mName.getText().toString());
    }

    public static ImportMnenonicsFragment create() {
        return new ImportMnenonicsFragment();
    }

    @Override
    public ImportMnenonicsContract.Presenter initPresenter() {
        return mPresenter;
    }

    @Override
    public ImportViewModule initViewModule() {
        return viewModel;
    }

    @Override
    public int getContentLayoutID() {
        return R.layout.fragment_import_mnenonics;
    }


    String  mScanContent;
    boolean isInputMnemonics =false;
    boolean isInputName =false;
    boolean  isLoading = false;
    @Override
    public void initData() {
        super.initData();
        viewModel = ViewModelProviders.of(this, viewModuleFactory)
                .get(ImportViewModule.class);
        getmPresenter().takeView(this,viewModel);
        viewModel.progress().observe(this,this::importCallback);
        viewModel.changeDefaultWallet().observe(this,this::defaultWalletChange);
        viewModel.importWallet().observe(this,this::importWallet);
        viewModel.observeImportWalletError().observe(this,this::showError);
        Log.d("init  data    ImportMnemonicsFragment");


        mImport.setEnabled(false);
        mImport.setBackgroundResource(R.drawable.bg_color_dae6ff);

        mMnenonics.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(mMnenonics.getText().length()>0){
                    isInputMnemonics =true;
                    if(isInputName){
                        mImport.setEnabled(true);
                        mImport.setBackgroundResource(R.drawable.bg_gradient_blue);
                    }
                }else{
                    isInputMnemonics =false;
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
                    if(isInputMnemonics){

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
        isLoading = false;
        loading.stop();
        if(((ImportActivity)getActivity()).getScanContent()!=null){
            mScanContent = ((ImportActivity) getActivity()).getScanContent();
            ((ImportActivity) getActivity()).clearScanContent();
        }
        mMnenonics.setText(mScanContent);
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
    private void showError(Boolean aBoolean) {
        loading.stop();
    }
}
