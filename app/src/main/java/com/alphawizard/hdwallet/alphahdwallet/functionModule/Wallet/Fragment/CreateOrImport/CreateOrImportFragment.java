package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.CreateOrImport;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.CreateWalletEntity;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.WalletsViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.dapp.DappContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.alphahdwallet.interact.CreateWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.utils.String2StringList;
import com.alphawizard.hdwallet.common.base.Layout.PlaceHolder.EmptyLayout;
import com.alphawizard.hdwallet.common.presenter.BasePresenterFragment;
import com.alphawizard.hdwallet.common.util.Log;
import com.example.web3lib.BuildConfig;
import com.example.web3lib.OnSignMessageListener;
import com.example.web3lib.OnSignPersonalMessageListener;
import com.example.web3lib.OnSignTransactionListener;
import com.example.web3lib.OnSignTypedMessageListener;
import com.example.web3lib.Web3View;
import com.google.gson.Gson;

import net.qiujuer.genius.ui.widget.Loading;

import java.math.BigInteger;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import trust.Call;
import trust.SignMessageRequest;
import trust.SignPersonalMessageRequest;
import trust.SignTransactionRequest;
import trust.SignTypedMessageRequest;
import trust.Trust;
import trust.core.entity.Address;
import trust.core.entity.Message;
import trust.core.entity.Transaction;
import trust.core.entity.TypedData;

public class CreateOrImportFragment extends BasePresenterFragment<CreateOrImportContract.Presenter,WalletViewModule> implements CreateOrImportContract.View{
    @Inject
    CreateOrImportContract.Presenter mPresenter;

    @Inject
    WalletsViewModuleFactory viewModuleFactory;
    WalletViewModule viewModel;



    String  mnenonics ;


    @BindView(R.id.loading)
    Loading loading;

    @BindView(R.id.btn_import)
    Button mImport;

    @BindView(R.id.btn_create)
    Button mCreate;


    boolean  isCreate = false;
    @OnClick(R.id.btn_create)
    void onClickCreate(){
        loading.start();
        mImport.setEnabled(false);
        mCreate.setEnabled(false);
        isCreate =true;
        viewModel.newWallet();
    }

    @OnClick(R.id.btn_import)
    void onClickImport(){
        viewModel.openImportRouter(getActivity());
    }


    @Override
    public CreateOrImportContract.Presenter initPresenter() {
        return mPresenter;
    }

    @Override
    public WalletViewModule initViewModule() {
        return viewModel;
    }

    @Override
    public int getContentLayoutID() {
        return R.layout.fragment_no_account;
    }


//    @BindView(R.id.url)
//    TextView url;


//    @OnClick(R.id.btn_success)
//    void onClickSuccess(){
////        web3.onSignCancel(mTransaction);
//        web3.onSignTransactionSuccessful(mTransaction,null);
//    }
//    @OnClick(R.id.btn_fail)
//    void onClickFail(){
//        web3.onSignCancel(mTransaction);
////        web3.onSignTransactionSuccessful(mTransaction,null);
//    }


    @Override
    public void initWidget(View view) {
        super.initWidget(view);
    }



    @Override
    public void initData() {
        super.initData();
        mImport.setEnabled(true);
        mCreate.setEnabled(true);
        loading.stop();
        viewModel = ViewModelProviders.of(this, viewModuleFactory)
                .get(WalletViewModule.class);
        mPresenter.takeView(this,viewModel);
        viewModel.createdWallet().observe(this,this::onCreatedWallet);
        viewModel.defaultWallet().observe(this,this::defaultWallet);
        viewModel.createWalletEntity().observe(this,this::onCreateWalletEntity);
    }



    private void defaultWallet(Wallet wallet) {
        if(isCreate) {
            viewModel.openBackup(getActivity(),String2StringList.string2StringList(mnenonics));
        }
        if(wallet!=null&&createAddress.equalsIgnoreCase(wallet.address)) {
            isCreate = false;
        }

    }

    String createAddress = "";

//    回到createOrImportFragment的时候   在没有粗发 click 的情况下，会莫名其妙的调用到这里 因此加多一个  isCreate的 判定
    private void onCreatedWallet(Wallet wallet) {
        createAddress = wallet.address;
        viewModel.setDefaultWallet(wallet);
        Log.d("onCreatedWallet");
//        showBackupMnenonicsDialog(mnenonics);



    }

    private void onCreateWalletEntity(CreateWalletEntity createWalletEntity) {
        Log.d("getMnenonics  :"+createWalletEntity.getMnenonics());
        mnenonics = createWalletEntity.getMnenonics();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }



}
