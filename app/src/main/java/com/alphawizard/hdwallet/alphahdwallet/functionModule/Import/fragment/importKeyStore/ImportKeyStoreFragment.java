package com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importKeyStore;

import android.arch.lifecycle.ViewModelProviders;
import android.widget.Button;
import android.widget.EditText;

import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportViewModule;
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

    @BindView(R.id.btn_import )
    Button mImport;




    @OnClick(R.id.btn_import)
    void onClickImport(){
        getmPresenter().importKeyStore(mKeystore.getText().toString(),mPassword.getText().toString(),"Wallet");
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

    @Override
    public void initData() {
        super.initData();
        viewModel = ViewModelProviders.of(this, viewModuleFactory)
                .get(ImportViewModule.class);
        getmPresenter().takeView(this,viewModel);
        viewModel.progress().observe(this,this::importCallback);
    }

    private void importCallback(Boolean aBoolean) {
        if(aBoolean){

            viewModel.openWallet(getActivity());
            getActivity().finish();
        }
    }
}
