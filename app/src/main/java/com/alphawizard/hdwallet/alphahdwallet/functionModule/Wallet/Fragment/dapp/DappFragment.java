package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.dapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.Property;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Launch.LaunchActivity;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.WalletsViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Accounts.AccountsContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletActivity;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.alphahdwallet.utils.BalanceUtils;
import com.alphawizard.hdwallet.common.base.App.Activity;
import com.alphawizard.hdwallet.common.presenter.BasePresenterFragment;
import com.alphawizard.hdwallet.common.util.Log;
import com.example.web3lib.BuildConfig;
import com.example.web3lib.OnSignMessageListener;
import com.example.web3lib.OnSignPersonalMessageListener;
import com.example.web3lib.OnSignTransactionListener;
import com.example.web3lib.OnSignTypedMessageListener;
import com.example.web3lib.Web3View;
import com.google.gson.Gson;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.genius.ui.compat.UiCompat;

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

public class DappFragment extends BasePresenterFragment<DappContract.Presenter,WalletViewModule> implements DappContract.View, OnSignTransactionListener, OnSignPersonalMessageListener, OnSignTypedMessageListener, OnSignMessageListener {

    public  final static  String   MY_URL  ="http://47.91.247.93:8000/my";
    public  final static  String   DICE_URL  ="http://47.91.247.93:8000/dice";
//    public  final static  String   MY_URL  ="http://192.168.120.187:8080/my";
//    public  final static  String   DICE_URL  ="http://192.168.120.187:8080/dice";

    @Inject
    DappContract.Presenter mPresenter;

    @Inject
    WalletsViewModuleFactory viewModuleFactory;
    WalletViewModule viewModel;



    private Call<SignMessageRequest> callSignMessage;
    private Call<SignPersonalMessageRequest> callSignPersonalMessage;
    private Call<SignTypedMessageRequest> callSignTypedMessage;
    private Call<SignTransactionRequest> callSignTransaction;


    String  hexSign =  "";

    Transaction mTransaction;

    @Override
    public DappContract.Presenter initPresenter() {
        return mPresenter;
    }

    @Override
    public WalletViewModule initViewModule() {
        return viewModel;
    }

    @Override
    public int getContentLayoutID() {
        return R.layout.fragment_wallet_dapp;
    }


//    @BindView(R.id.url)
//    TextView url;

    @BindView(R.id.web3view)
    Web3View web3;


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
//        url = view.findViewById(R.id.url);
        web3 = view.findViewById(R.id.web3view);
//        view.findViewById(R.id.go).setOnClickListener(this);



