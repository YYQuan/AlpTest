package com.alphawizard.hdwallet.alphahdwallet.interact;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepositoryType;

import io.reactivex.Single;

public class ImportAccountInteract {

    WalletRepositoryType mWalletRepositoryType;

    public ImportAccountInteract(WalletRepositoryType walletRepositoryType) {
        mWalletRepositoryType = walletRepositoryType;
    }

    public Single<Wallet>  importKeystore(String keystore ,String password){
        return mWalletRepositoryType.importAccount(keystore,password);
    }

    public Single<Wallet>  importPrivateKey(String privateKey ,String password){
        return mWalletRepositoryType.importPrivateKey(privateKey,password);
    }
}
