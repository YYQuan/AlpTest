package com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importMnenonics;

import android.arch.lifecycle.ViewModelProviders;
import android.widget.Button;
import android.widget.EditText;

import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importKeyStore.ImportKeyStoreContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.ImportViewModuleFactory;
import com.alphawizard.hdwallet.common.presenter.BasePresenterFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class ImportMnenonicsFragment extends BasePresenterFragment<ImportMnenonicsContract.Presenter,ImportViewModule> implements  ImportMnenonicsContract.View{

    @Inject
    ImportMnenonicsContract.Presenter mPresenter;

    @Inject
    ImportViewModuleFactory viewModuleFactory;
    ImportViewModule viewModel;

    @BindView(R.id.ed_mnenonics )
    EditText mMnenonics;



    @BindView(R.id.btn_import )
    Button mImport;

    @OnClick(R.id.btn_import)
    void onClickImport(){

        getmPresenter().importMnenonics(mMnenonics.getText().toString(),"Wallet");
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
