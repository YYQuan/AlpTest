package com.alphawizard.hdwallet.alphahdwallet.functionModule.ManagerAccounts;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.CreateWalletEntity;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.BackupModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.ManagerAccountsViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Accounts.AccountsContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Accounts.AccountsFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.WalletDetail.WalletDetailActivity;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.web3.Web3Activity;
import com.alphawizard.hdwallet.alphahdwallet.interact.CreateWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.utils.Dp2px;
import com.alphawizard.hdwallet.alphahdwallet.utils.KeyboardUtils;
import com.alphawizard.hdwallet.alphahdwallet.utils.StatusBarUtil;
import com.alphawizard.hdwallet.common.base.Layout.PlaceHolder.EmptyLayout;
import com.alphawizard.hdwallet.common.base.widget.RecyclerView.RecyclerAdapter;
import com.alphawizard.hdwallet.common.presenter.BasePresenterActivity;
import com.alphawizard.hdwallet.common.presenter.BasePresenterToolbarActivity;
import com.alphawizard.hdwallet.common.util.Log;

import net.qiujuer.genius.ui.widget.Loading;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.alphawizard.hdwallet.alphahdwallet.functionModule.WalletDetail.WalletDetailRouter.WALLET_DETAIL_STRING;

public class ManagerAccountsActivity extends BasePresenterToolbarActivity<ManagerAccountsContract.Presenter,ManagerAccountsViewModule> implements ManagerAccountsContract.View {


    @Inject
    ManagerAccountsViewModuleFactory managerAccountsViewModuleFactory;
    ManagerAccountsViewModule viewModel;

    @Inject
    ManagerAccountsContract.Presenter mPresenter;

//    @BindView(R.id.place_holder)
//    EmptyLayout placeHolder;

    @BindView(R.id.recyclerView_accounts)
    RecyclerView recyclerView;



    @BindView(R.id.btn_create_account)
    Button  btnCreate;

    @BindView(R.id.btn_import_account)
    Button  btnImport;

    @BindView(R.id.layout_manager_wallet)
    LinearLayout mLayout;


    @OnClick(R.id.btn_create_account)
    void onClickCreate(){
        enableClick(false);
        viewModel.newWallet();
    }

    @OnClick(R.id.lay_back)
    void onClickLayBack(){
        enableClick(false);
        onBackPressed();
    }

    @OnClick(R.id.btn_import_account)
    void onClickImport(){
        enableClick(false);
        viewModel.openImport(this);
    }

//    @OnClick(R.id.iv_back)
//    void onClickBack(){
//
//        onBackPressed();
//    }


    private void enableClick(boolean isEnable){
        if(isEnable){
            isLoading = false ;
            loading.stop();
        }else{
            isLoading = true;
            loading.start();
        }
        btnCreate.setEnabled(isEnable);
        btnImport.setEnabled(isEnable);
    }

    RecyclerAdapter<Wallet> mAdapter;

    private Dialog dialog;
    String  mnenonics ;
    String defaultAddress = "";
    boolean  isLoading = false;

    @BindView(R.id.loading)
    Loading loading;


    @BindView(R.id.lay_back)
    FrameLayout mLayBack ;



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
        loading.stop();


        viewModel = ViewModelProviders.of(this, managerAccountsViewModuleFactory)
                .get(ManagerAccountsViewModule.class);
        getmPresenter().takeView(this,viewModel);

