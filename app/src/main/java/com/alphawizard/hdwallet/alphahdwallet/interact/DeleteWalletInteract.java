package com.alphawizard.hdwallet.alphahdwallet.interact;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PasswordStore;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepositoryType;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class DeleteWalletInteract {

    WalletRepositoryType walletRepository;
    PasswordStore passwordStore;

    public DeleteWalletInteract(WalletRepositoryType walletRepository, PasswordStore passwordStore) {
        this.walletRepository =  walletRepository;
        this.passwordStore = passwordStore;
    }

    public Completable  deleteWallet(Wallet wallet,String password){
        return Completable.fromAction(()->walletRepository.deleteWallet(wallet.address,password));
    }

    public Single<Wallet[]> delete(Wallet wallet,String pass) {
        return passwordStore.getPassword(wallet)
                .flatMapCompletable(password -> {
                    if(password.equalsIgnoreCase(pass)){
                        return walletRepository.deleteWallet(wallet.address, pass);
                    }
                    return null;
                })
                .andThen(walletRepository.fetchWallets())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
