package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Account;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Transaction;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.WalletsViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Accounts.AccountsFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.alphahdwallet.utils.KeyboardUtils;
import com.alphawizard.hdwallet.alphahdwallet.widget.BackupView;
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

public class AccountFragment extends BasePresenterFragment<AccountContract.Presenter,WalletViewModule> implements  AccountContract.View, TabLayout.BaseOnTabSelectedListener {

    private static final float QR_IMAGE_WIDTH_RATIO = 0.9f;

    @Inject
    AccountContract.Presenter mPresenter;

    @Inject
    WalletsViewModuleFactory viewModuleFactory;
    WalletViewModule viewModel;

    @BindView(R.id.tv_balances)
    TextView mBalance;



    @BindView(R.id.btn_send)
    Button  mSend;

    @BindView(R.id.btn_receive)
    Button  mReceive;

    @BindView(R.id.place_holder)
    EmptyLayout placeHolder;

    @BindView(R.id.recyclerView_transactionBean)
    RecyclerView recyclerView;

    RecyclerAdapter<Transaction.TransactionBean> mAdapter;

    String defaultWalletAddress;

    private Dialog dialog;


    @OnClick(R.id.btn_send)
    void clickBtnSend(){
        viewModel.openSendEth(getActivity());
    }

    @OnClick(R.id.btn_receive)
    void clickBtnReceive(){
        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        int imageSize = (int) (size.x * QR_IMAGE_WIDTH_RATIO);
        Bitmap bitmap = getmPresenter().createQRImage(viewModel.getDefaultWalletAddress(),imageSize);
        showCodeDialog("0x2fa986D54445a0c7e337A735Daf1121a4038474e",bitmap);
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


        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        tabLayout.addTab(tabLayout.newTab().setText("全部"),0);
        tabLayout.addTab(tabLayout.newTab().setText("接收"),1);
        tabLayout.addTab(tabLayout.newTab().setText("发送"),2);

        tabLayout.addOnTabSelectedListener(this);
    }

    private void transBeansChange(List<Transaction.TransactionBean> transactionBeans) {
        Log.d("transBeansChange");
        mAdapter.add(transactionBeans);

        mAdapter.notifyDataSetChanged();
        mPresenter.refresh(  transactionBeans);
//        placeHolder.triggerOkOrEmpty(mAdapter.getDataList().size()>0);
    }

    private void ethValueChange(String s) {

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


    private void showCodeDialog(String  address,Bitmap  bitmap) {
        BackupView view = new BackupView(getActivity(),bitmap);
        dialog = buildDialog()
                .setView(view)
                .create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    private AlertDialog.Builder buildDialog() {
        hideDialog();
        return new AlertDialog.Builder(getActivity());
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        App.showToast(" tab is"+tab.getText());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    class ActionViewHolder  extends RecyclerAdapter.ViewHolder<Transaction.TransactionBean> {

        @BindView(R.id.txt_address)
        TextView mAddress;

        @BindView(R.id.txt_value)
        TextView mValue;

        @BindView(R.id.txt_time)
        TextView mTime;

        ActionViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(Transaction.TransactionBean bean) {
            if(bean.getTo().equalsIgnoreCase(defaultWalletAddress)){
                mAddress.setText(bean.getFrom());
            }else{
                mAddress.setText(bean.getTo());
            }

            mValue .setText(bean.getValue());

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis((bean.getTimeStamp()) * DateUtils.SECOND_IN_MILLIS);
            mTime.setText(""+calendar.getTime() );
        }
    }
}

