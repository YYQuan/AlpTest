package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepositoryType;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.fristLaunch.FirstLaunchRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.send.SendRouter;
import com.alphawizard.hdwallet.alphahdwallet.interact.CreateWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.FetchWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.FindDefaultWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.GetBalanceInteract;
import com.alphawizard.hdwallet.common.base.ViewModule.BaseViewModel;
import com.alphawizard.hdwallet.common.base.ViewModule.entity.C;
import com.alphawizard.hdwallet.common.base.ViewModule.entity.ErrorEnvelope;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class WalletViewModule extends BaseViewModel {

    private static final long GET_BALANCE_INTERVAL = 8;

    CreateWalletInteract mCreateWalletInteract;
    DefaultWalletInteract mDefaultWalletInteract;
    FindDefaultWalletInteract mFindDefaultWalletInteract;
    WalletRepositoryType  mWalletRepositoryType;
    GetBalanceInteract mGetBalanceInteract;
    SendRouter  mSendRouter;
    FetchWalletInteract mFetchWalletInteract;
    FirstLaunchRouter  mFirstLaunchRouter;
    private final MutableLiveData<Wallet[]> wallets = new MutableLiveData<>();
    private final MutableLiveData<Wallet> defaultWallet = new MutableLiveData<>();
    private final MutableLiveData<Wallet> createdWallet = new MutableLiveData<>();
    private final MutableLiveData<ErrorEnvelope> createWalletError = new MutableLiveData<>();
    private final MutableLiveData<String> exportedStore = new MutableLiveData<>();
    private final MutableLiveData<String>  ethValue = new MutableLiveData<>();
    private final MutableLiveData<ErrorEnvelope> exportWalletError = new MutableLiveData<>();

    private final MutableLiveData<String> defaultWalletBalance = new MutableLiveData<>();

    public WalletViewModule(CreateWalletInteract createWalletInteract,
                            DefaultWalletInteract defaultWalletInteract,
                            FindDefaultWalletInteract findDefaultWalletInteract,
                            FetchWalletInteract fetchWalletInteract,
                            GetBalanceInteract  getBalanceInteract,
                            FirstLaunchRouter  firstLaunchRouter,
                            SendRouter  sendRouter,
                            WalletRepositoryType walletRepositoryType
                                )
    {
        mCreateWalletInteract = createWalletInteract;
        mDefaultWalletInteract = defaultWalletInteract;
        mFindDefaultWalletInteract = findDefaultWalletInteract;
        mFetchWalletInteract = fetchWalletInteract;
        mWalletRepositoryType  =  walletRepositoryType;
        mFirstLaunchRouter = firstLaunchRouter;
        mGetBalanceInteract =getBalanceInteract;
        mSendRouter = sendRouter;
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

    public void newWallet() {
        progress.setValue(true);
        mCreateWalletInteract
                .create()
//				create 过程中没有throw 就回调success ,如果有throw  异常的话，那么就会掉error
                .subscribe(account -> {
//                    fetchWallets();
                    createdWallet.postValue(account);
                }, this::onCreateWalletError);
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

    private void onDefaultWalletChanged(Wallet wallet) {
        progress.postValue(false);
        defaultWallet.postValue(wallet);
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
                .subscribe();
    }

//    public void getTickerPrice(){
//        mWalletRepositoryType
//                .getTickerPrice()
//                .subscribe(this::getTickerPriceSuccess,this::getTickerPriceError);
//    }

    private void getTickerPriceSuccess(String stringResponse) {

        stringResponse = stringResponse.substring(stringResponse.indexOf("<span id='price'>"));
        stringResponse = stringResponse.substring(17,stringResponse.indexOf("@"));
        ethValue.postValue(stringResponse);
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
