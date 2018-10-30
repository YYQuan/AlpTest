package com.alphawizard.hdwallet.alphahdwallet.interact;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PasswordStore;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepositoryType;

import io.reactivex.Single;

public class ImportAccountInteract {

    WalletRepositoryType mWalletRepositoryType;
    PasswordStore mPasswordStore;

    public ImportAccountInteract(WalletRepositoryType walletRepositoryType,PasswordStore passwordStore) {
        mWalletRepositoryType = walletRepositoryType;
        mPasswordStore =  passwordStore;
    }

    public Single<Wallet>  importKeystore(String keystore,String password){
        return mWalletRepositoryType.importAccount(keystore,password);

    }

    public Single<Wallet>  importPrivateKey(String privateKey){
        return mPasswordStore.generatePassword()
                .flatMap(newPassword ->mWalletRepositoryType.importPrivateKey(privateKey,newPassword));
    }

    public Single<Wallet>  importMnenonics(String privateKey){
        return mPasswordStore.generatePassword()
                .flatMap(newPassword ->mWalletRepositoryType.importMnenonics(privateKey,newPassword));
    }
}
