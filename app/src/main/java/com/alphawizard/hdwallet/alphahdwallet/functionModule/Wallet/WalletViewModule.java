package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.CreateWalletEntity;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Transaction;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PasswordStore;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepositoryType;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ConfirmSend.ConfirmSendActivity;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ConfirmSend.ConfirmSendRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.CreateOrImport.CreateOrImportRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ManagerAccounts.ManagerAccountsRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.receiver.ReceiverRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.send.SendRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.web3.Web3Router;
import com.alphawizard.hdwallet.alphahdwallet.interact.CreateWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.ExportWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.FetchWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.FindDefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.GetBalanceInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.LanguageInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.SendTransactionInteract;
import com.alphawizard.hdwallet.alphahdwallet.service.EthTickerService;
import com.alphawizard.hdwallet.alphahdwallet.utils.rx.Operators;
import com.alphawizard.hdwallet.common.base.App.Activity;
import com.alphawizard.hdwallet.common.base.ViewModule.BaseViewModel;
import com.alphawizard.hdwallet.common.base.ViewModule.entity.C;
import com.alphawizard.hdwallet.common.base.ViewModule.entity.ErrorEnvelope;
import com.alphawizard.hdwallet.common.util.Log;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WalletViewModule extends BaseViewModel {

    private static final long GET_BALANCE_INTERVAL = 5;

    CreateWalletInteract mCreateWalletInteract;
    DefaultWalletInteract mDefaultWalletInteract;
    FindDefaultWalletInteract mFindDefaultWalletInteract;
    WalletRepositoryType  mWalletRepositoryType;
    GetBalanceInteract mGetBalanceInteract;
    SendTransactionInteract mSendTransactionInteract;
    LanguageInteract mLanguageInteract;
    SendRouter  mSendRouter;
    FetchWalletInteract mFetchWalletInteract;
    CreateOrImportRouter mCreateOrImportRouter;
    ManagerAccountsRouter mManagerRouter;
    Web3Router mWeb3Router;
    ImportRouter mImportRouter;
    BackupRouter mBackupRouter;
    WalletRouter mWalletRouter;
    ReceiverRouter mReceiverRouter;

    ExportWalletInteract mExportWalletInteract;
    PasswordStore mPasswordStore;
    ConfirmSendRouter  mConfirmSendRouter;

    //    获取全部wallet
    private final MutableLiveData<Wallet[]> wallets = new MutableLiveData<>();
//    获取默认钱包
    private final MutableLiveData<Wallet> defaultWallet = new MutableLiveData<>();
//    获取创建出来的钱包
    private final MutableLiveData<Wallet> createdWallet = new MutableLiveData<>();
//    创建钱包发生错误
    private final MutableLiveData<ErrorEnvelope> createWalletError = new MutableLiveData<>();
//    导出
    private final MutableLiveData<String> exportedStore = new MutableLiveData<>();
    private final MutableLiveData<String>  ethValue = new MutableLiveData<>();
    private final MutableLiveData<ErrorEnvelope> exportWalletError = new MutableLiveData<>();

    private final MutableLiveData<String> transactionHash = new MutableLiveData<>();
    private final MutableLiveData<Boolean> notDefaultWallet = new MutableLiveData<>();
    private final MutableLiveData<List<Transaction.TransactionBean>> transactionBeans = new MutableLiveData<>();
    private final MutableLiveData<CreateWalletEntity> createWalletEntity = new MutableLiveData<>();


    private final MutableLiveData<String> defaultWalletBalance = new MutableLiveData<>();


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


    public WalletViewModule(CreateWalletInteract createWalletInteract,
                            DefaultWalletInteract defaultWalletInteract,
                            FindDefaultWalletInteract findDefaultWalletInteract,
                            FetchWalletInteract fetchWalletInteract,
                            GetBalanceInteract  getBalanceInteract,
                            ExportWalletInteract exportWalletInteract,
                            SendTransactionInteract sendTransactionInteract,
                            LanguageInteract languageInteract,
                            CreateOrImportRouter createOrImportRouter,
                            SendRouter  sendRouter,
                            ManagerAccountsRouter managerRouter,
                            Web3Router web3Router,
                            BackupRouter backupRouter,
                            ImportRouter importRouter,
                            ReceiverRouter receiverRouter,
                            WalletRouter walletRouter,
                            ConfirmSendRouter  confirmSendRouter ,
                            WalletRepositoryType walletRepositoryType,
                            PasswordStore passwordStore
                                )
    {
        mCreateWalletInteract = createWalletInteract;
        mDefaultWalletInteract = defaultWalletInteract;
        mFindDefaultWalletInteract = findDefaultWalletInteract;
        mFetchWalletInteract = fetchWalletInteract;
        mExportWalletInteract =exportWalletInteract;
        mSendTransactionInteract = sendTransactionInteract;
        mLanguageInteract =  languageInteract;
        mWalletRepositoryType  =  walletRepositoryType;
        mCreateOrImportRouter = createOrImportRouter;
        mGetBalanceInteract =getBalanceInteract;
        mSendRouter = sendRouter;
        mWeb3Router= web3Router;
        mBackupRouter =  backupRouter;
        mImportRouter = importRouter;
        mReceiverRouter = receiverRouter;
        mWalletRouter =  walletRouter;
        mManagerRouter = managerRouter ;
        mPasswordStore =  passwordStore;
        mConfirmSendRouter =   confirmSendRouter;
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

    public LiveData<CreateWalletEntity> createWalletEntity() {
        return createWalletEntity;
    }
    public LiveData<Boolean> notDefaultWalletContent() {
        return notDefaultWallet;
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
        mFetchWalletInteract
                .fetchAccount(wallet)
                .compose(Operators.setDefaultWallet(mDefaultWalletInteract))
                .subscribe(wallet1 -> onDefaultWalletChanged(wallet1), this::setDefaultWalletError);
    }

    private void setDefaultWalletError(Throwable throwable) {
    }

    public void getDefaultWallet(){
         mFindDefaultWalletInteract
                .find()
                .subscribe(wallet -> onDefaultWalletChanged(wallet), this::onGetDefaultAccountsError);
    }



    private void onDefaultWalletChanged(Wallet wallet) {
        progress.postValue(false);
        defaultWallet.postValue(wallet);


//        defaultWallet 变化 要立即改变balance 和transaction  record
        if(call!=null) {
//            getBalance
            Single.just(mGetBalanceInteract
                    .getBalance(defaultWallet.getValue())
                    .subscribe(defaultWalletBalance::postValue,  this::onGetDefaultBalanceError));

            call = apiClient.getTransaction("account", "txlist", mWalletRepositoryType.getDefaultWalletAddress().blockingGet(), "desc");
            call.enqueue(callback);
        }
    }

    public void newWallet() {
        progress.setValue(true);

        //        CreateWalletEntity
        mCreateWalletInteract
                .generatePassword()
                .flatMap(s-> mPasswordStore.generateWalletName()
                        .flatMap(n ->mCreateWalletInteract.generateMnenonics(s,n))
                )
                .flatMap(e-> {
                    createWalletEntity.postValue(e);
                    return mCreateWalletInteract.create(e); })
                .subscribe(this::onCreateWallet,this::onCreateWalletError);
    }
    private void onCreateWallet(Wallet wallet) {
        createdWallet.postValue(wallet);
    }



    public void setCurrentLanguage(String str ){
            mLanguageInteract.setCurrentLanguage(str)
                .subscribe();
    }

    public void getBalance (){
        mGetBalanceInteract
                .getBalance(defaultWallet.getValue())
                .observeOn(Schedulers.io())
                .subscribe(balance ->
                    defaultWalletBalance.postValue(balance)
                , this::onGetDefaultBalanceError);
    }


    public void getBalanceCyclical() {


        cancel();
        disposable = Observable.interval(0, GET_BALANCE_INTERVAL, TimeUnit.SECONDS)
                .doOnNext(l ->
                        mDefaultWalletInteract
                                .getDefaultWallet()
                                .flatMap(wallet -> mGetBalanceInteract
                                        .getBalance(wallet) )
                                .observeOn(Schedulers.io())
                                .subscribe(balance -> {
                                            defaultWalletBalance.postValue(balance);
                                            getTransactionRecord();
                                        },
                                        this::onGetDefaultBalanceError))
//               获取当前ETH 市值
//                .doOnNext(l-> Single.just(mWalletRepositoryType
//                        .getTickerPrice())
//                        .observeOn(Schedulers.io())
//                        .subscribe(this::getTickerPriceSuccess,this::getTickerPriceError))
                .subscribe();

    }



    public void cancelGetBalanceCyclical(){
        cancel();
    }

    public void fetchTransactions() {

    }


    // 从etheresum中 提取出  eth当前市值  保留着先别删除
    private void getTickerPriceSuccess(String stringResponse) {


        getTransactionRecord();

        if(stringResponse.indexOf("<span id='price'>")>=0) {

            stringResponse = stringResponse.substring(stringResponse.indexOf("<span id='price'>"));
            stringResponse = stringResponse.substring(17, stringResponse.indexOf("@"));
            ethValue.postValue(stringResponse);
        }
    }

//    拿交易记录
    private  void getTransactionRecord(){
        if(apiClient==null) {
            apiClient = retrofit.create(EthTickerService.ApiClient.class);
        }
        if (call == null) {
//            String  address = mWalletRepositoryType.getDefaultWalletAddress().blockingGet();
            mWalletRepositoryType.getDefaultWalletAddress()
                    .flatMap(address ->{
                        call = apiClient.getTransaction("account", "txlist",address,"desc");
                        call.enqueue(callback);
                        return Single.just(address);
                    })
                    .subscribeOn(Schedulers.io())
                    .subscribe();
//            call = apiClient.getTransaction("account", "txlist",address,"desc");
//            call.enqueue(callback);
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
    }

    public void openSendEth(Context context,float value){
        mSendRouter.open(context,value);
    }

    public void openSendEthHasInfo(Context context,String   form , String to , String amount , String  balance, long gasPrice ,long gasLimit , String data){
//        mSendRouter.open(context,to,amount,value,transaction);

        mConfirmSendRouter.open(context,form,to,balance,amount ,gasPrice,gasLimit,data);

    }

    public void openManagerRouter(Context context){
        mManagerRouter.open(context);
    }

    public void openBackup(Context context,ArrayList<String> strings){
        mBackupRouter.open(context,strings);
    }

    public void openReceiver(Context context){
        mReceiverRouter.open(context);
    }

    public void openImportRouter(Context context){
        mImportRouter.open(context);
    }

    public void  openWallet(Activity context){
        mWalletRouter.openTransactionForResult(context,false);
    }

    // 从etheresum中 提取出eth当前市值的过程中出错    保留着先别删除
    private void getTickerPriceError(Throwable throwable) {
        exportWalletError.postValue(new ErrorEnvelope(C.ErrorCode.UNKNOWN, null));
    }


    private void onGetDefaultBalanceError(Throwable throwable) {
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
        if(throwable instanceof NoSuchElementException){
            notDefaultWallet.setValue(true);
            createdWallet.postValue(null);
        }
    }


     class Call implements retrofit2.Callback<Transaction>{

        MutableLiveData<List<Transaction.TransactionBean>> transactionBeans;

        public Call(MutableLiveData<List<Transaction.TransactionBean>> transactionBeans) {
            this.transactionBeans = transactionBeans;
        }

        @Override
        public void onResponse(retrofit2.Call<Transaction> call, Response<Transaction> response) {

            Transaction body = response.body();

            if(body!=null) {
                List<Transaction.TransactionBean>  list = body.result;
                if(list.size()<=0){
                    transactionBeans.postValue(list);
                    return ;
                }
                Transaction.TransactionBean bean =   list.get(0);
                mDefaultWalletInteract
                        .getDefaultWallet()
                        .subscribe(wallet -> {
                            if(wallet.address.equalsIgnoreCase(bean.getFrom())
                                    ||wallet.address.equalsIgnoreCase(bean.getTo())) {
                                transactionBeans.postValue(list);
                            }
                        },this::onFailure);



            }
            Log.d("wallet get current wallet transaction info  10s/次 ");
        }

         private void onFailure(Throwable throwable) {
         }

         @Override
        public void onFailure(retrofit2.Call<Transaction> call, Throwable t) {

        }
    }
}
