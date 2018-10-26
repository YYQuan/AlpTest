package com.alphawizard.hdwallet.alphahdwallet.functionModule.Import;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.widget.Button;
import android.widget.EditText;

import com.alphawizard.hdwallet.alphahdwallet.R;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importKeyStore.ImportKeyStoreFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importPrivateKey.ImportPrivateKeyFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.FirstLaunchViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.ImportViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.fristLaunch.FirstLaunchContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.fristLaunch.FirstLaunchViewModule;
import com.alphawizard.hdwallet.alphahdwallet.utils.Adapter.TabPagerAdapter;
import com.alphawizard.hdwallet.common.presenter.BasePresenterToolbarActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class ImportActivity extends BasePresenterToolbarActivity<ImportContract.Presenter,ImportViewModule> implements ImportContract.View  {

    private static final int KEYSTORE_FORM_INDEX = 0;
    private static final int PRIVATE_KEY_FORM_INDEX = 1;

    @Inject
    ImportViewModuleFactory walletsViewModuleFactory;
    ImportViewModule viewModel;

    @Inject
    ImportContract.Presenter mPresenter;

//
//    @BindView(R.id.ed_keystore)
//    EditText mKeystore;
//
//    @BindView(R.id.ed_password)
//    EditText mPassword;
//
//    @BindView(R.id.btn_import)
//    Button mImport;


    private final List<Pair<String, Fragment>> pages = new ArrayList<>();


//    void onClickImport(){
//        String password = mPassword.getText().toString();
//        String keystore = mKeystore.getText().toString();
//        mPresenter.importKeystore(keystore,password);
//        keystore ="CDBC0D533178042BBF321678918BB90FF9C369F465C26A5BFB94E3392D0C9776";
//        mPresenter.importPrivateKey(keystore);
//    }




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

    @Override
    public void initWidget() {
        super.initWidget();

        pages.add(KEYSTORE_FORM_INDEX, new Pair<>("KEYSTORE", ImportKeyStoreFragment.create()));
        pages.add(PRIVATE_KEY_FORM_INDEX, new Pair<>("PRIVATE", ImportPrivateKeyFragment.create()));
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager(), pages));
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void importCallback(Boolean aBoolean) {
        if(aBoolean){
            viewModel.openWallet(this);
        }
    }


}
