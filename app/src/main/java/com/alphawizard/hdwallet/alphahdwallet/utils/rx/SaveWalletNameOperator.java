package com.alphawizard.hdwallet.alphahdwallet.utils.rx;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PasswordStore;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepositoryType;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.observers.DisposableCompletableObserver;

public class SaveWalletNameOperator implements SingleTransformer<Wallet, Wallet> {

    private final PasswordStore passwordStore;
    private final String name;


    public SaveWalletNameOperator(
            PasswordStore passwordStore,  String name) {
        this.passwordStore = passwordStore;
        this.name = name;

    }

    @Override
    public SingleSource<Wallet> apply(Single<Wallet> upstream) {
        Wallet wallet = upstream.blockingGet();
        return passwordStore
                .setWalletName(wallet, name)
                .toSingle(() -> wallet);
    }
}
