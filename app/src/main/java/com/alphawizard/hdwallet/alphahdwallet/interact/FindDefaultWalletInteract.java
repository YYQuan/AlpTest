package com.alphawizard.hdwallet.alphahdwallet.interact;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PreferenceRepositoryType;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepositoryType;
import com.alphawizard.hdwallet.alphahdwallet.service.AccountKeystoreService;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FindDefaultWalletInteract {

	PreferenceRepositoryType preferenceRepositoryType;
	WalletRepositoryType walletRepositoryType;

	public FindDefaultWalletInteract(PreferenceRepositoryType preferenceRepositoryType,
									 WalletRepositoryType walletRepositoryType) {
		this.preferenceRepositoryType = preferenceRepositoryType;
		this.walletRepositoryType =  walletRepositoryType;
	}

	public Single<Wallet> find() {
		return getDefaultWallet()
//				onErrorResumeNext操作符的意思是 如果  single 回调error的时候，
// 	·			就用 新的single（onErrorResumeNext中的参数）替代原始的single 去通知观察者
				.onErrorResumeNext(
//						这部分只在原始single  error时 起作用


						walletRepositoryType.fetchWallets()
						.to(single -> Flowable.fromArray(single.blockingGet()))
//						那flowable中的第一个元素，如果该元素为null则throw 异常
						.firstOrError()
						.flatMapCompletable(wallet->Completable.fromAction(() -> preferenceRepositoryType.setCurrentWalletAddress(wallet.address)))
						.andThen(Single.fromCallable(preferenceRepositoryType::getCurrentWalletAddress)
								.flatMap(walletRepositoryType::findWallet))
				)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	protected Single<Wallet> getDefaultWallet() {
		return walletRepositoryType.getDefaultWallet();
	}

}
