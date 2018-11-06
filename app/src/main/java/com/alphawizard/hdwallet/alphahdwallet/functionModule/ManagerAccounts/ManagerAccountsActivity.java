package com.alphawizard.hdwallet.alphahdwallet.functionModule.ManagerAccounts;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.BackupModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.ManagerAccountsViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Accounts.AccountsContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Accounts.AccountsFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupViewModule;
import com.alphawizard.hdwallet.alphahdwallet.interact.CreateWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.utils.KeyboardUtils;
import com.alphawizard.hdwallet.common.base.Layout.PlaceHolder.EmptyLayout;
import com.alphawizard.hdwallet.common.base.widget.RecyclerView.RecyclerAdapter;
import com.alphawizard.hdwallet.common.presenter.BasePresenterActivity;
import com.alphawizard.hdwallet.common.presenter.BasePresenterToolbarActivity;
import com.alphawizard.hdwallet.common.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class ManagerAccountsActivity extends BasePresenterToolbarActivity<ManagerAccountsContract.Presenter,ManagerAccountsViewModule> implements ManagerAccountsContract.View {


    @Inject
    ManagerAccountsViewModuleFactory managerAccountsViewModuleFactory;
    ManagerAccountsViewModule viewModel;

    @Inject
    ManagerAccountsContract.Presenter mPresenter;

    @BindView(R.id.place_holder)
    EmptyLayout placeHolder;

    @BindView(R.id.recyclerView_accounts)
    RecyclerView recyclerView;



    @BindView(R.id.btn_create_account)
    Button  btnCreate;

    @BindView(R.id.btn_import_account)
    Button  btnImport;


    @OnClick(R.id.btn_create_account)
    void onClickCreate(){
        viewModel.newWallet("Wallet");
    }

    @OnClick(R.id.btn_import_account)
    void onClickImport(){
        viewModel.openImport(this);
    }

    RecyclerAdapter<Wallet> mAdapter;

    private Dialog dialog;
    String  mnenonics ;


    @Override
    public int getContentLayoutID() {
        return R.layout.activity_manager_accounts;
    }

    @Override
    public ManagerAccountsContract.Presenter initPresenter() {
        return mPresenter;
    }

    @Override
    public ManagerAccountsViewModule initViewModule() {
        return viewModel;
    }

    @Override
    public void initData() {
        super.initData();
        viewModel = ViewModelProviders.of(this, managerAccountsViewModuleFactory)
                .get(ManagerAccountsViewModule.class);
        getmPresenter().takeView(this,viewModel);

        viewModel.wallets().observe(this,this::onGetWallets);
        viewModel.createWalletEntity().observe(this,this::onCreateWalletEntity);
        viewModel.createdWallet().observe(this,this::onCreatedWallet);


    }

    private void onCreatedWallet(Wallet wallet) {
        viewModel.openBackup(this,string2StringList(mnenonics));
    }


    private ArrayList<String> string2StringList(String mnenonics){
        ArrayList<String> list = new ArrayList<>();
        String patternStr = "(\\s*=\\s*)|(\\s*,\\s*)|(\\s*;\\s*)|(\\s+)";
        Pattern pattern = Pattern.compile(patternStr);
        String[] dataArr = pattern.split(mnenonics);

        for(int i = 0 ; i<dataArr.length;i++){
            list.add(dataArr[i]);
        }
        return list;
    }

    private void onCreateWalletEntity(CreateWalletInteract.CreateWalletEntity createWalletEntity) {
        mnenonics = createWalletEntity.getMnenonics();
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
    public void initWidget() {
        super.initWidget();
        ActionBar actionBar = getSupportActionBar();
        //      隐藏toolbar上的 back btn
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(false);
//            actionBar.setTitle("ETH");
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                viewModel.openWalletDetail(ManagerAccountsActivity.this,wallet.address);
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
        View   view = View.inflate(this,R.layout.layout_dialog_copeboard,null);
        TextView tv  = view.findViewById(R.id.tv_keystore);
        tv.setText(string);
        dialog = buildDialog()
                .setView(view)
                .setPositiveButton("copy",
                        (dialogInterface, i) -> {
                            ClipboardManager cm = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
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
        return new AlertDialog.Builder(this);
    }

    public  void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    class ActionViewHolder  extends RecyclerAdapter.ViewHolder<Wallet> implements  View.OnClickListener {

        @BindView(R.id.tv_eth_address)
        TextView mContent;

        @BindView(R.id.im_to)
        ImageView imageTo;

        ActionViewHolder(View itemView) {
            super(itemView);
        }
        String address ;
        @Override
        public void onBindViewHolder(Wallet wallet) {
            address = wallet.address;
            mContent.setText(address);
            imageTo.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            viewModel.openWalletDetail(ManagerAccountsActivity.this,address);
        }
    }
}