        viewModel.wallets().observe(this,this::onGetWallets);
        viewModel.createWalletEntity().observe(this,this::onCreateWalletEntity);
        viewModel.createdWallet().observe(this,this::onCreatedWallet);
        viewModel.accountsBalance().observe(this,this::onGetAccountBalance);
        viewModel.accountsName().observe(this,this::onGetAccountName);
        viewModel.defaultWallet().observe(this,this::onGetDefaultWallet);
        viewModel.newAccountDefaultWallet().observe(this,this::onNewAccountDefaultWallet);


    }

    private void onNewAccountDefaultWallet(Wallet wallet) {
        viewModel.openBackup(this,string2StringList(mnenonics));
    }






    private HashMap<String, String> mAccountsNameMap = new HashMap<>();
    private void onGetAccountName(HashMap<String, String> stringStringHashMap) {
        if(stringStringHashMap!=null&&!stringStringHashMap.equals(mAccountsBalanceMap)) {
            mAccountsNameMap = stringStringHashMap;
            mAdapter.notifyDataSetChanged();
        }
    }


    private void onGetDefaultWallet(Wallet wallet) {
        defaultAddress = wallet.address;
        mAdapter.notifyDataSetChanged();
    }


    private HashMap<String, String> mAccountsBalanceMap = new HashMap<>();
    private void onGetAccountBalance(HashMap<String, String> walletStringHashMap) {
        if(walletStringHashMap!=null) {
            mAccountsBalanceMap = walletStringHashMap;
            mAdapter.notifyDataSetChanged();
        }

    }

    private void onCreatedWallet(Wallet wallet) {

        viewModel.setNewAccountDefaultWallet(wallet);
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

    private void onCreateWalletEntity(CreateWalletEntity createWalletEntity) {
        mnenonics = createWalletEntity.getMnenonics();
    }


    private void onExportWallet(String s) {

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
        isLoading  =false;
        loading.stop();

        mPresenter.getWallets();
        mPresenter.getDefaultWallet();
        viewModel.getAccountsBalance();
        viewModel.getAccountsName();

        isOpenDetailing =false;
        enableClick(true);
    }

    private void onGetWallets(Wallet[] wallets) {
        if(wallets.length>0){
//            placeHolder.triggerOkOrEmpty(true);
        }
        mPresenter.refresh( Arrays.asList(wallets));
    }


    boolean isOpenDetailing  = false;



    @Override
    public void initWidget() {
        super.initWidget();

        ActionBar actionBar = getSupportActionBar();
        //      隐藏toolbar上的 back btn
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("");
        }

        //        透明状态栏 ， 这种方式不会引起  崩溃
        StatusBarUtil.transparencyBar(this);

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

                Log.d("NabagerAcciybtsActivity  onClickListener ");
//                mPresenter.setDefaultWallet(wallet);

                if(!isOpenDetailing) {
                    isOpenDetailing = true;
                    viewModel.openWalletDetail(ManagerAccountsActivity.this, wallet.address);
                }
            }

            @Override
            public boolean onLongClickListener(RecyclerAdapter.ViewHolder holder, Wallet wallet) {
//                viewModel.exportAccount(wallet,"123");
                return super.onLongClickListener(holder, wallet);
            }
        });



//        setPlaceHolderView(placeHolder);
//        placeHolder.bind(recyclerView);



    }






    @Override
    public RecyclerAdapter<Wallet> getRecyclerViewAdapter() {
        return mAdapter;
    }

    @Override
    public void onRecyclerChange() {
//        placeHolder.triggerOkOrEmpty(mAdapter.getItemCount()>=0);
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
//                            App.showToast("已复制 keystore");
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



    class ActionViewHolder  extends RecyclerAdapter.ViewHolder<Wallet> {

        @BindView(R.id.txt_eth_address)
        TextView mContent;

        @BindView(R.id.txt_name)
        TextView mName;


        @BindView(R.id.iv_is_default_wallet)
        ImageView imageDefault;

        @OnClick(R.id.iv_is_default_wallet)
        void onClickImageDefault(){
            viewModel.setDefaultWallet(mWallet);
        }
        @OnClick(R.id.txt_name)
        void onClickName  (){
            viewModel.setDefaultWallet(mWallet);
        }


        @BindView(R.id.txt_eth_balance)
        TextView mBalance;

        @BindView(R.id.im_to)
        ImageView imageTo;


//        @OnClick(R.id.iv_is_default_wallet)
//        void  onCLickSetDefault(){
//
//            mPresenter.setDefaultWallet(new Wallet(address));
//        }

        ActionViewHolder(View itemView) {
            super(itemView);
        }
        String address ;
        Wallet mWallet ;
        @Override
        public void onBindViewHolder(Wallet wallet) {
            mWallet = wallet;
            address = wallet.address;
            mContent.setText(address);


            String valueName = mAccountsNameMap.get(wallet.address);
            if(valueName!=null) {
                mName.setText(valueName);
            }else{
                mName.setText( "Wallet");
            }

            String valueBalance = mAccountsBalanceMap.get(wallet.address);

            if(address.equalsIgnoreCase(defaultAddress)){


                imageDefault.setImageResource(R.mipmap.ic_select_activity);
//                imageDefault.setBackgroundResource(R.mipmap.ic_select_activity);
            }else{
                imageDefault.setImageResource(R.mipmap.ic_select_unactivity);
//                imageDefault.setBackgroundResource(R.mipmap.ic_select_unactivity);
            }
            if(valueBalance!=null) {
                mBalance.setText(valueBalance + "ETH");
            }else{
                mBalance.setText( "0.000ETH");
            }
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.cancelGetAccountsBalance();
    }
}
