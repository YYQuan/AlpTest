package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Account;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Transaction;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.WalletsViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Accounts.AccountsFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.common.base.Layout.PlaceHolder.EmptyLayout;
import com.alphawizard.hdwallet.common.base.widget.RecyclerView.RecyclerAdapter;
import com.alphawizard.hdwallet.common.presenter.BasePresenterFragment;
import com.alphawizard.hdwallet.common.util.Log;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

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

    @BindView(R.id.place_holder)
    EmptyLayout placeHolder;

    @BindView(R.id.recyclerView_transactionBean)
    RecyclerView recyclerView;

    RecyclerAdapter<Transaction.TransactionBean> mAdapter;

    String defaultWalletAddress;

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
        viewModel.transactionBeans().observe(this,this::transBeansChange);
        mPresenter.getDefaultWallet();

    }

    @Override
    public void initWidget(View view) {
        super.initWidget(view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter =new RecyclerAdapter<Transaction.TransactionBean>() {
            @Override
            public ViewHolder createViewHolder(View view, int type) {
                return new AccountFragment.ActionViewHolder(view);
            }

            @Override
            protected int getItemViewType(int position, Transaction.TransactionBean session) {
                return R.layout.cell_account_list;
            }
        });
        mAdapter.setListener(new RecyclerAdapter.HolderClickListenerImpl<Transaction.TransactionBean>() {
            @Override
            public void onClickListener(RecyclerAdapter.ViewHolder holder, Transaction.TransactionBean wallet) {
                super.onClickListener(holder, wallet);
            }
        });
        setPlaceHolderView(placeHolder);
        placeHolder.bind(recyclerView);
    }

    private void transBeansChange(List<Transaction.TransactionBean> transactionBeans) {
        Log.d("transBeansChange");
        mAdapter.add(transactionBeans);

        mAdapter.notifyDataSetChanged();
        mPresenter.refresh(  transactionBeans);
        placeHolder.triggerOkOrEmpty(mAdapter.getDataList().size()>0);
    }

    private void ethValueChange(String s) {
        mValues.setText(s);
    }

    private void defaultWalletBalanceChange(Wallet wallet) {
        mPresenter.getBalance();
        defaultWalletAddress = wallet.address;
    }

    private void defaultWalletBalanceChange(String s) {
        Log.d(" balance : "+s);
        mBalance.setText(s);
    }

    @Override
    public RecyclerAdapter<Transaction.TransactionBean> getRecyclerViewAdapter() {
        return mAdapter;
    }

    @Override
    public void onRecyclerChange() {

    }

    class ActionViewHolder  extends RecyclerAdapter.ViewHolder<Transaction.TransactionBean> {

        @BindView(R.id.txt_title)
        TextView mTitle;

        @BindView(R.id.txt_content)
        TextView mContent;

        @BindView(R.id.txt_time)
        TextView mTime;

        ActionViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(Transaction.TransactionBean bean) {
            if(bean.getTo().equalsIgnoreCase(defaultWalletAddress)){
                mTitle.setText("receive  from  :"+bean.getFrom());
            }else{
                mTitle.setText("send   to  :"+bean.getTo());
            }

            mContent .setText("value   :"+bean.getValue());

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis((bean.getTimeStamp()) * DateUtils.SECOND_IN_MILLIS);
            mTime.setText("time :"+calendar.getTime() );
        }
    }
}
