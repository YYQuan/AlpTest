package com.alphawizard.hdwallet.alphahdwallet.functionModule.WalletDetail;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportActivity;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.VerifyMnemonicsModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.WalletDetailModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.verifyMnemonics.VerifyMnemonicsContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.verifyMnemonics.VerifyMnemonicsViewModule;
import com.alphawizard.hdwallet.alphahdwallet.utils.Dp2px;
import com.alphawizard.hdwallet.alphahdwallet.utils.StatusBarUtil;
import com.alphawizard.hdwallet.alphahdwallet.widget.MyDialog;
import com.alphawizard.hdwallet.common.presenter.BasePresenterActivity;
import com.alphawizard.hdwallet.common.presenter.BasePresenterToolbarActivity;

import net.qiujuer.genius.ui.widget.Loading;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class WalletDetailActivity extends BasePresenterToolbarActivity<WalletDetailContract.Presenter,WalletDetailViewModule> implements WalletDetailContract.View {


    public  static  void show(Context context){
        context.startActivity(new Intent(context, WalletDetailActivity.class));
    }
    @Inject
    WalletDetailModuleFactory walletsViewModuleFactory;
    WalletDetailViewModule viewModel;

    @Inject
    WalletDetailContract.Presenter mPresenter;

    String address ;

    @BindView(R.id.txt_eth_balance)
    TextView mBalance;

    @BindView(R.id.txt_address)
    TextView mAddress;

    @BindView(R.id.lay_export_private_key)
    LinearLayout mPrivatekey;

    @BindView(R.id.lay_export_keystore)
    LinearLayout mKeystore;

    @BindView(R.id.lay_export_mnemonics)
    LinearLayout mMnemonics;

    @BindView(R.id.ed_wallet_name)
    EditText mName;


    @BindView(R.id.txt_save)
    TextView mSave;

    @BindView(R.id.linie_keystore)
    View linieKeystore;

    @BindView(R.id.linie_mnemonics)
    View linieMnemonics;

    private long lastClickTime = 0L;
    private static final int FAST_CLICK_DELAY_TIME = 500;  // 快速点击间隔


    public  boolean     isRepeatClick(){


        if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME){
            return  true;
        }
        lastClickTime = System.currentTimeMillis();
        return false;
    }

    @OnClick(R.id.txt_save)
    void onClickSave(){
        if(isRepeatClick()){
            return ;
        }
        viewModel.saveWalletName(address,mName.getText().toString());
    }

    @OnClick(R.id.lay_export_private_key)
    void onClickPrivateKey(){
        if(isRepeatClick()){
            return ;
        }
        enableClick(false);
        viewModel.exportPrivatekey(address);
    }

    @OnClick(R.id.lay_export_keystore)
    void onClickKeystore(){

        if(isRepeatClick()){
            return ;
        }
//        viewModel.exportKeystore(address);

        showBackupKeystoreDialog(password);
//        viewModel.exportKeystore(address);
    }

