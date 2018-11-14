package com.alphawizard.hdwallet.alphahdwallet.functionModule.Import;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;

import com.alphawizard.hdwallet.alphahdwallet.R;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importKeyStore.ImportKeyStoreFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importMnenonics.ImportMnenonicsFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importPrivateKey.ImportPrivateKeyFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.ImportViewModuleFactory;
import com.alphawizard.hdwallet.common.presenter.BasePresenterToolbarActivity;
import com.alphawizard.hdwallet.common.util.Helper.NavHelper;
import com.alphawizard.hdwallet.common.util.Log;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class ImportActivity extends BasePresenterToolbarActivity<ImportContract.Presenter,ImportViewModule> implements ImportContract.View ,NavHelper.OnMenuSelector<Integer> {

    private static final int KEYSTORE_FORM_INDEX = 0;
    private static final int PRIVATE_KEY_FORM_INDEX = 1;
    private static final int MNENONICS_FORM_INDEX = 2;

    @Inject
    ImportViewModuleFactory walletsViewModuleFactory;
    ImportViewModule viewModel;

    @Inject
    ImportContract.Presenter mPresenter;


    @BindView(R.id.btn_mnemonics)
    Button  mMnemonics;

    @BindView(R.id.btn_keystore)
    Button  mKeystore;

    @BindView(R.id.btn_privateKey)
    Button  mPrivateKey;


    @OnClick({R.id.btn_privateKey,R.id.btn_keystore,R.id.btn_mnemonics})
    void onClickImportBTN(Button  itemID){
        Log.d("  now  " + itemID.getId());
        switch (itemID.getId()){
            case R.id.btn_mnemonics:
                mHelper.performClickMenu(MNENONICS_FORM_INDEX);
                break;
            case R.id.btn_keystore:
                mHelper.performClickMenu(KEYSTORE_FORM_INDEX);
                break;
            case R.id.btn_privateKey:
                mHelper.performClickMenu(PRIVATE_KEY_FORM_INDEX);
                break;

        }
    }
//
//    @BindView(R.id.ed_keystore)
//    EditText mKeystore;
//
//    @BindView(R.id.ed_password)
//    EditText mPassword;
//
//    @BindView(R.id.btn_import)
//    Button mImport;

    private NavHelper<Integer> mHelper;

//    void onClickImport(){
//        String password = mPassword.getText().toString();
//        String keystore = mKeystore.getText().toString();
//        mPresenter.importKeystore(keystore,password);
//        keystore ="CDBC0D533178042BBF321678918BB90FF9C369F465C26A5BFB94E3392D0C9776";
//        mPresenter.importPrivateKey(keystore);
//    }

    public  static  void show(Context context){
        context.startActivity(new Intent(context, ImportActivity.class));
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

    @SuppressLint("ResourceType")
    @Override
    public void initWidget() {
        super.initWidget();

        mHelper = new NavHelper<>(this,getSupportFragmentManager(),R.id.lay_container,this);
        mHelper.add(KEYSTORE_FORM_INDEX, new NavHelper.Tab<>(ImportKeyStoreFragment.class, R.string.title_wallet))
                .add(PRIVATE_KEY_FORM_INDEX, new NavHelper.Tab<>(ImportPrivateKeyFragment.class, R.string.title_dapps))
                .add(MNENONICS_FORM_INDEX, new NavHelper.Tab<>(ImportMnenonicsFragment.class, R.string.title_mine));

        mHelper.performClickMenu(1);
//        ViewPager viewPager = findViewById(R.id.viewPager);
//        TabPagerAdapter adatper = new TabPagerAdapter(this,getSupportFragmentManager(), pages);
//        viewPager.setAdapter(adatper);
//        TabLayout tabLayout = findViewById(R.id.tabLayout);
//        tabLayout.setupWithViewPager(viewPager);
//        viewPager.addOnAdapterChangeListener(new ViewPager.OnAdapterChangeListener() {
//            @Override
//            public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter pagerAdapter, @Nullable PagerAdapter pagerAdapter1) {
//
//
//                    TabLayout.Tab tab = tabLayout.getTabAt( viewPager.getCurrentItem());//获得每一个tab
//                    tab.setCustomView(R.layout.cell_tab_item);//给每一个tab设置view
//
//                    TextView textView = (TextView) tab.getCustomView().findViewById(R.id.tv_item_content);
//                    textView.setText(pages.get(viewPager.getCurrentItem()).first);//设置tab上的文字
//                    textView.setTextColor(0xffff0000);
//
//            }
//        });
//
//
//        for (int i = 0; i < adatper.getCount(); i++) {
//            TabLayout.Tab tab = tabLayout.getTabAt(i);//获得每一个tab
//            tab.setCustomView(R.layout.cell_tab_item);//给每一个tab设置view
//
//            TextView textView = (TextView) tab.getCustomView().findViewById(R.id.tv_item_content);
//            textView.setText(pages.get(i).first);//设置tab上的文字
//        }

    }

    private void importCallback(Boolean aBoolean) {
        if(aBoolean){
            viewModel.openWallet(this);
        }
    }


    @Override
    public void onMenuSucceed(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {

    }

    @Override
    public void onMenuRefresh(NavHelper.Tab<Integer> newTab) {

    }
}
