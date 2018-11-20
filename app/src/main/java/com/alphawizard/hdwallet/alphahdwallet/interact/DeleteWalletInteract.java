package com.alphawizard.hdwallet.alphahdwallet.interact;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PasswordStore;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepositoryType;

import io.reactivex.Completable;

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
}
