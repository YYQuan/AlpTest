package com.alphawizard.hdwallet.alphahdwallet.interact;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PreferenceRepositoryType;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepositoryType;
import com.alphawizard.hdwallet.alphahdwallet.di.ActivityScoped;
import com.alphawizard.hdwallet.alphahdwallet.service.AccountKeystoreService;
import com.alphawizard.hdwallet.alphahdwallet.service.GethKeystoreAccountService;
import com.alphawizard.hdwallet.common.util.Log;

import java.security.SecureRandom;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;


public class CreateWalletInteract {

	WalletRepositoryType walletRepository;

	public CreateWalletInteract(WalletRepositoryType walletRepository) {
		this.walletRepository =  walletRepository;
	}

	public Single<Wallet> create(CreateWalletEntity entiry) {
		return walletRepository.createAccount(entiry.mnenonics,entiry.password);
	}

	public Single<CreateWalletEntity>  generateMnenonics(String password){
		return walletRepository.generateMnemonics()
				.flatMap(s-> Single.just(new CreateWalletEntity(s,password)));
	}

	public Single<String>  generatePassword(){
		return walletRepository.generatePassword();
	}


	public static class  CreateWalletEntity {
		String   password ;
		String  mnenonics ;


		public CreateWalletEntity(String mnenonics , String  password) {
			this.password = password;
			this.mnenonics = mnenonics;
		}

		public String getPassword() {

			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getMnenonics() {
			return mnenonics;
		}

		public void setMnenonics(String mnenonics) {
			this.mnenonics = mnenonics;
		}
	}
}
