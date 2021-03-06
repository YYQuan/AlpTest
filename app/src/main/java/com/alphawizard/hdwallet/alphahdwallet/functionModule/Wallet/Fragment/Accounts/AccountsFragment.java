package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Accounts;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.WalletsViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletActivityContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.alphahdwallet.utils.KeyboardUtils;
import com.alphawizard.hdwallet.alphahdwallet.widget.BackupView;
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

    private Dialog dialog;


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
        viewModel.exportedStore().observe(this,this::onExportWallet);

    }

    private void onExportWallet(String s) {
        Log.d("keystore : "+s);
        showBackupKeystoreDialog(s);
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

            @Override
            public boolean onLongClickListener(RecyclerAdapter.ViewHolder holder, Wallet wallet) {
//                viewModel.exportAccount(wallet,"123");
                showBackupDialog(wallet);
                return super.onLongClickListener(holder, wallet);
            }
        });
        setPlaceHolderView(placeHolder);
        placeHolder.bind(recyclerView);
    }






    @Override
    public RecyclerAdapter<Wallet> getRecyclerViewAdapter() {
        return mAdapter;
    }

    @Override
    public void onRecyclerChange() {
        placeHolder.triggerOkOrEmpty(mAdapter.getItemCount()>=0);
    }



    private void showBackupDialog(Wallet wallet) {
//        BackupView view = new BackupView(getActivity());
//        dialog = buildDialog()
//                .setView(view)
//                .setPositiveButton("ok",
//                        (dialogInterface, i) -> {
//                            viewModel.exportAccount(wallet, view.getPassword());
//                            KeyboardUtils.hideKeyboard(view.findViewById(R.id.password));
//                        })
//                .setNegativeButton("cancel", (dialogInterface, i) -> {
//                    KeyboardUtils.hideKeyboard(view.findViewById(R.id.password));
//                })
//                .setOnDismissListener(dialog -> KeyboardUtils.hideKeyboard(view.findViewById(R.id.password)))
//                .create();
//        dialog.show();
    }

    private void showBackupKeystoreDialog(String string) {
//        BackupView view = new BackupView(getActivity());
        View   view = View.inflate(getActivity(),R.layout.layout_dialog_copeboard,null);
        TextView tv  = view.findViewById(R.id.tv_keystore);
        tv.setText(string);
        dialog = buildDialog()
                .setView(view)
                .setPositiveButton("copy",
                        (dialogInterface, i) -> {
                            ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                            // 将文本内容放到系统剪贴板里。
                            cm.setText(tv.getText());
                            KeyboardUtils.hideKeyboard(view.findViewById(R.id.tv_keystore));
                            App.showToast("已复制 keystore");
                        })
                .setNegativeButton("cancel", (dialogInterface, i) -> {
                    KeyboardUtils.hideKeyboard(view.findViewById(R.id.tv_keystore));
                })
                .setOnDismissListener(dialog -> KeyboardUtils.hideKeyboard(view.findViewById(R.id.tv_keystore)))
                .create();
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

    class ActionViewHolder  extends RecyclerAdapter.ViewHolder<Wallet> {



        @BindView(R.id.txt_eth_address)
        TextView mContent;


        ActionViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(Wallet wallet) {


            mContent.setText(wallet.address);
        }
    }
}
