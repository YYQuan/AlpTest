package com.alphawizard.hdwallet.alphahdwallet.db.Repositor;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Transaction;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;


import java.math.BigInteger;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import jnr.ffi.StructLayout;
import retrofit2.Call;
import retrofit2.Response;

public interface WalletRepositoryType {


	Single<Wallet> createAccount(String mnenonics,String password) ;
	Single<String>  generateMnemonics();
	Single<BigInteger> balanceInWei(Wallet wallet);
	Single<byte[]> signTransaction(Wallet signer,String signerPassword,String toAddress,
								   BigInteger amount,
								   BigInteger gasPrice,
								   BigInteger gasLimit,
								   long nonce,
								   byte[] data,
								   long chainId);
	Single<byte[]> signPerson(Wallet signer,
								   String signerPassword,
								   byte[] data);
	Single<String> exportAccount(Wallet wallet,String password, String newPassword);
	Single<Wallet> importKeystore(String keystore, String password,String newPassword);
	Single<Wallet> importPrivateKey(String privateKey, String password);
	Single<Wallet> importMnenonics(String mnenonics,String password);
	String getTickerPrice();
	Call<Transaction> getTransactions(String address);


	Single<Wallet>  getDefaultWallet();
	Single<String>  getDefaultWalletAddress();


	Completable deleteWallet(String address, String password);

	Single<Wallet> findWallet(String address);
	Single<Wallet[]> fetchWallets();
}
