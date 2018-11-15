package com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importPrivateKey;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.widget.Button;
import android.widget.EditText;

import com.alphawizard.hdwallet.alphahdwallet.R;
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

    @BindView(R.id.btn_import )
    Button  mImport;


    @OnClick(R.id.btn_import)
    void onClickImport(){
        getmPresenter().importPrivateKey(mPrivatekey.getText().toString(),"Wallet");
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mScanContent = ((ImportActivity) activity).getScanContent();
    }



    @Override
    public void initData() {
        super.initData();
        viewModel = ViewModelProviders.of(this, viewModuleFactory)
                .get(ImportViewModule.class);
        getmPresenter().takeView(this,viewModel);
        viewModel.progress().observe(this,this::importCallback);

    }

    @Override
    public void onResume() {
        super.onResume();
        mPrivatekey.setText(mScanContent);
    }

    private void importCallback(Boolean aBoolean) {
        if(aBoolean){
            viewModel.openWallet(getActivity());
            getActivity().finish();
        }
    }
}
