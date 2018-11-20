package com.alphawizard.hdwallet.alphahdwallet.interact;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepositoryType;

import io.reactivex.Single;

public class FetchWalletInteract {

    WalletRepositoryType walletRepository;

    public FetchWalletInteract(WalletRepositoryType walletRepository) {
        this.walletRepository =  walletRepository;
    }

    public Single<Wallet[]> fetchAccounts(){
        return walletRepository.fetchWallets();
    }

    public Single<Wallet> fetchAccount(Wallet wallet){
        return walletRepository.findWallet(wallet.address);
    }
}
