package com.alphawizard.hdwallet.alphahdwallet.db.Repositor;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;


import io.reactivex.Completable;
import io.reactivex.Single;

public interface PasswordStore {
	Single<String> getPassword(Wallet wallet);
	Completable setPassword(Wallet wallet, String password);
	Single<String> generatePassword();
}
