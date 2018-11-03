package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.setting;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.WalletsViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Accounts.AccountsContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.alphahdwallet.utils.KeyboardUtils;
import com.alphawizard.hdwallet.common.base.Layout.PlaceHolder.EmptyLayout;
import com.alphawizard.hdwallet.common.base.widget.RecyclerView.RecyclerAdapter;
import com.alphawizard.hdwallet.common.presenter.BasePresenterFragment;
import com.alphawizard.hdwallet.common.util.Log;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingFragment extends BasePresenterFragment<SettingContract.Presenter,WalletViewModule> implements SettingContract.View {

    @Inject
    SettingContract.Presenter mPresenter;

    @Inject
    WalletsViewModuleFactory viewModuleFactory;
    WalletViewModule viewModel;


    @BindView(R.id.layout_manager_wallet)
    LinearLayout linearLayout;

    @OnClick(R.id.layout_manager_wallet)
    void onClickManagerWallet(){
        viewModel.openManagerRouter(getActivity());
    }

    @Override
    public SettingContract.Presenter initPresenter() {
        return mPresenter;
    }

    @Override
    public WalletViewModule initViewModule() {
        return viewModel;
    }

    @Override
    public int getContentLayoutID() {
        return R.layout.fragment_wallet_setting;
    }

    @Override
    public void initData() {
        super.initData();
        viewModel = ViewModelProviders.of(this, viewModuleFactory)
                .get(WalletViewModule.class);
        getmPresenter().takeView(this,viewModel);
    }



    @Override
    public void onResume() {
        super.onResume();
    }



    @Override
    public void initWidget(View view) {
        super.initWidget(view);

    }





}
