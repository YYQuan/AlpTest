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
import com.alphawizard.hdwallet.common.presenter.BasePresenterFragment;
import com.alphawizard.hdwallet.common.util.Log;
import com.example.web3lib.BuildConfig;
import com.example.web3lib.OnSignMessageListener;
import com.example.web3lib.OnSignPersonalMessageListener;
import com.example.web3lib.OnSignTransactionListener;
import com.example.web3lib.OnSignTypedMessageListener;
import com.example.web3lib.Web3View;
import com.google.gson.Gson;

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
    @OnClick(R.id.btn_create)
    void onClickCreate(){
        viewModel.newWallet("Wallet");
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

        viewModel = ViewModelProviders.of(this, viewModuleFactory)
                .get(WalletViewModule.class);
        mPresenter.takeView(this,viewModel);
        viewModel.createdWallet().observe(this,this::onCreatedWallet);
        viewModel.createWalletEntity().observe(this,this::onCreateWalletEntity);
    }

    private void onCreatedWallet(Wallet wallet) {
        Log.d("onCreatedWallet");
//        showBackupMnenonicsDialog(mnenonics);

        viewModel.openBackup(getActivity(), String2StringList.string2StringList(mnenonics));

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
