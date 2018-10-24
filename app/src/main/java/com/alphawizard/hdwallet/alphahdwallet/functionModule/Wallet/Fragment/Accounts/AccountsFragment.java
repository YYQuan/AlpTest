package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Accounts;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphawizard.hdwallet.alphahdwallet.R;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.WalletsViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletActivityContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.common.base.Layout.PlaceHolder.EmptyLayout;
import com.alphawizard.hdwallet.common.base.widget.RecyclerView.RecyclerAdapter;
import com.alphawizard.hdwallet.common.presenter.BasePresenterFragment;
import com.alphawizard.hdwallet.common.util.Log;

import net.qiujuer.genius.ui.compat.UiCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class AccountsFragment extends BasePresenterFragment<AccountsContract.Presenter,WalletViewModule> implements AccountsContract.View {

    @Inject
    AccountsContract.Presenter mPresenter;

    @Inject
    WalletsViewModuleFactory viewModuleFactory;
    WalletViewModule viewModel;

    @BindView(R.id.place_holder)
    EmptyLayout placeHolder;

    @BindView(R.id.recyclerView_accounts)
    RecyclerView recyclerView;

    RecyclerAdapter<Wallet> mAdapter;

    @Override
    public AccountsContract.Presenter initPresenter() {
        return mPresenter;
    }

    @Override
    public WalletViewModule initViewModule() {
        return viewModel;
    }

    @Override
    public int getContentLayoutID() {
        return R.layout.fragment_wallet_accounts;
    }

    @Override
    public void initData() {
        super.initData();
        viewModel = ViewModelProviders.of(this, viewModuleFactory)
                .get(WalletViewModule.class);
        getmPresenter().takeView(this,viewModel);

        viewModel.wallets().observe(this,this::onGetWallets);
        viewModel.defaultWallet().observe(this,this::onDefaultWallet);

    }

    private void onDefaultWallet(Wallet wallet) {
        Log.d("default wallet  address:" +wallet.address);
        mPresenter.onDefaultWalletChange(wallet);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getWallets();
        mPresenter.getDefaultWallet();
    }

    private void onGetWallets(Wallet[] wallets) {
        if(wallets.length>0){
            placeHolder.triggerOkOrEmpty(true);
        }
        mPresenter.refresh( Arrays.asList(wallets));
    }

    @Override
    public void initWidget(View view) {
        super.initWidget(view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter =new RecyclerAdapter<Wallet>() {
            @Override
            public ViewHolder createViewHolder(View view, int type) {
                return new ActionViewHolder(view);
            }

            @Override
            protected int getItemViewType(int position, Wallet session) {
                return R.layout.cell_accounts_list;
            }
        });
        mAdapter.setListener(new RecyclerAdapter.HolderClickListenerImpl<Wallet>() {
            @Override
            public void onClickListener(RecyclerAdapter.ViewHolder holder, Wallet wallet) {
                super.onClickListener(holder, wallet);
                mPresenter.setDefaultWallet(wallet);
            }
        });
        setPlaceHolderView(placeHolder);
        placeHolder.bind(recyclerView);
    }


    @Override
    public void defaultWalletChange(Wallet wallet) {

    }

    @Override
    public RecyclerAdapter<Wallet> getRecyclerViewAdapter() {
        return mAdapter;
    }

    @Override
    public void onRecyclerChange() {
        placeHolder.triggerOkOrEmpty(mAdapter.getItemCount()>=0);
    }


    class ActionViewHolder  extends RecyclerAdapter.ViewHolder<Wallet> {

        @BindView(R.id.txt_title)
        TextView mTitle;

        @BindView(R.id.txt_content)
        TextView mContent;

        @BindView(R.id.iv_eth)
        ImageView mImageView;

        ActionViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(Wallet wallet) {
            String defaultWalletAddress = mPresenter.getDefauleWalletAddress();
            if( defaultWalletAddress.equalsIgnoreCase(wallet.address)){
                mImageView.setBackgroundResource(R.drawable.ic_selecte);
            }else {
                mImageView.setBackgroundResource(R.drawable.ic_not_selecte);
            }
            mTitle.setText(wallet.address);
            mContent.setText(wallet.address);
        }
    }
}
