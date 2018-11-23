package com.alphawizard.hdwallet.alphahdwallet.utils.rx;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PasswordStore;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepositoryType;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.observers.DisposableCompletableObserver;

public class SetDefaultWalletOperator implements SingleTransformer<Wallet, Wallet> {

    private final DefaultWalletInteract defaultWalletInteract;

    public SetDefaultWalletOperator(DefaultWalletInteract defaultWalletInteract) {

        this.defaultWalletInteract = defaultWalletInteract;
    }

    @Override
    public SingleSource<Wallet> apply(Single<Wallet> upstream) {
        Wallet wallet = upstream.blockingGet();
        return defaultWalletInteract
                .setDefaultWallet(wallet)
                .toSingle(() -> wallet);
    }
}