//    @OnClick(R.id.iv_back)
//    void onClickBack(){
//        if(isRepeatClick()){
//            return ;
//        }
//        onBackPressed();
//    }
    @OnClick(R.id.lay_export_mnemonics)
    void onClickMnemonics(){
        if(isRepeatClick()){
            return ;
        }
        enableClick(false);
        viewModel.exportMnemonics(address);
    }

    @BindView(R.id.btn_delete)
    Button mDelete;


    @OnClick(R.id.lay_back)
    void onClickLayBack(){
        onBackPressed();
    }

    @OnClick(R.id.btn_delete)
    void onClickDelete(){
        if(isRepeatClick()){
            return ;
        }
        enableClick(false);
        viewModel.deleteWallet(new Wallet(address),password);
    }


    void enableClick(boolean enable ){
        if(enable){
            loading.start();
            mDelete.setEnabled(true);
            mPrivatekey.setEnabled(true);
            mKeystore.setEnabled(true);
            mMnemonics.setEnabled(true);
        }else{
            loading.stop();
            mDelete.setEnabled(false);
            mPrivatekey.setEnabled(false);
            mKeystore.setEnabled(false);
            mMnemonics.setEnabled(false);
        }
    }

    String password;

    @BindView(R.id.loading)
    Loading loading;


    boolean  hasKeystore = true;
    boolean  hasMnemonics = true;

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_wallet_detail;
    }

    @Override
    public WalletDetailContract.Presenter initPresenter() {
        return mPresenter;
    }

    @Override
    public WalletDetailViewModule initViewModule() {
        return viewModel;
    }

    @Override
    public void initData() {
        super.initData();

        address =getIntent().getStringExtra(WalletDetailRouter.WALLET_DETAIL_STRING);
        viewModel = ViewModelProviders.of(this, walletsViewModuleFactory)
                .get(WalletDetailViewModule.class);
        mPresenter.takeView(this,viewModel);
        viewModel.walletBalance().observe(this,this::walletBalanceChange);
        viewModel.exportKeyStoreString().observe(this,this::exportKeyStoreString);
        viewModel.exportPrivateKeyString().observe(this,this::exportPrivateKeyString);
        viewModel.exportMnemonicsString().observe(this,this::exportMnemonicsString);
        viewModel.walletNameString().observe(this ,this::walletNameString);
        viewModel.progress().observe(this,this::saveWalletNameSuccess);
        viewModel.isFailExportContent().observe(this,this::isFailExport);
        viewModel.hasMnemonicsString().observe(this,this::hasMnemonics);
        viewModel.passwordString().observe(this,this::getPassword);

        viewModel.isOkDeleteContent().observe(this,this::deleteCallback);
        viewModel.getBalance(address);
        viewModel.getWalletName(address);
        viewModel.hasMnemonics(address);
        mAddress.setText(address);
        viewModel.getPassword(new Wallet(address));

    }

    private void deleteCallback(Boolean aBoolean) {
        if(aBoolean){
            onBackPressed();
        }
    }

    private void isFailExport(Boolean aBoolean) {
        if(!aBoolean){
            loading.stop();
            mPrivatekey.setEnabled(true);
            mKeystore.setEnabled(true);
            mMnemonics.setEnabled(true);
        }
    }



    private void getPassword(String s) {
        password = s;
        if(password.length()>30){
            linieKeystore.setVisibility(View.GONE);
            mKeystore.setVisibility(View.GONE);
            hasKeystore = false ;
        }
        setMargin();
    }

    private void hasMnemonics(Boolean aBoolean) {
        if(!aBoolean){
            hasMnemonics = false ;
            linieMnemonics.setVisibility(View.GONE);
            mMnemonics.setVisibility(View.GONE);
        }
        setMargin();
    }

    private void  setMargin(){


        if(!hasMnemonics&&!hasKeystore) {

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mPrivatekey.getLayoutParams();
            int marginTop = Dp2px.dip2px(19);
            int marginBottom = Dp2px.dip2px(22);
            lp.setMargins(0, marginTop, 0, marginBottom);
            mPrivatekey.setLayoutParams(lp);
        }
        else if(!hasMnemonics){
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mKeystore.getLayoutParams();
            int marginTop = Dp2px.dip2px(19);
            int marginBottom = Dp2px.dip2px(22);
            lp.setMargins(0, marginTop, 0, marginBottom);
            mKeystore.setLayoutParams(lp);
        }
    }

    private void saveWalletNameSuccess(Boolean aBoolean) {
        if(aBoolean){
            viewModel.openWallet(this);
        }
    }

    private void walletNameString(String s) {
//        App.showToast(s);
        mName.setText(s);
    }

    private void exportMnemonicsString(String s) {
        enableClick(true);
        shareTextIntent(s);
    }

    private void exportPrivateKeyString(String s) {
        enableClick(true);
        shareTextIntent(s);
    }

    private void exportKeyStoreString(String s) {
        enableClick(true);
        shareTextIntent(s);
    }

    private void shareTextIntent(String s) {
        //        文本分享
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, s);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        ActionBar actionBar = getSupportActionBar();

//      隐藏toolbar上的 back btn
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(false);
//            actionBar.setTitle("ETH");
        }

//        StatusBarUtil.statusBarLightMode(this,0x010110);
//        StatusBarUtil.setStatusBarColor(this,0xffffff);
//        透明状态栏 ， 这种方式不会引起  崩溃
        StatusBarUtil.transparencyBar(this);



        mName.setSelection(mName.getText().length());
    }

    private void walletBalanceChange(String s) {
        mBalance.setText(s+"EHT");
    }

    Dialog dialog ;

    public void showBackupKeystoreDialog(String password) {

//        BackupView view = new BackupView(getActivity());
        View view = View.inflate(this, R.layout.dialog_verify_password,null);
        EditText editPassword = view.findViewById(R.id.edit_keystore_password);
        Button isOk = view.findViewById(R.id.btn_ok);
        Button isCancle = view.findViewById(R.id.btn_cancel);

        isCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        isOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.equalsIgnoreCase(editPassword.getText().toString())){
//                    App.showToast(" password is true ");
                    enableClick(false);
                    viewModel.exportKeystore(address);
                    return ;
                }
                showBackupKeystoreErrorDialog();

            }
        });

        dialog = buildDialog()
                .setView(view)
//                .setOnDismissListener(dialog -> KeyboardUtils.hideKeyboard(view.findViewById(R.id.tv_keystore)))
                .create();
        dialog.show();
    }

    public void showBackupKeystoreErrorDialog() {
        if(dialog.isShowing()){
            dialog.dismiss();
        }
//        BackupView view = new BackupView(getActivity());
        View view = View.inflate(this, R.layout.dialog_verify_password_error,null);
        TextView tv = view.findViewById(R.id.txt_sure);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog = buildDialog()
                .setView(view)
//                .setOnDismissListener(dialog -> KeyboardUtils.hideKeyboard(view.findViewById(R.id.tv_keystore)))
                .create();
        dialog.show();
    }

    private AlertDialog.Builder buildDialog() {
        hideDialog();
        return new AlertDialog.Builder(this);
    }

    public  void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }


}
