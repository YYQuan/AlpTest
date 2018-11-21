package com.alphawizard.hdwallet.alphahdwallet.interact;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PreferenceRepositoryType;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepository;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepositoryType;
import com.alphawizard.hdwallet.alphahdwallet.service.AccountKeystoreService;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class DefaultWalletInteract {

    PreferenceRepositoryType preferenceRepositoryType;

    WalletRepositoryType walletRepository;

    public DefaultWalletInteract(WalletRepositoryType walletRepository,
            PreferenceRepositoryType   preferenceRepositoryType) {
        this.preferenceRepositoryType =   preferenceRepositoryType;
        this.walletRepository =walletRepository;
    }


    public Completable setDefaultWallet(Wallet wallet){
        return Completable.fromAction(()->preferenceRepositoryType.setCurrentWalletAddress(wallet.address));
    }

    public Completable clearDefaultWallet( ){
        return Completable.fromAction(()->preferenceRepositoryType.setCurrentWalletAddress(null))
                .subscribeOn(Schedulers.io());
    }


    public Single<Wallet> getDefaultWallet(){
        return walletRepository.getDefaultWallet();
    }


    public Single<String> getDefaultWalletAddress(){
        return walletRepository.getDefaultWalletAddress();
    }

}
