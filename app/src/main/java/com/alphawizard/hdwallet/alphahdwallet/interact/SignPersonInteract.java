package com.alphawizard.hdwallet.alphahdwallet.interact;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.CreateWalletEntity;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PasswordStore;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepositoryType;
import com.alphawizard.hdwallet.alphahdwallet.utils.rx.Operators;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.alphawizard.hdwallet.alphahdwallet.utils.rx.Operators.completableErrorProxy;


public class SignPersonInteract {

	WalletRepositoryType walletRepository;
	PasswordStore passwordStore;

	public SignPersonInteract(WalletRepositoryType walletRepository, PasswordStore passwordStore) {
		this.walletRepository =  walletRepository;
		this.passwordStore = passwordStore;
	}

//	public   Single<byte[]>   signPerson(Wallet wallet ,byte[] data){
//		return passwordStore.getPassword(wallet)
//				.flatMap(password -> walletRepository.signPerson(wallet,password,data))
//				.subscribeOn(Schedulers.io());
//	}

}
