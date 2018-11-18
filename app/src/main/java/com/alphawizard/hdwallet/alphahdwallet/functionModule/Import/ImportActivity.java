package com.alphawizard.hdwallet.alphahdwallet.functionModule.Import;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.Toast;

import com.alphawizard.hdwallet.alphahdwallet.R;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.InterfaceUtil.ScanContentProvider;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importKeyStore.ImportKeyStoreFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importMnenonics.ImportMnenonicsFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importPrivateKey.ImportPrivateKeyFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.ImportViewModuleFactory;
import com.alphawizard.hdwallet.common.presenter.BasePresenterActivity;
import com.alphawizard.hdwallet.common.presenter.BasePresenterToolbarActivity;
import com.alphawizard.hdwallet.common.util.Helper.NavHelper;
import com.alphawizard.hdwallet.common.util.Log;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class ImportActivity extends BasePresenterActivity<ImportContract.Presenter,ImportViewModule> implements ImportContract.View
        , NavHelper.OnMenuSelector<Integer>
        ,ScanContentProvider {

    private static final int KEYSTORE_FORM_INDEX = 0;
    private static final int PRIVATE_KEY_FORM_INDEX = 1;
    private static final int MNENONICS_FORM_INDEX = 2;


    private static final int CAMERA_OK = 1;

    private static final int BARCODE_READER_REQUEST_CODE = 1;

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

    @OnClick(R.id.iv_back)
    void onClickBack(){
        onBackPressed();
    }

    String scanContent;

    @OnClick({R.id.btn_privateKey,R.id.btn_keystore,R.id.btn_mnemonics})
    void onClickImportBTN(Button  itemID){
        Log.d("  now  " + itemID.getId());
        switch (itemID.getId()){
            case R.id.btn_mnemonics:
                mMnemonics.setBackgroundResource(R.drawable.bg_color_6a89c4);
                mKeystore.setBackgroundResource(R.drawable.bg_color_393a50);
                mPrivateKey.setBackgroundResource(R.drawable.bg_color_393a50);
                mHelper.performClickMenu(MNENONICS_FORM_INDEX);
                break;
            case R.id.btn_keystore:
                mMnemonics.setBackgroundResource(R.drawable.bg_color_393a50);
                mKeystore.setBackgroundResource(R.drawable.bg_color_6a89c4);
                mPrivateKey.setBackgroundResource(R.drawable.bg_color_393a50);
                mHelper.performClickMenu(KEYSTORE_FORM_INDEX);
                break;
            case R.id.btn_privateKey:
                mMnemonics.setBackgroundResource(R.drawable.bg_color_393a50);
                mKeystore.setBackgroundResource(R.drawable.bg_color_393a50);
                mPrivateKey.setBackgroundResource(R.drawable.bg_color_6a89c4);
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


    @OnClick(R.id.iv_saoma)
    void  onClickScan(){

        if (Build.VERSION.SDK_INT>22){
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                //先判断有没有权限 ，没有就在这里进行权限的申请
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CAMERA},CAMERA_OK);

            }else {
                //说明已经获取到摄像头权限了 想干嘛干嘛
            }
        }else {
                //这个说明系统版本在6.0之下，不需要动态获取权限。

        }


        Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
        startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);

    }

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
        mHelper.add(KEYSTORE_FORM_INDEX, new NavHelper.Tab<>(ImportKeyStoreFragment.class, "Keystore"))
                .add(PRIVATE_KEY_FORM_INDEX, new NavHelper.Tab<>(ImportPrivateKeyFragment.class, "private"))
                .add(MNENONICS_FORM_INDEX, new NavHelper.Tab<>(ImportMnenonicsFragment.class, "mnemonics"));

        mHelper.performClickMenu(MNENONICS_FORM_INDEX);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BARCODE_READER_REQUEST_CODE) {//处理二维码扫描结果
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }

                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this, "result:"+result, Toast.LENGTH_SHORT).show();
                    if(!result.equalsIgnoreCase(scanContent)){
                        scanContent  = result;
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {

                }
            }
        }
        scanContent  = "123456789";

        mHelper.performClickMenu((int)mHelper.getCurrentTab().extra);
    }

    @Override
    public String getScanContent() {
        return scanContent;
    }
}
