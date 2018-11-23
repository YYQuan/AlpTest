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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class AccountFragment extends BasePresenterFragment<AccountContract.Presenter,WalletViewModule> implements  AccountContract.View, TabLayout.BaseOnTabSelectedListener {

    private static final float QR_IMAGE_WIDTH_RATIO = 0.9f;
    private static final int  TAB_ALL = 0;
    private static final int  TAB_RECEIVE = 1;
    private static final int  TAB_SEND = 2;

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
    RecyclerAdapter<Transaction.TransactionBean> mReceiveAdapter;
    RecyclerAdapter<Transaction.TransactionBean> mSendAdapter;

    String defaultWalletAddress;


    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private Dialog dialog;



    @OnClick(R.id.btn_send)
    void clickBtnSend(){
        float i=Float.parseFloat(mBalanceString);
        viewModel.openSendEth(getActivity(),i);
    }

    @OnClick(R.id.btn_receive)
    void clickBtnReceive(){
        viewModel.openReceiver(getActivity());
//        Point size = new Point();
//        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
//        int imageSize = (int) (size.x * QR_IMAGE_WIDTH_RATIO);
//        if(defaultWalletAddress!=null){
//            Bitmap bitmap = getmPresenter().createQRImage(defaultWalletAddress,imageSize);
//            showCodeDialog(defaultWalletAddress,bitmap);
//        }
    }

//    @OnClick(R.id.iv_setting)
//    void onClickSetting(){
//        viewModel.openManagerRouter(getActivity());
//    }

    @OnClick(R.id.layout_setting)
    void onClickSetting(){
        viewModel.openManagerRouter(getActivity());
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
        viewModel.defaultWallet().observe(this,this::defaultWalletChange);
        viewModel.defaultWalletBalance().observe(this,this::defaultWalletBalanceChange);
        viewModel.ethValue().observe(this,this::ethValueChange);
        viewModel.transactionBeans().observe(this,this::transBeansChange);
        mPresenter.getDefaultWallet();


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter =new RecyclerAdapter<Transaction.TransactionBean>() {
            @Override
            public ViewHolder createViewHolder(View view, int type) {
                return new AccountFragment.ActionViewHolder(view);
            }

            @Override
            protected int getItemViewType(int position, Transaction.TransactionBean session) {
                return R.layout.cell_account_list;
            }
        };

        mReceiveAdapter =new RecyclerAdapter<Transaction.TransactionBean>() {
            @Override
            public ViewHolder createViewHolder(View view, int type) {
                return new AccountFragment.ActionViewHolder(view);
            }

            @Override
            protected int getItemViewType(int position, Transaction.TransactionBean session) {
                return R.layout.cell_account_list;
            }
        };

        mSendAdapter =new RecyclerAdapter<Transaction.TransactionBean>() {
            @Override
            public ViewHolder createViewHolder(View view, int type) {
                return new AccountFragment.ActionViewHolder(view);
            }

            @Override
            protected int getItemViewType(int position, Transaction.TransactionBean session) {
                return R.layout.cell_account_list;
            }
        };

        recyclerView.setAdapter(mAdapter);

        mAdapter.setListener(new RecyclerAdapter.HolderClickListenerImpl<Transaction.TransactionBean>() {
            @Override
            public void onClickListener(RecyclerAdapter.ViewHolder holder, Transaction.TransactionBean wallet) {
                super.onClickListener(holder, wallet);
            }
        });

        mSendAdapter.setListener(new RecyclerAdapter.HolderClickListenerImpl<Transaction.TransactionBean>() {
            @Override
            public void onClickListener(RecyclerAdapter.ViewHolder holder, Transaction.TransactionBean wallet) {
                super.onClickListener(holder, wallet);
            }
        });

        mReceiveAdapter.setListener(new RecyclerAdapter.HolderClickListenerImpl<Transaction.TransactionBean>() {
            @Override
            public void onClickListener(RecyclerAdapter.ViewHolder holder, Transaction.TransactionBean wallet) {
                super.onClickListener(holder, wallet);
            }
        });

    }

    @Override
    public void initWidget(View view) {
        super.initWidget(view);
//        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
//        .setCustomView(R.layout.cell_tab_item)
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.wallet_account_transaction_all)),TAB_ALL);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.wallet_account_transaction_receiver)),TAB_RECEIVE);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.wallet_account_transaction_send)),TAB_SEND);

        tabLayout.addOnTabSelectedListener(this);

        setPlaceHolderView(placeHolder);
        placeHolder.bind(recyclerView);
    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getDefaultWallet();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void transBeansChange(List<Transaction.TransactionBean> transactionBeans) {
        Log.d("transBeansChange");


        mAdapter.replace(transactionBeans);
        mReceiveAdapter.replace(filterReceiveTransaction(transactionBeans));
        mSendAdapter.replace(filterSendTransaction(transactionBeans));

        switch (tabLayout.getSelectedTabPosition()){
            case TAB_ALL:
                mAdapter.notifyDataSetChanged();
                break;
            case TAB_RECEIVE:
                mReceiveAdapter.notifyDataSetChanged();
                break;
            case TAB_SEND:
                mSendAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
        mPresenter.refresh(transactionBeans);
//        placeHolder.triggerOkOrEmpty(mAdapter.getDataList().size()>0);
    }

    private List<Transaction.TransactionBean> filterReceiveTransaction(List<Transaction.TransactionBean> list){
        List<Transaction.TransactionBean> receiveList = new ArrayList<>();
        for(Transaction.TransactionBean bean:list){
            if(bean.getTo().equalsIgnoreCase(defaultWalletAddress)){
                receiveList.add(bean);
            }
        }
        return  receiveList;
    }

    private List<Transaction.TransactionBean> filterSendTransaction(List<Transaction.TransactionBean> list){
        List<Transaction.TransactionBean> receiveList = new ArrayList<>();
        for(Transaction.TransactionBean bean:list){
            if(bean.getFrom().equalsIgnoreCase(defaultWalletAddress)){
                receiveList.add(bean);
            }
        }
        return  receiveList;
    }


    private void ethValueChange(String s) {

    }

    private void defaultWalletChange(Wallet wallet) {
        defaultWalletAddress = wallet.address;
        viewModel.getBalanceCyclical();

    }

    String mBalanceString = "0";
    private void defaultWalletBalanceChange(String s) {
        Log.d(" balance : "+s);
        mBalanceString = s;
        mBalance.setText(s + "ETH");
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
//        App.showToast(" tab is"+tab.getText());
        switch(tab.getPosition()){
            case TAB_ALL:
                mAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(mAdapter);
                break;
            case TAB_RECEIVE:
                mAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(mReceiveAdapter);
                break;
            case TAB_SEND:
                mAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(mSendAdapter);
                break;
            default:
                break;
        }

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

        @BindView(R.id.txt_sign)
        TextView mSign;


        ActionViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(Transaction.TransactionBean bean) {
            if(bean.getTo().equalsIgnoreCase(defaultWalletAddress)&&!bean.getFrom().equalsIgnoreCase(defaultWalletAddress)){
//                接收
                mAddress.setText(bean.getFrom());
                mSign.setText("+");
                mSign.setTextColor(getResources().getColor(R.color.colorGreen));
            }else{
//                发送
                mAddress.setText(bean.getTo());
                mSign.setText("-");
                mSign.setTextColor(getResources().getColor(R.color.colorRed));
            }

//            int i = Integer.parseInt(bean.getValue());
            String value  = "";
            if(bean.getValue().length()>14){
                value = bean.getValue().substring(0,bean.getValue().length()-14);
                int i = Integer.parseInt(value);
                int ethNum = i/10000;
                int ethPoint = i%10000;
                mValue.setText(ethNum+"."+ethPoint+"ETH");
            }else{
                mValue.setText("0.0"+"ETH");
            }


//            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
//            calendar.setTimeInMillis((bean.getTimeStamp()) * DateUtils.SECOND_IN_MILLIS);
//            "yyyy-MM-dd HH:mm:ss"
            String  time  =getDateToString((bean.getTimeStamp()) * DateUtils.SECOND_IN_MILLIS
                    , "yyyy MM dd HH:mm");
            mTime.setText("" +time );
        }
    }

    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

}