        web3.setBackgroundColor(ContextCompat.getColor(getActivity(),android.R.color.transparent));
        web3.setBackgroundResource(R.color.colorPrimaryBackgroud);


    }



    @Override
    public void initData() {
        super.initData();

        viewModel = ViewModelProviders.of(this, viewModuleFactory)
                .get(WalletViewModule.class);
        viewModel.transactionHash().observe(this,this::onTransactionChange);
        viewModel.defaultWallet().observe(this,this::defaultWalletChange);
        viewModel.defaultWalletBalance().observe(this,this::getBalance);
        viewModel.signPerson().observe(this,this::signPersonalSuccess);
        viewModel.getDefaultWallet();

//        if(defaultWalletAddress== null&&!web3.isShown()){
//            setupWeb3();
////            web3.loadUrl("http://192.168.150.84:8080/");
//            web3.loadUrl(" http://47.91.247.93:8000");
//            web3.requestFocus();
//        }

    }

    private void signPersonalSuccess(byte[] bytes) {
        String callback = String.format("onSignSuccessful(%1$s, \"%2$s\")", "8888", bytes);
        web3.post(() -> web3.evaluateJavascript(callback, value -> android.util.Log.d("WEB_VIEW", value)));
    }

    @Override
    public void initFirst() {

        super.initFirst();
        if(defaultWalletAddress== null&&!web3.isShown()){
            if(WalletActivity.isTransaction){
                if(WalletActivity.transactionResult){

                }else{

                }

            }else{
                setupWeb3();
                web3.loadUrl(DICE_URL);
                web3.requestFocus();
            }

        }
    }

    float balance  = 0f;
    String  balanceString = "0.0";
    private void getBalance(String s) {
        balanceString = s;
        balance = Float.parseFloat(s);
    }

    String defaultWalletAddress;
    private void defaultWalletChange(Wallet wallet) {

        viewModel.getBalance();
        if(! wallet.address.equalsIgnoreCase(defaultWalletAddress)){
            defaultWalletAddress = wallet.address;
            setupWeb3();
            if(DICE_URL.equalsIgnoreCase(web3.getUrl())) {
                web3.loadUrl(DICE_URL);
            }else{
                web3.loadUrl(MY_URL);
            }
            web3.requestFocus();
        }
    }


    private void onTransactionChange(String s) {
        hexSign  = s ;
    }

    private void setupWeb3() {

        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
        web3.setChainId(4);
        web3.setRpcUrl("https://mainnet.infura.io/llyrtzQ3YhkdESt2Fzrk");

        if(defaultWalletAddress!=null) {
            web3.setWalletAddress(new Address(defaultWalletAddress));

        }
        web3.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Log.d("  webView 下载");
            }
        });

        web3.setOnSignMessageListener(message ->
                callSignMessage = Trust.signMessage().message(message).call(getActivity()));
        web3.setOnSignPersonalMessageListener(new OnSignPersonalMessageListener() {
            @Override
            public void onSignPersonalMessage(Message<String> message) {
                Log.d("onSignPersonalMessage");

                String value ="";
                byte[]  messageByte = new byte[32];
                if(message.value.length()==32) {
                    value = message.value.substring(2,32);
                    messageByte  = value.getBytes();
                    App.showToast("Sign  Message value  length  is  32");
                }else{
                    App.showToast("Sign  Message value  length  is  "+ message.value.length());
                    for(int i = 0 ; i<32 ;i++){
                        messageByte[i] = 0x1;
                    }
                }


//                viewModel.signPersonDefaultWallet(messageByte);
//                String callback = String.format("onSignSuccessful(%1$s, \"%2$s\")", "8888", message.value);
//                web3.post(() -> web3.evaluateJavascript(callback, value -> android.util.Log.d("WEB_VIEW", value)));
//                callSignPersonalMessage = Trust.signPersonalMessage().message(message).call(DappFragment.this.getActivity());
            }
        });
        web3.setOnSignTransactionListener(transaction ->
                callSignTransaction = Trust.signTransaction().transaction(transaction).call(getActivity()));
        web3.setOnSignTransactionListener(transaction -> {
                    onSignTransaction(transaction);
                    callSignTransaction = Trust.signTransaction().transaction(transaction).call(getActivity());
                }
            );
        web3.setOnSignTransactionListener(this::onSignTransaction);

        web3.setOnSignTypedMessageListener(message ->
                callSignTypedMessage = Trust.signTypedMessage().message(message).call(getActivity()));
        web3.setOnLanguageChangeListener(params ->
                    {
                        if(params.equalsIgnoreCase("zh-CN")){

                        }else{

                        }
                        viewModel.setCurrentLanguage(params);
                        getActivity().finish();
                        viewModel.openWallet((Activity) getActivity());
//                        viewModel.openManagerRouter(getActivity());
                    }
                );
        web3.setOnOpenInfoChangeListener(params -> {

            Run.onBackground(new Action() {
                @Override
                public void call() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Run.onUiAsync(()->{
//                        onBackPressed();
//                        setupWeb3();
//                        web3.loadUrl(DICE_URL);
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(params);
                        intent.setData(content_url);
                        startActivity(intent);
                    });

                    Log.d("白皮书");
                }
            });
        });
    }



    @Override
    public void onSignMessage(Message<String> message) {
        Toast.makeText(getActivity(), message.value, Toast.LENGTH_LONG).show();
        web3.onSignCancel(message);
    }

    @Override
    public void onSignPersonalMessage(Message<String> message) {
        Toast.makeText(getActivity(), message.value, Toast.LENGTH_LONG).show();
        web3.onSignCancel(message);
    }

    @Override
    public void onSignTypedMessage(Message<TypedData[]> message) {
        Toast.makeText(getActivity(), new Gson().toJson(message), Toast.LENGTH_LONG).show();
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

        mTransaction = transaction;

        String  value = "";
        try {
            value = BalanceUtils.weiToEth(transaction.value,5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String  amount =  null;
        try {
            amount =  BalanceUtils.weiToEth(transaction.value,6);


        } catch (Exception e) {
            e.printStackTrace();
        }
        long   priceLong  = (long) ( transaction.gasPrice.longValue()/(10e8f));

        viewModel.openSendEthHasInfo(getActivity(),defaultWalletAddress,transaction.recipient.toString(),amount,balanceString,priceLong,transaction.gasLimit,transaction.payload);


    }

    public void  onSignCancel(){
        web3.onSignCancel(mTransaction);
        mTransaction= null;
//        web3.onSignTransactionSuccessful(mTransaction,null);
    }


    public void  onSignSuccessful(){
//        web3.onSignCancel(mTransaction);
        web3.onSignTransactionSuccessful(mTransaction,null);
        mTransaction= null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(WalletActivity.isTransaction){
            if(WalletActivity.transactionResult){
                onSignSuccessful();
            }else{
                onSignCancel();
            }

        }else{

        }
        WalletActivity.isTransaction = false;
        WalletActivity.transactionResult = false;
        viewModel.getBalance();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== WalletRouter.RESULT_CODE_FOR_TRANSACTION) {
            boolean result = data.getBooleanExtra(WalletRouter.RESULT_FOR_TRANSACTION,false)  ;
            if(result){
                web3.onSignTransactionSuccessful(mTransaction,null);
            }else{
                web3.onSignCancel(mTransaction);
            }
        }
    }

    public void jump2Mine(){
        if(web3!=null){
            if(!MY_URL.equalsIgnoreCase(web3.getUrl())) {
                setupWeb3();
                web3.loadUrl(MY_URL);
            }
        }
    }

    public void jump2WebMain(){
        if(web3!=null){
            if(!DICE_URL.equalsIgnoreCase(web3.getUrl())) {
                setupWeb3();
                web3.loadUrl(DICE_URL);
            }
        }
    }


    @Override
    public boolean onBackPressed() {
        if(!web3.getUrl().equalsIgnoreCase( DICE_URL)){
            setupWeb3();
            web3.loadUrl(DICE_URL);
            return  true;
        }
        return super.onBackPressed();
    }
}
