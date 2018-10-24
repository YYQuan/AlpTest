package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Account;

import android.arch.lifecycle.ViewModelProviders;
import android.widget.Button;
import android.widget.TextView;

import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.WalletsViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.common.base.widget.RecyclerView.RecyclerAdapter;
import com.alphawizard.hdwallet.common.presenter.BasePresenterFragment;
import com.alphawizard.hdwallet.common.util.Log;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class AccountFragment extends BasePresenterFragment<AccountContract.Presenter,WalletViewModule> implements  AccountContract.View{

    @Inject
    AccountContract.Presenter mPresenter;

    @Inject
    WalletsViewModuleFactory viewModuleFactory;
    WalletViewModule viewModel;

    @BindView(R.id.tv_balances)
    TextView mBalance;

    @BindView(R.id.tv_values)
    TextView mValues;

    @BindView(R.id.btn_send)
    Button  mSend;

    @OnClick(R.id.btn_send)
    void clickBtnSend(){
        viewModel.openSendEth(getActivity());
    }

    @Override
    public AccountContract.Presenter initPresenter() {
        return mPresenter;
    }

    @Override
    public WalletViewModule initViewModule() {
        return viewModel;
    }

    @Override
    public int getContentLayoutID() {
        return  R.layout.fragment_wallet_account;
    }

    @Override
    public void initData() {
        super.initData();
        viewModel = ViewModelProviders.of(this, viewModuleFactory)
                .get(WalletViewModule.class);
        getmPresenter().takeView(this,viewModel);
        viewModel.defaultWallet().observe(this,this::defaultWalletBalanceChange);
        viewModel.defaultWalletBalance().observe(this,this::defaultWalletBalanceChange);
        viewModel.ethValue().observe(this,this::ethValueChange);
        mPresenter.getDefaultWallet();

    }

    private void ethValueChange(String s) {
        mValues.setText(s);
    }

    private void defaultWalletBalanceChange(Wallet wallet) {
        mPresenter.getBalance();
    }

    private void defaultWalletBalanceChange(String s) {
        Log.d(" balance : "+s);
        mBalance.setText(s);
    }

    @Override
    public RecyclerAdapter<Wallet> getRecyclerViewAdapter() {
        return null;
    }

    @Override
    public void onRecyclerChange() {

    }
}
