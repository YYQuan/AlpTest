package com.alphawizard.hdwallet.alphahdwallet.db.Repositor;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;


import io.reactivex.Completable;
import io.reactivex.Single;

public interface PasswordStore {
	Single<String> getPassword(Wallet wallet);
	Single<String> getMnemonics(Wallet wallet);
	Completable setPassword(Wallet wallet, String password);
	Completable setMnemonics(Wallet wallet, String mnemonics);
	Completable setWalletName(Wallet wallet, String name);
	Single<String> generatePassword();
	Single<String> generateWalletName();
	Single<String> getPrivateKey(Wallet   wallet );
	Single<String> getWalletName(Wallet wallet);


	Completable deleteWalletName();
	Completable deleteWalletPassword();
	Completable	deleteWalletMnemonics();
}
