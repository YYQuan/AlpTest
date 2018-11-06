package com.alphawizard.hdwallet.alphahdwallet.functionModule.WalletDetail;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.VerifyMnemonicsModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.WalletDetailModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.verifyMnemonics.VerifyMnemonicsContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.verifyMnemonics.VerifyMnemonicsViewModule;
import com.alphawizard.hdwallet.common.presenter.BasePresenterActivity;
import com.alphawizard.hdwallet.common.presenter.BasePresenterToolbarActivity;

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
        viewModel.exportPrivatekey(address);
    }

    @OnClick(R.id.lay_export_keystore)
    void onClickKeystore(){
        viewModel.exportKeystore(address);
    }

    @OnClick(R.id.lay_export_mnemonics)
    void onClickMnemonics(){
        viewModel.exportMnemonics(address);
    }




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
        viewModel.hasMnemonicsString().observe(this,this::hasMnemonics);
        viewModel.getBalance(address);
        viewModel.getWalletName(address);
        viewModel.hasMnemonics(address);
        mAddress.setText(address);

    }

    private void hasMnemonics(Boolean aBoolean) {
        if(!aBoolean){

            mMnemonics.setVisibility(View.GONE);

        }
    }

    private void saveWalletNameSuccess(Boolean aBoolean) {
        if(aBoolean){
            App.showToast(" save  success");
        }
    }

    private void walletNameString(String s) {
        App.showToast(s);
        mName.setText(s);
    }

    private void exportMnemonicsString(String s) {
        App.showToast(s);
    }

    private void exportPrivateKeyString(String s) {
        App.showToast(s);
    }

    private void exportKeyStoreString(String s) {
        App.showToast(s);
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


}
