package com.alphawizard.hdwallet.alphahdwallet.functionModule.ConfirmSend;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.percent.PercentRelativeLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.BackupModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.ConfirmSendModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupMnemonicsActivity;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupViewModule;
import com.alphawizard.hdwallet.alphahdwallet.utils.StatusBarUtil;
import com.alphawizard.hdwallet.common.presenter.BasePresenterActivity;
import com.alphawizard.hdwallet.common.util.Log;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.alphawizard.hdwallet.alphahdwallet.functionModule.ConfirmSend.ConfirmSendRouter.SEND_REQUEST_AMOUNT;
import static com.alphawizard.hdwallet.alphahdwallet.functionModule.ConfirmSend.ConfirmSendRouter.SEND_REQUEST_FROM;
import static com.alphawizard.hdwallet.alphahdwallet.functionModule.ConfirmSend.ConfirmSendRouter.SEND_REQUEST_GAS;
import static com.alphawizard.hdwallet.alphahdwallet.functionModule.ConfirmSend.ConfirmSendRouter.SEND_REQUEST_TO;

public class ConfirmSendActivity extends BasePresenterActivity<ConfirmSendContract.Presenter,ConfirmSendViewModule> implements ConfirmSendContract.View {

    public  static  void show(Context context){
        context.startActivity(new Intent(context, ConfirmSendActivity.class));
    }
    @Inject
    ConfirmSendModuleFactory viewModuleFactory;
    ConfirmSendViewModule viewModel;

    @Inject
    ConfirmSendContract.Presenter mPresenter;


    @BindView(R.id.iv_back)
    ImageView mBack;



    @BindView(R.id.txt_eth_amount)
    TextView  mEth;


    @BindView(R.id.txt_from_content)
    TextView mTxtForm;


    @BindView(R.id.txt_to_content)
    TextView  mTxtTo;


    @BindView(R.id.txt_gas_content)
    TextView mTxtGas;


    @BindView(R.id.layout_setting)
    PercentRelativeLayout mSetting;


    @BindView(R.id.layout_setting_senior)
    LinearLayout mSettingSenior;

    @BindView(R.id.edit_gas_price)
    EditText mGasPrice;

    @BindView(R.id.edit_gas_limit)
    EditText  mGasLimit;

    @BindView(R.id.edit_data)
    EditText mEditData;

    @BindView(R.id.layout_setting_senior_cancel)
    LinearLayout mSeniorCancel;

    @BindView(R.id.btn_send)
    Button  mSend;



    String  from;
    String  to ;
    String  amount ;
    String gas ;

    @OnClick(R.id.layout_setting_senior)
    void  onClickSettingSenior(){
        mSetting.setVisibility(View.GONE);
    }

    @OnClick(R.id.layout_setting_senior_cancel)
    void  onClickSettingSeniorCancel(){
        mSetting.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_send)
    void  onClickSend(){
//        viewModel.sendTransaction();
    }


    @Override
    public int getContentLayoutID() {
        return R.layout.activity_confirm_send;
    }

    @Override
    public ConfirmSendContract.Presenter initPresenter() {
        return mPresenter;
    }

    @Override
    public ConfirmSendViewModule initViewModule() {
        return viewModel;
    }

    @Override
    public void initData() {
        super.initData();
        viewModel = ViewModelProviders.of(this, viewModuleFactory)
                .get(ConfirmSendViewModule.class);
        mPresenter.takeView(this,viewModel);



        from =getIntent().getStringExtra(SEND_REQUEST_FROM);
        to =getIntent().getStringExtra(SEND_REQUEST_TO);
        amount =getIntent().getStringExtra(SEND_REQUEST_AMOUNT);
        gas =getIntent().getStringExtra(SEND_REQUEST_GAS);

        mTxtForm.setText(from);
        mTxtTo.setText(to);
        mEth.setText(amount);
        mTxtGas.setText(gas);


        mGasPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculatorGasprice();
            }
        });
        mGasLimit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculatorGasprice();
            }
        });

    }

    @Override
    public void initWidget() {
        super.initWidget();
        //        透明状态栏 ， 这种方式不会引起  崩溃
        StatusBarUtil.transparencyBar(this);
    }


    private void   calculatorGasprice(){
        String price = mGasPrice.getText().toString();
        String limit = mGasLimit.getText().toString();
        Long priceL  = Long.valueOf(price);
        Long limitL  = Long.valueOf(limit);
        Log.d("calculatorGasprice priceL  = " +priceL);
        Log.d("calculatorGasprice limitL  = " +limitL);
        Log.d("calculatorGasprice priceL * limitL  = " +(priceL * limitL));

        float  sum  = priceL * limitL /(10e9f);
        String  sumString = String.valueOf(sum);
        Log.d("calculatorGasprice sum  = " +sum);
        Log.d("calculatorGasprice sumString  = " +sumString);
        mTxtGas.setText(formatFloatNumber(sum)+" ETH");
    }

    public static String formatFloatNumber(double value) {
        if(value != 0.00){
            java.text.DecimalFormat df = new java.text.DecimalFormat("########.00000000");
            return df.format(value);
        }else{
            return "0.00";
        }

    }

    public  String formatFloatNumber(Double value) {
        if(value != null){
            if(value.doubleValue() != 0.00){
                java.text.DecimalFormat df = new java.text.DecimalFormat("########.00");
                return df.format(value.doubleValue());
            }else{
                return "0.00";
            }
        }
        return "";
    }
}
