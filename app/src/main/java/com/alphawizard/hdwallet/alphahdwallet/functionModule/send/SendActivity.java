package com.alphawizard.hdwallet.alphahdwallet.functionModule.send;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.SendViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletActivityContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.common.presenter.BasePresenterToolbarActivity;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.alphawizard.hdwallet.alphahdwallet.functionModule.send.SendRouter.WALLET_BALANCE;

public class SendActivity extends BasePresenterToolbarActivity<SendContract.Presenter,SendViewModule> implements SendContract.View{

    private static final int BARCODE_READER_REQUEST_CODE = 1;


    @Inject
    SendContract.Presenter mPresenter;

    @Inject
    SendViewModuleFactory viewModuleFactory;
    SendViewModule viewModel;


    @BindView(R.id.ed_eth_address)
    EditText  mAddresss;

    @BindView(R.id.ed_eth_amounts)
    EditText  mAmount;

    @BindView(R.id.btn_send)
    Button mSend;



    @OnClick(R.id.iv_code)
    void onClickCode(){
        Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
        startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
    }

    @OnClick(R.id.iv_back)
    void onClickBack(){
       onBackPressed();
    }

    @BindView(R.id.txt_eth_balance)
    TextView mBalance;

    boolean isInputAddress =false;
    boolean isInputAmounts =false;

    @Override
    public SendContract.Presenter initPresenter() {
        return mPresenter;
    }

    @Override
    public SendViewModule initViewModule() {
        return viewModel;
    }

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_send;
    }

    float balance = 0f;
    @Override
    public void initData() {
        super.initData();
        viewModel = ViewModelProviders.of(this, viewModuleFactory)
                .get(SendViewModule.class);
        viewModel.progress().observe(this,this::sendCallback);

        getmPresenter().takeView(this,viewModel);

        balance = getIntent().getFloatExtra(WALLET_BALANCE,0);
        mBalance.setText("可用："+balance+"ETH");

        mAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(mAmount.getText().length()>0){
                    isInputAmounts =true;
                    if(isInputAddress){
                        checkInput();
                    }
                }else{
                    isInputAmounts =false;
                    enableNext(false);
                }



            }
        });
        mAddresss.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(mAddresss.getText().length()>0){
                    isInputAddress =true;
                    if(isInputAmounts){
                        checkInput();
                    }
                }else{
                    isInputAddress =false;
                    enableNext(false);
                }
            }
        });


    }

    private void checkInput(){
        float inputValue = Float.parseFloat(mAmount.getText().toString());

        if(balance<=inputValue){
            enableNext(false);
            return ;
        }
        if(mAddresss.getText().toString().length()<15){
            return;
        }
        String subAddress = mAddresss.getText().toString().substring(0,2);

        if("0x".equalsIgnoreCase(subAddress)){
            enableNext(true);
        }
    }

    private void enableNext(boolean isEnable){
        if(!isEnable) {
            mSend.setBackgroundResource(R.drawable.bg_color_393a50);
            mSend.setEnabled(false);
        }else {
            mSend.setBackgroundResource(R.drawable.bg_gradient_blue);
            mSend.setEnabled(true);
        }
    }

    @Override
    public void initWidget() {
        super.initWidget();

        ActionBar actionBar = getSupportActionBar();

//      隐藏toolbar上的 back btn
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("");
        }

        mSend.setEnabled(false);
    }

    private void sendCallback(Boolean aBoolean) {
        if(aBoolean) {
            viewModel.openWallet(this);
        }
    }

    @OnClick(R.id.btn_send)
    void onClickSend(){
        String address = mAddresss.getText().toString();
        String amounts = mAmount.getText().toString();
        mPresenter.sendTransaction(address,amounts);
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

                    mAddresss.setText(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
//                    ToastUtil.show(TabMainActivity.this, getString(R.string.analyse_qrcode_fail), Toast.LENGTH_LONG);
                }
            }
        }
    }
}

