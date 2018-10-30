package com.alphawizard.hdwallet.alphahdwallet.interact;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PasswordStore;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PreferenceRepositoryType;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepositoryType;
import com.alphawizard.hdwallet.alphahdwallet.di.ActivityScoped;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.fristLaunch.FirstLaunchActivity;
import com.alphawizard.hdwallet.alphahdwallet.service.AccountKeystoreService;
import com.alphawizard.hdwallet.alphahdwallet.service.GethKeystoreAccountService;
import com.alphawizard.hdwallet.alphahdwallet.utils.rx.Operators;
import com.alphawizard.hdwallet.common.util.Log;

import java.security.SecureRandom;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

import static com.alphawizard.hdwallet.alphahdwallet.utils.rx.Operators.completableErrorProxy;


public class CreateWalletInteract {

	WalletRepositoryType walletRepository;
	PasswordStore passwordStore;

	public CreateWalletInteract(WalletRepositoryType walletRepository, PasswordStore passwordStore) {
		this.walletRepository =  walletRepository;
		this.passwordStore = passwordStore;
	}


	public Single<Wallet> create(CreateWalletEntity entiry) {

		return  walletRepository.createAccount(entiry.mnenonics,entiry.password)
				.compose(Operators.savePassword(passwordStore, walletRepository, entiry.password))
				.compose(Operators.saveMnemonics(passwordStore, walletRepository, entiry.mnenonics))
				.flatMap(wallet -> passwordVerification(wallet, entiry.password));

	}

	private Single<Wallet> passwordVerification(Wallet wallet, String masterPassword) {
		return passwordStore
				.getPassword(wallet)
				.flatMap(password -> walletRepository
						.exportAccount(wallet, password, password)
						.flatMap(keyStore -> walletRepository.findWallet(wallet.address)))
				.onErrorResumeNext(throwable -> walletRepository
						.deleteWallet(wallet.address, masterPassword)
						.lift(completableErrorProxy(throwable))
						.toSingle(() -> wallet));
	}





	public Single<CreateWalletEntity>  generateMnenonics(String password){
		return walletRepository.generateMnemonics()
				.flatMap(s-> Single.just(new CreateWalletEntity(s,password)));
	}

	public Single<String>  generatePassword(){
		return passwordStore.generatePassword();
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
