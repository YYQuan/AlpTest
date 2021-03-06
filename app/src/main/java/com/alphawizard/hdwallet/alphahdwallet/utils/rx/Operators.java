package com.alphawizard.hdwallet.alphahdwallet.utils.rx;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PasswordStore;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepositoryType;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;


import io.reactivex.CompletableOperator;
import io.reactivex.SingleTransformer;

public class Operators {

    public static SingleTransformer<Wallet, Wallet> savePassword(
            PasswordStore passwordStore, WalletRepositoryType walletRepository, String password) {
        return new SavePasswordOperator(passwordStore, walletRepository, password);
    }

    public static SingleTransformer<Wallet, Wallet> saveMnemonics(
            PasswordStore passwordStore, WalletRepositoryType walletRepository, String mnemonics) {
        return new SaveMnemonicsOperator(passwordStore, walletRepository, mnemonics);
    }


    public static SingleTransformer<Wallet, Wallet> saveWalletName(
            PasswordStore passwordStore, String name) {
        return new SaveWalletNameOperator(passwordStore, name);
    }


    public static SingleTransformer<Wallet, Wallet> setDefaultWallet(DefaultWalletInteract defaultWalletInteract) {
        return new SetDefaultWalletOperator(defaultWalletInteract);
    }

    public static CompletableOperator completableErrorProxy(Throwable throwable) {
        return new CompletableErrorProxyOperator(throwable);
    }


}
