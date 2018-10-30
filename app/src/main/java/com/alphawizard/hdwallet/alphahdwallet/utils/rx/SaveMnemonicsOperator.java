package com.alphawizard.hdwallet.alphahdwallet.utils.rx;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PasswordStore;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepositoryType;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.observers.DisposableCompletableObserver;

public class SaveMnemonicsOperator implements SingleTransformer<Wallet, Wallet> {

    private final PasswordStore passwordStore;
    private final String mnemonics;
    private final WalletRepositoryType walletRepository;

    public SaveMnemonicsOperator(
            PasswordStore passwordStore, WalletRepositoryType walletRepository, String mnemonics) {
        this.passwordStore = passwordStore;
        this.mnemonics = mnemonics;
        this.walletRepository = walletRepository;
    }

    @Override
    public SingleSource<Wallet> apply(Single<Wallet> upstream) {
        Wallet wallet = upstream.blockingGet();
        return passwordStore
                .setMnemonics(wallet, mnemonics)
                .onErrorResumeNext(err -> walletRepository.deleteWallet(wallet.address, mnemonics)
                        .lift(observer -> new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                observer.onError(err);
                            }

                            @Override
                            public void onError(Throwable e) {
                                observer.onError(e);
                            }
                        }))
                .toSingle(() -> wallet);
    }
}
