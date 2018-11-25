package com.alphawizard.hdwallet.alphahdwallet.functionModule.ConfirmSend;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.percent.PercentRelativeLayout;
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


    @OnClick(R.id.layout_setting_senior)
    void  onClickSettingSenior(){
        mSetting.setVisibility(View.GONE);
    }

    @OnClick(R.id.layout_setting_senior_cancel)
    void  onClickSettingSeniorCancel(){
        mSetting.setVisibility(View.VISIBLE);
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
    }

    @Override
    public void initWidget() {
        super.initWidget();
        //        透明状态栏 ， 这种方式不会引起  崩溃
        StatusBarUtil.transparencyBar(this);
    }
}
