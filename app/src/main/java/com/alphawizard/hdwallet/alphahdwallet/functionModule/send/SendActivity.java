package com.alphawizard.hdwallet.alphahdwallet.functionModule.send;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ConfirmSend.ConfirmSendRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.SendViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletActivity;
import com.alphawizard.hdwallet.alphahdwallet.utils.BalanceUtils;
import com.alphawizard.hdwallet.alphahdwallet.utils.StatusBarUtil;
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
                Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
                startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
            }
        }else {
            //这个说明系统版本在6.0之下，不需要动态获取权限。

            Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
            startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
        }



    }

//    @OnClick(R.id.iv_back)
//    void onClickBack(){
//       onBackPressed();
//    }

    @OnClick(R.id.lay_back)
    void onClickLayBack(){
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
    boolean  isGame  =false;
    @Override
    public void initData() {
        super.initData();
        loading.stop();
        viewModel = ViewModelProviders.of(this, viewModuleFactory)
                .get(SendViewModule.class);
        viewModel.progress().observe(this,this::sendCallback);

        getmPresenter().takeView(this,viewModel);

        balance = getIntent().getFloatExtra(WALLET_BALANCE,0);

        String  balanceStr = getResources().getString(R.string.send_has_amount);
        mBalance.setText(balanceStr+balance+"ETH");

        amount = getIntent().getStringExtra(WALLET_SEND_AMOUNT);
        address = getIntent().getStringExtra(WALLET_SEND2ADDRESS);

        transaction = getIntent().getParcelableExtra(WALLET_SEND_TRANSACTION);



        if(address!=null&&address.length()>5){
            mAddresss.setText(address);

            mAddresss.setEnabled(false);
            mAmount.setEnabled(false);
            mCode.setClickable(false);

            if(amount!=null&&amount.length()>0){
                mAmount.setText(amount);
                mSend.setEnabled(true);
                isGame = true;
            }
        }else{
            mAddresss.setEnabled(true);
            mAmount.setEnabled(true);
            mCode.setClickable(true);
        }


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
                String amountTrue  = getResources().getString(R.string.send_btn_send);
                String amountError  = getResources().getString(R.string.send_btn_send_amount_error);



                if(mAmount.getText().toString().length()<=0) {
                    enableNext(false);
                    mSend.setText(amountTrue);
                    isInputAmounts =false;
                    return ;
                }else{
                    float inputValue = Float.parseFloat(mAmount.getText().toString());
                    if(balance<=inputValue){
                        enableNext(false);
                        mSend.setText(amountError);
                        isInputAmounts =false;
                        return ;
                    }
                    mSend.setText(amountTrue);
                    isInputAmounts =true;
                }


                if(isInputAmounts&&isInputAddress){
                    enableNext(true);
                }


            }
        });
        mAddresss.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String addressTrue  = getResources().getString(R.string.send_btn_send);
                String addressError  = getResources().getString(R.string.send_btn_send_addresss_error);
                mSend.setText(addressTrue);
                if(0<s.toString().length()&&s.toString().length()<=2){
                    if(s.toString().length()==1){
                        if(s.toString().equalsIgnoreCase("0")){
                            return ;
                        }else{
                            mSend.setText(addressError);
                        }
                    }
                    if(s.toString().length()==2){
                        if(s.toString().equalsIgnoreCase("0x")){
                            return ;
                        }else{
                            mSend.setText(addressError);
                        }
                    }
                }

                if(mAddresss.getText().length()>30){
                    isInputAddress =true;
                }else{
                    isInputAddress =false;
                    mSend.setText(addressError);
                    enableNext(false);
                }

                if(isInputAmounts&&isInputAddress){
                    enableNext(true);
                }

            }
        });

        checkEnable();
    }


    private void checkEnable(){
        String amountTrue  = getResources().getString(R.string.send_btn_send);
        String amountError  = getResources().getString(R.string.send_btn_send_amount_error);
        if(transaction !=null){
            float inputValue = Float.parseFloat(mAmount.getText().toString());
            if(balance<=inputValue){
                enableNext(false);
                mSend.setText(amountError);
                isInputAmounts =false;
                return ;
            }else{
                enableNext(true);
                mSend.setText(amountTrue);
                isInputAmounts =true;
            }
        }
    }

    private void enableNext(boolean isEnable){

        if(!isEnable) {
            mSend.setBackgroundResource(R.drawable.bg_color_dae6ff);
            mSend.setEnabled(false);
        }else {
            String addressTrue  = getResources().getString(R.string.send_btn_send);
            mSend.setText(addressTrue);
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

        //        透明状态栏 ， 这种方式不会引起  崩溃
        StatusBarUtil.transparencyBar(this);

        mSend.setEnabled(false);

    }

    private void sendCallback(Boolean aBoolean) {

    }

    @OnClick(R.id.btn_send)
    void onClickSend(){
        loading.start();
        mSend.setEnabled(false);
        mAddresss.setEnabled(false);
        mCode.setClickable(false);
        mAmount.setEnabled(false);
        if(transaction!=null) {



//            viewModel.sendTransaction(transaction.recipient.toString(), transaction.value, transaction.gasPrice, gasLimit, transaction.nonce, transaction.payload, 4);
            long   priceLong  = (long) ( transaction.gasPrice.longValue()/(10e8f));

            String  amount =  null;
            try {
                amount =  BalanceUtils.weiToEth(transaction.value,6);


            } catch (Exception e) {
                e.printStackTrace();
            }
            viewModel.openConfirm(this,transaction.recipient.toString(), amount,priceLong,transaction.gasLimit,transaction.payload);
            transaction = null;


        }else {

            String amounts = mAmount.getText().toString();
//            mPresenter.sendTransaction(mAddresss.getText().toString(), amounts);

            BigInteger gasBidInteger = BalanceUtils.baseToSubunit("21", 13);
            String  gas = String.valueOf(gasBidInteger.longValue());
            viewModel.openConfirm(this,mAddresss.getText().toString(), amounts, ConfirmSendRouter.DEFAULT_GAS_PRICE,ConfirmSendRouter.DEFAULT_GAS_LIMIT,"");

//            viewModel.openConfirm(this,address, amounts,gas);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loading.stop();
        if(isGame){
            mAddresss.setEnabled(false);
            mAmount.setEnabled(false);
            mCode.setClickable(false);
            mSend.setEnabled(true);
        }else{
            mAddresss.setEnabled(true);
            mAmount.setEnabled(true);
            mSend.setEnabled(true);
            mCode.setClickable(true);
        }

    }


    //activity权限授权结果回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
        startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
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


