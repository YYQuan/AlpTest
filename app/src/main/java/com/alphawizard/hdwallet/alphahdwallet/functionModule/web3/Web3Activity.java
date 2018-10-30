package com.alphawizard.hdwallet.alphahdwallet.functionModule.web3;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.WalletsViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.Web3ViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletActivityContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.common.base.App.Activity;
import com.alphawizard.hdwallet.common.base.ViewModule.BaseViewModel;
import com.alphawizard.hdwallet.common.presenter.BaseContract;
import com.alphawizard.hdwallet.common.presenter.BasePresenterToolbarActivity;
import com.example.web3lib.BuildConfig;
import com.example.web3lib.OnSignMessageListener;
import com.example.web3lib.OnSignPersonalMessageListener;
import com.example.web3lib.OnSignTransactionListener;
import com.example.web3lib.OnSignTypedMessageListener;
import com.example.web3lib.Web3View;
import com.google.gson.Gson;

import javax.inject.Inject;

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




public class Web3Activity extends BasePresenterToolbarActivity implements View.OnClickListener
        , OnSignTransactionListener, OnSignPersonalMessageListener, OnSignTypedMessageListener, OnSignMessageListener
{
    @Inject
    Web3Contract.Presenter mPresenter;

    @Inject
    Web3ViewModuleFactory viewModuleFactory;
    Web3ViewModule viewModel;

    public  static  void show(Context context){
        context.startActivity(new Intent(context, Web3Activity.class));
    }

    private TextView url;
    private Web3View web3;
    private Call<SignMessageRequest> callSignMessage;
    private Call<SignPersonalMessageRequest> callSignPersonalMessage;
    private Call<SignTypedMessageRequest> callSignTypedMessage;
    private Call<SignTransactionRequest> callSignTransaction;


    @Override
    public int getContentLayoutID() {
        return R.layout.activity_web3;
    }

    @Override
    public BaseContract.BasePresenter initPresenter() {
        return mPresenter;
    }

    @Override
    public BaseViewModel initViewModule() {
        return viewModel;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        url = findViewById(R.id.url);
        web3 = findViewById(R.id.web3view);
        findViewById(R.id.go).setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();

        viewModel = ViewModelProviders.of(this, viewModuleFactory)
                .get(Web3ViewModule.class);

        setupWeb3();
    }

    private void setupWeb3() {
        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
        web3.setChainId(1);
        web3.setRpcUrl("https://mainnet.infura.io/llyrtzQ3YhkdESt2Fzrk");
//        web3.setWalletAddress(new Address("0x242776e7ca6271e416e737adffcfeb22e8dc1b3c"));
        String address  = viewModel.getDefaultWalletAddress();
        web3.setWalletAddress(new Address(address));

        web3.setOnSignMessageListener(message ->
                callSignMessage = Trust.signMessage().message(message).call(this));
        web3.setOnSignPersonalMessageListener(message ->
                callSignPersonalMessage = Trust.signPersonalMessage().message(message).call(this));
      web3.setOnSignTransactionListener(transaction ->
                callSignTransaction = Trust.signTransaction().transaction(transaction).call(this));
//        web3.setOnSignTransactionListener(this::onSignTransaction);

        web3.setOnSignTypedMessageListener(message ->
                callSignTypedMessage = Trust.signTypedMessage().message(message).call(this));
    }

    @Override
    public void onSignMessage(Message<String> message) {
        Toast.makeText(this, message.value, Toast.LENGTH_LONG).show();
        web3.onSignCancel(message);
    }

    @Override
    public void onSignPersonalMessage(Message<String> message) {
        Toast.makeText(this, message.value, Toast.LENGTH_LONG).show();
        web3.onSignCancel(message);
    }

    @Override
    public void onSignTypedMessage(Message<TypedData[]> message) {
        Toast.makeText(this, new Gson().toJson(message), Toast.LENGTH_LONG).show();
        web3.onSignCancel(message);
    }

    @Override
    public void onSignTransaction(Transaction transaction) {
        String str = new StringBuilder()
                .append(transaction.recipient == null ? "" : transaction.recipient.toString()).append(" : ")
                .append(transaction.contract == null ? "" : transaction.contract.toString()).append(" : ")
                .append(transaction.value.toString()).append(" : ")
                .append(transaction.gasPrice.toString()).append(" : ")
                .append(transaction.gasLimit).append(" : ")
                .append(transaction.nonce).append(" : ")
                .append(transaction.payload).append(" : ")
                .toString();
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();

//        viewModel.sendTransaction(transaction.recipient.toString(),transaction.value,transaction.gasPrice,transaction.gasLimit,transaction.nonce);
        web3.onSignCancel(transaction);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callSignTransaction != null) {
            callSignTransaction.onActivityResult(requestCode, resultCode, data, response -> {
                Transaction transaction = response.request.body();
                if (response.isSuccess()) {
                    web3.onSignTransactionSuccessful(transaction, response.result);
                } else {
                    if (response.error == Trust.ErrorCode.CANCELED) {
                        web3.onSignCancel(transaction);
                    } else {
                        web3.onSignError(transaction, "Some error");
                    }
                }
            });
        }

        if (callSignMessage != null) {
            callSignMessage.onActivityResult(requestCode, resultCode, data, response -> {
                Message message = response.request.body();
                if (response.isSuccess()) {
                    web3.onSignMessageSuccessful(message, response.result);
                } else {
                    if (response.error == Trust.ErrorCode.CANCELED) {
                        web3.onSignCancel(message);
                    } else {
                        web3.onSignError(message, "Some error");
                    }
                }
            });
        }

        if (callSignPersonalMessage != null) {
            callSignPersonalMessage.onActivityResult(requestCode, resultCode, data, response -> {
                Message message = response.request.body();
                if (response.isSuccess()) {
                    web3.onSignMessageSuccessful(message, response.result);
                } else {
                    if (response.error == Trust.ErrorCode.CANCELED) {
                        web3.onSignCancel(message);
                    } else {
                        web3.onSignError(message, "Some error");
                    }
                }
            });
        }

        if (callSignTypedMessage != null) {
            callSignTypedMessage.onActivityResult(requestCode, resultCode, data, response -> {
                Message message = response.request.body();
                if (response.isSuccess()) {
                    web3.onSignMessageSuccessful(message, response.result);
                } else {
                    if (response.error == Trust.ErrorCode.CANCELED) {
                        web3.onSignCancel(message);
                    } else {
                        web3.onSignError(message, "Some error");
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        web3.loadUrl(url.getText().toString());
        web3.requestFocus();
    }


}
