package com.alphawizard.hdwallet.alphahdwallet.db.Repositor;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;


import io.reactivex.Completable;
import io.reactivex.Single;

public interface PasswordStore {
	Single<String> getPassword(Wallet wallet);
	Single<String> getMnemonics(Wallet wallet);
	Completable setPassword(Wallet wallet, String password);
	Completable setMnemonics(Wallet wallet, String mnemonics);
	Single<String> generatePassword();
}
