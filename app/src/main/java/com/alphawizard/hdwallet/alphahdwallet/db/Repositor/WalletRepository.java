package com.alphawizard.hdwallet.alphahdwallet.db.Repositor;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Transaction;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.service.AccountKeystoreService;
import com.alphawizard.hdwallet.alphahdwallet.service.EthTickerService;
import com.alphawizard.hdwallet.alphahdwallet.service.TickerService;
import com.alphawizard.hdwallet.common.util.Log;

import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WalletRepository implements WalletRepositoryType {

	private  OkHttpClient httpClient ;
	private PreferenceRepositoryType preferenceRepositoryType;
	private AccountKeystoreService accountKeystoreService;
	private TickerService tickerService;
	public WalletRepository(OkHttpClient httpClient,
							PreferenceRepositoryType preferenceRepositoryType,
							AccountKeystoreService accountKeystoreService,
							TickerService tickerService
	) {
		this.httpClient =httpClient;
		this.preferenceRepositoryType = preferenceRepositoryType;
		this.accountKeystoreService = accountKeystoreService;
		this.tickerService =  tickerService;
	}

	public Single<Wallet>  createAccount(String  mnemonics,String password){
		return accountKeystoreService.createMnemonicsAccount(mnemonics,password);
	}

	public  Single<String>  generateMnemonics(){
		return accountKeystoreService
				.generateMnemonics();
	}

	public String getTickerPrice(){

		return tickerService.fetchTickerPrice();
	}

	public Call<Transaction> getTransactions(String address){
		return tickerService.fetchTransactions(address);
	}




	//	查询账号余额
	@Override
	public Single<BigInteger> balanceInWei(Wallet wallet) {
		return Single.fromCallable(() -> Web3jFactory
					.build(new HttpService("https://rinkeby.infura.io/llyrtzQ3YhkdESt2Fzrk", httpClient, false))
					.ethGetBalance(wallet.address, DefaultBlockParameterName.LATEST)
					.send()
					.getBalance())
                .subscribeOn(Schedulers.io());
	}


//	@Override
//	public Single<byte[]> signPerson(Wallet signer,
//									 String signerPassword,
//									 byte[] data){
//		return   accountKeystoreService.signPerson(signer,signerPassword,data);
//	}


	@Override
	public Single<byte[]> signTransaction(  Wallet signer,String signerPassword,String toAddress,
											BigInteger amount,
											BigInteger gasPrice,
											BigInteger gasLimit,
											long nonce,
											byte[] data,
											long chainId){

		return accountKeystoreService.signTransaction(
				signer,
				signerPassword,
				toAddress,
				amount,
				gasPrice,
				gasLimit,
				nonce,
		 		data,
		 		chainId);
	}

	public Single<Wallet> importKeystore(String keystore, String password,String newPassword){
		return accountKeystoreService.importKeystore(keystore,password,newPassword);
	}
	public Single<Wallet> importPrivateKey(String privateKey, String password){
		return accountKeystoreService.importPrivateKey(privateKey,password);
	}

	public  void deleteWallet(Wallet wallet,String password ){
		 accountKeystoreService.deleteAccount(wallet.address ,password)
				.subscribeOn(Schedulers.io());

	}

	@Override
	public Single<Wallet> importMnenonics(String mnenonics,String password){
//		seed 的passphrase 设置为null
		return accountKeystoreService.importMnenonics(mnenonics,password);
	}

	@Override
	public Single<String> exportAccount(Wallet wallet,String password, String newPassword){
		return accountKeystoreService.exportAccount(wallet, password, newPassword);
	}

	public Single<Wallet> getDefaultWallet(){
		return Single.fromCallable(preferenceRepositoryType::getCurrentWalletAddress)
				.flatMap(this::findWallet);
	}

	public Single<String>  getDefaultWalletAddress(){
		return Single.fromCallable(preferenceRepositoryType::getCurrentWalletAddress);

	}


	@Override
	public Completable deleteWallet(String address, String password) {
		return accountKeystoreService.deleteAccount(address, password);
	}


	public Single<Wallet> findWallet(String address) {
		return fetchWallets()
				.flatMap(accounts -> {
					for (Wallet wallet : accounts) {
						if (wallet.sameAddress(address)) {
							return Single.just(wallet);
						}
					}
					return null;
				});
	}

	public Single<Wallet[]> fetchWallets() {
        return accountKeystoreService
				.fetchAccounts()
				.subscribeOn(Schedulers.io());
	}

}