package com.alphawizard.hdwallet.alphahdwallet.functionModule.Import;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Button;

import com.alphawizard.hdwallet.alphahdwallet.R;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.InterfaceUtil.ScanContentProvider;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importKeyStore.ImportKeyStoreFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importMnenonics.ImportMnenonicsFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importPrivateKey.ImportPrivateKeyFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.ImportViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.utils.StatusBarUtil;
import com.alphawizard.hdwallet.common.presenter.BasePresenterActivity;
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
//
//    @OnClick(R.id.iv_back)
//    void onClickBack(){
//        onBackPressed();
//    }

    @OnClick(R.id.lay_back)
    void onClickLayBack(){
        onBackPressed();
    }

    String scanContent;

    @OnClick({R.id.btn_privateKey,R.id.btn_keystore,R.id.btn_mnemonics})
    void onClickImportBTN(Button  itemID){
        Log.d("  now  " + itemID.getId());
        switch (itemID.getId()){
            case R.id.btn_mnemonics:
                mMnemonics.setBackgroundResource(R.drawable.bg_color_3174ff);
                mKeystore.setBackgroundResource(R.drawable.bg_color_9fadca);
                mPrivateKey.setBackgroundResource(R.drawable.bg_color_9fadca);
                mHelper.performClickMenu(MNENONICS_FORM_INDEX);
                break;
            case R.id.btn_keystore:
                mMnemonics.setBackgroundResource(R.drawable.bg_color_9fadca);
                mKeystore.setBackgroundResource(R.drawable.bg_color_3174ff);
                mPrivateKey.setBackgroundResource(R.drawable.bg_color_9fadca);
                mHelper.performClickMenu(KEYSTORE_FORM_INDEX);
                break;
            case R.id.btn_privateKey:
                mMnemonics.setBackgroundResource(R.drawable.bg_color_9fadca);
                mKeystore.setBackgroundResource(R.drawable.bg_color_9fadca);
                mPrivateKey.setBackgroundResource(R.drawable.bg_color_3174ff);
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
                Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
                startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
            }
        }else {
                //这个说明系统版本在6.0之下，不需要动态获取权限。
            Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
            startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
        }
    }

    //activity权限授权结果回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

        onClickImportBTN(mMnemonics);

    }


    @Override
    protected void onResume() {
        super.onResume();
        mHelper.performClickMenu((int)mHelper.getCurrentTab().extra);
    }

    @SuppressLint("ResourceType")
    @Override
    public void initWidget() {
        super.initWidget();

        mHelper = new NavHelper<>(this,getSupportFragmentManager(),R.id.lay_container,this);
        mHelper.add(KEYSTORE_FORM_INDEX, new NavHelper.Tab<>(ImportKeyStoreFragment.class, KEYSTORE_FORM_INDEX))
                .add(PRIVATE_KEY_FORM_INDEX, new NavHelper.Tab<>(ImportPrivateKeyFragment.class, PRIVATE_KEY_FORM_INDEX))
                .add(MNENONICS_FORM_INDEX, new NavHelper.Tab<>(ImportMnenonicsFragment.class, MNENONICS_FORM_INDEX));

        //        透明状态栏 ， 这种方式不会引起  崩溃
        StatusBarUtil.transparencyBar(this);

        mHelper.performClickMenu(MNENONICS_FORM_INDEX);


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
                    if(!result.equalsIgnoreCase(scanContent)){
                        scanContent  = result;

                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {

                }
            }
        }

//        mHelper.performClickMenu((int)mHelper.getCurrentTab().extra);
    }

    @Override
    public String getScanContent() {
        return scanContent;
    }


    @Override
    public void  clearScanContent(){
        scanContent = "";
    }
}
