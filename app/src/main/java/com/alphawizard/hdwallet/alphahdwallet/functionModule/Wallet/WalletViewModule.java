package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Transaction;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepositoryType;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ManagerAccounts.ManagerAccountsRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.fristLaunch.FirstLaunchRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.send.SendRouter;
import com.alphawizard.hdwallet.alphahdwallet.interact.CreateWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.ExportWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.FetchWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.FindDefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.GetBalanceInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.SendTransactionInteract;
import com.alphawizard.hdwallet.alphahdwallet.service.EthTickerService;
import com.alphawizard.hdwallet.common.base.ViewModule.BaseViewModel;
import com.alphawizard.hdwallet.common.base.ViewModule.entity.C;
import com.alphawizard.hdwallet.common.base.ViewModule.entity.ErrorEnvelope;
import com.alphawizard.hdwallet.common.util.Log;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WalletViewModule extends BaseViewModel {

    private static final long GET_BALANCE_INTERVAL = 10;

    CreateWalletInteract mCreateWalletInteract;
    DefaultWalletInteract mDefaultWalletInteract;
    FindDefaultWalletInteract mFindDefaultWalletInteract;
    WalletRepositoryType  mWalletRepositoryType;
    GetBalanceInteract mGetBalanceInteract;
    SendTransactionInteract mSendTransactionInteract;
    SendRouter  mSendRouter;
    FetchWalletInteract mFetchWalletInteract;
    FirstLaunchRouter  mFirstLaunchRouter;
    ManagerAccountsRouter mManagerRouter;
    ExportWalletInteract mExportWalletInteract;
    private final MutableLiveData<Wallet[]> wallets = new MutableLiveData<>();
    private final MutableLiveData<Wallet> defaultWallet = new MutableLiveData<>();
    private final MutableLiveData<Wallet> createdWallet = new MutableLiveData<>();
    private final MutableLiveData<ErrorEnvelope> createWalletError = new MutableLiveData<>();
    private final MutableLiveData<String> exportedStore = new MutableLiveData<>();
    private final MutableLiveData<String>  ethValue = new MutableLiveData<>();
    private final MutableLiveData<ErrorEnvelope> exportWalletError = new MutableLiveData<>();

    private final MutableLiveData<String> transactionHash = new MutableLiveData<>();

    private final MutableLiveData<List<Transaction.TransactionBean>> transactionBeans = new MutableLiveData<>();



    private final MutableLiveData<String> defaultWalletBalance = new MutableLiveData<>();

    public WalletViewModule(CreateWalletInteract createWalletInteract,
                            DefaultWalletInteract defaultWalletInteract,
                            FindDefaultWalletInteract findDefaultWalletInteract,
                            FetchWalletInteract fetchWalletInteract,
                            GetBalanceInteract  getBalanceInteract,
                            ExportWalletInteract exportWalletInteract,
                            SendTransactionInteract sendTransactionInteract,
                            FirstLaunchRouter  firstLaunchRouter,
                            SendRouter  sendRouter,
                            ManagerAccountsRouter managerRouter,
                            WalletRepositoryType walletRepositoryType
                                )
    {
        mCreateWalletInteract = createWalletInteract;
        mDefaultWalletInteract = defaultWalletInteract;
        mFindDefaultWalletInteract = findDefaultWalletInteract;
        mFetchWalletInteract = fetchWalletInteract;
        mExportWalletInteract =exportWalletInteract;
        mSendTransactionInteract = sendTransactionInteract;
        mWalletRepositoryType  =  walletRepositoryType;
        mFirstLaunchRouter = firstLaunchRouter;
        mGetBalanceInteract =getBalanceInteract;
        mSendRouter = sendRouter;
        mManagerRouter = managerRouter ;
    }

    public LiveData<Wallet[]> wallets() {
        return wallets;
    }

    public LiveData<Wallet> defaultWallet() {
        return defaultWallet;
    }

    public LiveData<Wallet> createdWallet() {
        return createdWallet;
    }

    public LiveData<String> defaultWalletBalance() {
        return defaultWalletBalance;
    }

    public LiveData<String> ethValue() {
        return ethValue;
    }

    public LiveData<String> exportedStore() {
        return exportedStore;
    }

    public LiveData<String> transactionHash() {
        return transactionHash;
    }

    public LiveData<List<Transaction.TransactionBean>> transactionBeans() {
        return transactionBeans;
    }



    public void getAccounts(){
        progress.setValue(true);
        mFetchWalletInteract
                .fetchAccounts()
                .subscribe(accounts->{
                    wallets.postValue(accounts);
                },this::onGetAccountsError);
    }



    public void setDefaultWallet(Wallet wallet) {
        disposable = mDefaultWalletInteract
                .setDefaultWallet(wallet)
                .subscribe(() -> onDefaultWalletChanged(wallet), this::onError);
    }

    public void getDefaultWallet(){
        disposable = mFindDefaultWalletInteract
                .find()
                .subscribe(wallet -> onDefaultWalletChanged(wallet), this::onGetDefaultAccountsError);
    }


    public String  getDefaultWalletAddress(){
        return mDefaultWalletInteract.getDefaultWalletAddress().blockingGet();
    }

    private void onDefaultWalletChanged(Wallet wallet) {
        progress.postValue(false);
        defaultWallet.postValue(wallet);

//        defaultWallet 变化 要立即改变balance 和transaction  record
        if(call!=null) {
//            getBalance();
            Single.just(mGetBalanceInteract
                    .getBalance(defaultWallet.getValue())
                    .subscribe(defaultWalletBalance::postValue,  this::onGetDefaultBalanceError))
                    .subscribe();
            call = apiClient.getTransaction("account", "txlist", mWalletRepositoryType.getDefaultWalletAddress().blockingGet(), "desc");
            call.enqueue(callback);
        }
    }

    public void getBalance() {

        disposable = Observable.interval(0, GET_BALANCE_INTERVAL, TimeUnit.SECONDS)
                .doOnNext(l -> mGetBalanceInteract
                        .getBalance(defaultWallet.getValue())
                        .subscribe(defaultWalletBalance::postValue,  this::onGetDefaultBalanceError))
                .doOnNext(l-> Single.just(mWalletRepositoryType
                        .getTickerPrice())
                        .observeOn(Schedulers.io())
                        .subscribe(this::getTickerPriceSuccess,this::getTickerPriceError))
//                .doOnNext(l-> Single.just(mWalletRepositoryType
//                        .getTransactions(mWalletRepositoryType.getDefaultWalletAddress().blockingGet()))
//                        .observeOn(Schedulers.io())
//                        .subscribe(this::getTransactionsSuccess,this::getTransactionsError))
                .subscribe();
    }

    public void fetchTransactions() {

    }

    public void exportAccount(Wallet wallet,String password){
        mExportWalletInteract.exportKeystore(wallet,password)
                .subscribe(this::exportAccountSuccess,this::exportAccountError);
//        mWalletRepositoryType
//                .exportAccount(wallet,password)
//                .subscribe(this::exportAccountSuccess,this::exportAccountError);
    }

    private void exportAccountSuccess(String s) {
        exportedStore.postValue(s);
    }


    retrofit2.Call<Transaction> call ;
    EthTickerService.ApiClient apiClient;
    Retrofit retrofit = new Retrofit.Builder()
            //使用自定义的mGsonConverterFactory
//                            .addConverterFactory(GsonConverterFactory.create(buildGson()))
            .addConverterFactory(GsonConverterFactory.create())
//                            .baseUrl("http://apis.baidu.com/txapi/")
            .baseUrl("https://rinkeby.etherscan.io/")
            .build();
    Call  callback = new Call(transactionBeans);
    private void getTickerPriceSuccess(String stringResponse) {

        if(apiClient==null) {
            apiClient = retrofit.create(EthTickerService.ApiClient.class);
        }
        if (call == null) {
            call = apiClient.getTransaction("account", "txlist", mWalletRepositoryType.getDefaultWalletAddress().blockingGet(),"desc");
            call.enqueue(callback);
        }else{

            if(call.isExecuted()) {
//                call = apiClient.getTransaction("account", "txlist", mWalletRepositoryType.getDefaultWalletAddress().blockingGet(),"desc");
//                call.enqueue(callback);
                call.clone().enqueue(callback);

//                call = apiClient.getTransaction("account", "txlist", mWalletRepositoryType.getDefaultWalletAddress().blockingGet(),"desc");
//                call.enqueue(callback);
            }else{

            }
        }


        if(stringResponse.indexOf("<span id='price'>")>=0) {

            stringResponse = stringResponse.substring(stringResponse.indexOf("<span id='price'>"));
            stringResponse = stringResponse.substring(17, stringResponse.indexOf("@"));
            ethValue.postValue(stringResponse);
        }
    }

    static class Call implements retrofit2.Callback<Transaction>{

        MutableLiveData<List<Transaction.TransactionBean>> transactionBeans;

        public Call(MutableLiveData<List<Transaction.TransactionBean>> transactionBeans) {
            this.transactionBeans = transactionBeans;
        }

        @Override
        public void onResponse(retrofit2.Call<Transaction> call, Response<Transaction> response) {

              Transaction body = response.body();
              transactionBeans.postValue(body.result);
            Log.d("body ");
        }

        @Override
        public void onFailure(retrofit2.Call<Transaction> call, Throwable t) {

        }
    }


    public void  sendTransaction(String toAddress, BigInteger amount , BigInteger gasPrice, BigInteger gasLimit, long nonce, String dataString, long chainId){


        mSendTransactionInteract
                .sendTransaction(toAddress,amount,gasPrice,gasLimit,nonce,dataString,chainId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::sendSuccess,this::sendError);
    }


    private void sendError(Throwable throwable) {
        App.showToast("send fail");
    }

    private void sendSuccess(String s) {
        transactionHash.postValue(s);
        App.showToast("send success");
    }

    private void exportAccountError(Throwable throwable) {
        exportWalletError.postValue(new ErrorEnvelope(C.ErrorCode.UNKNOWN, null));
    }

    private void getTickerPriceError(Throwable throwable) {
        exportWalletError.postValue(new ErrorEnvelope(C.ErrorCode.UNKNOWN, null));
    }

    public void openSendEth(Context context){
        mSendRouter.open(context);
    }

    public void openFirstLaunch(Context context){
        mFirstLaunchRouter.open(context);
    }

    public void openManagerRouter(Context context){
        mManagerRouter.open(context);
    }



    private void getTransactionsError(Throwable throwable) {
        exportWalletError.postValue(new ErrorEnvelope(C.ErrorCode.UNKNOWN, null));
    }

    private void onGetDefaultBalanceError(Throwable throwable) {
        exportWalletError.postValue(new ErrorEnvelope(C.ErrorCode.UNKNOWN, null));
    }

    private void onExportError(Throwable throwable) {
        exportWalletError.postValue(new ErrorEnvelope(C.ErrorCode.UNKNOWN, null));
    }

    private void onCreateWalletError(Throwable throwable) {
        createWalletError.postValue(new ErrorEnvelope(C.ErrorCode.UNKNOWN, null));
    }

    private void onGetAccountsError(Throwable throwable) {
        createWalletError.postValue(new ErrorEnvelope(C.ErrorCode.UNKNOWN, null));
    }

    private void onGetDefaultAccountsError(Throwable throwable) {
        createWalletError.postValue(new ErrorEnvelope(C.ErrorCode.UNKNOWN, null));
    }
}
