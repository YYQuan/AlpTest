package com.alphawizard.hdwallet.alphahdwallet.interact;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.CreateWalletEntity;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PasswordStore;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepositoryType;
import com.alphawizard.hdwallet.alphahdwallet.utils.rx.Operators;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.alphawizard.hdwallet.alphahdwallet.utils.rx.Operators.completableErrorProxy;


public class CreateWalletInteract {

	WalletRepositoryType walletRepository;
	PasswordStore passwordStore;

	public CreateWalletInteract(WalletRepositoryType walletRepository, PasswordStore passwordStore) {
		this.walletRepository =  walletRepository;
		this.passwordStore = passwordStore;
	}


	public Single<Wallet> create(CreateWalletEntity entiry) {

		return  walletRepository.createAccount(entiry.getMnenonics(),entiry.getPassword())
				.compose(Operators.savePassword(passwordStore, walletRepository, entiry.getPassword()))
				.compose(Operators.saveMnemonics(passwordStore, walletRepository, entiry.getMnenonics()))
				.compose(Operators.saveWalletName(passwordStore, entiry.getWalletName()))
				.flatMap(wallet -> passwordVerification(wallet, entiry.getPassword()))
				.subscribeOn(Schedulers.io());

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





	public Single<CreateWalletEntity>  generateMnenonics(String password,String walletName){
		return walletRepository.generateMnemonics()
				.flatMap(s-> Single.just(new CreateWalletEntity(s,password,walletName)));
	}

	public Single<String>  generatePassword(){
		return passwordStore.generatePassword();
	}


//	public static class  CreateWalletEntity {
//		String   password ;
//		String  mnenonics ;
//		String walletName;
//
//
//		public CreateWalletEntity(String mnenonics , String  password,String walletName) {
//			this.password = password;
//			this.mnenonics = mnenonics;
//			this.walletName = walletName;
//		}
//
//		public String getPassword() {
//
//			return password;
//		}
//
//		public String getWalletName() {
//			return walletName;
//		}
//
//		public void setWalletName(String walletName) {
//			this.walletName = walletName;
//		}
//
//		public void setPassword(String password) {
//			this.password = password;
//		}
//
//		public String getMnenonics() {
//			return mnenonics;
//		}
//
//		public void setMnenonics(String mnenonics) {
//			this.mnenonics = mnenonics;
//		}
//	}
}
