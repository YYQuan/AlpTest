package com.alphawizard.hdwallet.alphahdwallet.interact;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PasswordStore;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepositoryType;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ExportWalletInteract {

    private  WalletRepositoryType walletRepository;
    PasswordStore passwordStore;


    public ExportWalletInteract(WalletRepositoryType walletRepository,
                                PasswordStore passwordStore) {
        this.walletRepository = walletRepository;
        this.passwordStore =  passwordStore;
    }

    public Single<String> exportKeystore(Wallet wallet, String backupPassword) {
//        String  mnemonics = passwordStore.getMnemonics(wallet).blockingGet();
        return passwordStore.getPassword(wallet)
                .flatMap(password ->walletRepository.exportAccount(wallet,password,backupPassword));
    }

    public Single<String> exportMnemonics(Wallet wallet) {

        return passwordStore.getMnemonics(wallet);

    }

    public Single<String> exportPrivateKey(Wallet wallet) {

        return passwordStore.getPrivateKey(wallet);


    }

}
