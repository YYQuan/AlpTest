package com.alphawizard.hdwallet.alphahdwallet.db.Repositor;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;


import java.math.BigInteger;

import io.reactivex.Observable;
import io.reactivex.Single;
import jnr.ffi.StructLayout;
import retrofit2.Response;

public interface WalletRepositoryType {


	Single createAccount() ;
	Single<BigInteger> balanceInWei(Wallet wallet);
	Single<byte[]> signTransaction(Wallet signer,String signerPassword,String toAddress,
								   BigInteger amount,
								   BigInteger gasPrice,
								   BigInteger gasLimit,
								   long nonce,
								   byte[] data,
								   long chainId);

	Single<Wallet> importAccount(String keystore, String password);

	String getTickerPrice();

	Single<Wallet>  getDefaultWallet();

	Single<Wallet> findWallet(String address);
	Single<Wallet[]> fetchWallets();
}
