package com.alphawizard.hdwallet.alphahdwallet.db.Repositor;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;


import java.math.BigInteger;

import io.reactivex.Single;

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

	Single<Wallet>  getDefaultWallet();

	Single<Wallet> findWallet(String address);
	Single<Wallet[]> fetchWallets();
}
