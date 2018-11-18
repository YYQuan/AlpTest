package com.alphawizard.hdwallet.alphahdwallet.functionModule.send;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.SendViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletActivity;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletActivityContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.common.presenter.BasePresenterToolbarActivity;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;


import net.qiujuer.genius.ui.widget.Loading;

import java.math.BigInteger;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import trust.core.entity.Transaction;

import static com.alphawizard.hdwallet.alphahdwallet.functionModule.send.SendRouter.WALLET_BALANCE;
import static com.alphawizard.hdwallet.alphahdwallet.functionModule.send.SendRouter.WALLET_SEND2ADDRESS;
import static com.alphawizard.hdwallet.alphahdwallet.functionModule.send.SendRouter.WALLET_SEND_AMOUNT;
import static com.alphawizard.hdwallet.alphahdwallet.functionModule.send.SendRouter.WALLET_SEND_TRANSACTION;

public class SendActivity extends BasePresenterToolbarActivity<SendContract.Presenter,SendViewModule> implements SendContract.View{

    private static final int BARCODE_READER_REQUEST_CODE = 1;
    private static final int CAMERA_OK = 1;

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

    @BindView(R.id.iv_code)
    ImageView mCode;

    Transaction transaction;

    @OnClick(R.id.iv_code)
    void onClickCode(){

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

    @OnClick(R.id.iv_back)
    void onClickBack(){
       onBackPressed();
    }

    @BindView(R.id.txt_eth_balance)
    TextView mBalance;

    boolean isInputAddress =false;
    boolean isInputAmounts =false;

    @BindView(R.id.loading)
    Loading loading;

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
    String amount  = "";
    String address = "";
    @Override
    public void initData() {
        super.initData();
        loading.stop();
        viewModel = ViewModelProviders.of(this, viewModuleFactory)
                .get(SendViewModule.class);
        viewModel.progress().observe(this,this::sendCallback);

        getmPresenter().takeView(this,viewModel);

        balance = getIntent().getFloatExtra(WALLET_BALANCE,0);
        mBalance.setText("可用："+balance+"ETH");

        amount = getIntent().getStringExtra(WALLET_SEND_AMOUNT);
        address = getIntent().getStringExtra(WALLET_SEND2ADDRESS);

        transaction = getIntent().getParcelableExtra(WALLET_SEND_TRANSACTION);
        if(transaction !=null){

        }


        if(address!=null&&address.length()>5){
            mAddresss.setText(address);

            WalletActivity.isTransaction =true;

            if(amount!=null&&amount.length()>0){
                mAmount.setText(amount);
                mAddresss.setEnabled(false);
                mAmount.setEnabled(false);
                mCode.setClickable(false);
            }
        }


        mAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                checkInput();
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
                checkInput();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                checkInput();
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
        checkInput();

    }

    private void checkInput(){
        if(mAmount.getText().toString().length()<=0)
            return;
        float inputValue = Float.parseFloat(mAmount.getText().toString());

        if(balance<=inputValue){
            enableNext(false);
            mSend.setText("ETH 余额不足");
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
        mSend.setText("发送");
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
            if(WalletActivity.isTransaction){
                WalletActivity.transactionResult = true;
                onBackPressed();
                return ;
            }
            viewModel.openWallet(this);

        }else{
            loading.stop();
            mSend.setEnabled(true);
            mAddresss.setEnabled(true);
            mAmount.setEnabled(true);
        }
    }

    @OnClick(R.id.btn_send)
    void onClickSend(){
        loading.start();
        mSend.setEnabled(false);
        mAddresss.setEnabled(false);
        mAmount.setEnabled(false);
        if(transaction!=null) {
            BigInteger gasLimit = BigInteger.valueOf(transaction.gasLimit);
            viewModel.sendTransaction(transaction.recipient.toString(), transaction.value, transaction.gasPrice, gasLimit, transaction.nonce, transaction.payload, 4);
            transaction = null;
        }else {
            String address = mAddresss.getText().toString();
            String amounts = mAmount.getText().toString();
            mPresenter.sendTransaction(address, amounts);
        }
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


