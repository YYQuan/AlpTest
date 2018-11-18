package com.alphawizard.hdwallet.alphahdwallet.interact;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PasswordStore;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepositoryType;
import com.alphawizard.hdwallet.alphahdwallet.utils.rx.Operators;

import io.reactivex.Single;

import static com.alphawizard.hdwallet.alphahdwallet.utils.rx.Operators.completableErrorProxy;

public class ImportAccountInteract {

    WalletRepositoryType mWalletRepositoryType;
    PasswordStore mPasswordStore;

    public ImportAccountInteract(WalletRepositoryType walletRepositoryType,PasswordStore passwordStore) {
        mWalletRepositoryType = walletRepositoryType;
        mPasswordStore =  passwordStore;
    }

    public Single<Wallet>  importKeystore(String keystore,String password,String name){
        return mPasswordStore.generatePassword()
                .flatMap(newPassword->mWalletRepositoryType.importKeystore(keystore,password,password)
                        .compose(Operators.savePassword(mPasswordStore, mWalletRepositoryType, password))
                        .compose(Operators.saveWalletName(mPasswordStore, mWalletRepositoryType,name))
                        .flatMap(wallet -> passwordVerification(wallet, password)));


    }

    public Single<Wallet>  importPrivateKey(String privateKey,String name){
        return mPasswordStore.generatePassword()
                .flatMap(newPassword ->mWalletRepositoryType.importPrivateKey(privateKey,newPassword)
                        .compose(Operators.savePassword(mPasswordStore, mWalletRepositoryType, newPassword))
                        .compose(Operators.saveWalletName(mPasswordStore, mWalletRepositoryType,name))
                        .flatMap(wallet -> passwordVerification(wallet, newPassword)));
    }

    public Single<Wallet>  importMnenonics(String mnemonics,String name){
        return mPasswordStore.generatePassword()
                .flatMap(newPassword ->mWalletRepositoryType.importMnenonics(mnemonics,newPassword)
                        .compose(Operators.savePassword(mPasswordStore, mWalletRepositoryType, newPassword))
                        .compose(Operators.saveMnemonics(mPasswordStore, mWalletRepositoryType, mnemonics))
                        .compose(Operators.saveWalletName(mPasswordStore, mWalletRepositoryType,name))
                        .flatMap(wallet -> passwordVerification(wallet, newPassword)));
    }

    private Single<Wallet> passwordVerification(Wallet wallet, String masterPassword) {
        return mPasswordStore
                .getPassword(wallet)
                .flatMap(password -> mWalletRepositoryType
                        .exportAccount(wallet, password, password)
                        .flatMap(keyStore -> mWalletRepositoryType.findWallet(wallet.address)))
                .onErrorResumeNext(throwable -> mWalletRepositoryType
                        .deleteWallet(wallet.address, masterPassword)
                        .lift(completableErrorProxy(throwable))
                        .toSingle(() -> wallet));
    }
}
