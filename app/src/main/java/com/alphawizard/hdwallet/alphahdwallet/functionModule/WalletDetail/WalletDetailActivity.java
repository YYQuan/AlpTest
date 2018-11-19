package com.alphawizard.hdwallet.alphahdwallet.functionModule.WalletDetail;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
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
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.VerifyMnemonicsModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.WalletDetailModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.verifyMnemonics.VerifyMnemonicsContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.verifyMnemonics.VerifyMnemonicsViewModule;
import com.alphawizard.hdwallet.alphahdwallet.widget.MyDialog;
import com.alphawizard.hdwallet.common.presenter.BasePresenterActivity;
import com.alphawizard.hdwallet.common.presenter.BasePresenterToolbarActivity;

import net.qiujuer.genius.ui.widget.Loading;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class WalletDetailActivity extends BasePresenterToolbarActivity<WalletDetailContract.Presenter,WalletDetailViewModule> implements WalletDetailContract.View {

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
    LinearLayout mAKeystore;

    @BindView(R.id.lay_export_mnemonics)
    LinearLayout mMnemonics;

    @BindView(R.id.ed_wallet_name)
    EditText mName;


    @BindView(R.id.txt_save)
    TextView mSave;

    @OnClick(R.id.txt_save)
    void onClickSave(){
        viewModel.saveWalletName(address,mName.getText().toString());
    }

    @OnClick(R.id.lay_export_private_key)
    void onClickPrivateKey(){
        loading.start();
        mPrivatekey.setEnabled(false);
        mAKeystore.setEnabled(false);
        mMnemonics.setEnabled(false);
        viewModel.exportPrivatekey(address);
    }

    @OnClick(R.id.lay_export_keystore)
    void onClickKeystore(){

//        viewModel.exportKeystore(address);

        showBackupKeystoreDialog(password);

    }

    @OnClick(R.id.iv_back)
    void onClickBack(){
        onBackPressed();
    }
    @OnClick(R.id.lay_export_mnemonics)
    void onClickMnemonics(){
        loading.start();
        mPrivatekey.setEnabled(false);
        mAKeystore.setEnabled(false);
        mMnemonics.setEnabled(false);
        viewModel.exportMnemonics(address);
    }

    String password;

    @BindView(R.id.loading)
    Loading loading;

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
        viewModel.getBalance(address);
        viewModel.getWalletName(address);
        viewModel.hasMnemonics(address);
        mAddress.setText(address);
        viewModel.getPassword(new Wallet(address));

    }

    private void isFailExport(Boolean aBoolean) {
        if(!aBoolean){
            loading.stop();
            mPrivatekey.setEnabled(true);
            mAKeystore.setEnabled(true);
            mMnemonics.setEnabled(true);
        }
    }

    private void getPassword(String s) {
        password = s;
        if(password.length()>30){
            mAKeystore.setVisibility(View.GONE);
        }
    }

    private void hasMnemonics(Boolean aBoolean) {
        if(!aBoolean){

            mMnemonics.setVisibility(View.GONE);

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
        loading.stop();
        mPrivatekey.setEnabled(true);
        mAKeystore.setEnabled(true);
        mMnemonics.setEnabled(true);
        shareTextIntent(s);
    }

    private void exportPrivateKeyString(String s) {
        loading.stop();
        mPrivatekey.setEnabled(true);
        mAKeystore.setEnabled(true);
        mMnemonics.setEnabled(true);
        shareTextIntent(s);
    }

    private void exportKeyStoreString(String s) {
        loading.stop();
        mPrivatekey.setEnabled(true);
        mAKeystore.setEnabled(true);
        mMnemonics.setEnabled(true);
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
                    loading.start();
                    mPrivatekey.setEnabled(false);
                    mAKeystore.setEnabled(false);
                    mMnemonics.setEnabled(false);
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
