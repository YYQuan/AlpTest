package com.alphawizard.hdwallet.alphahdwallet.data.entiry;

import android.support.annotation.Nullable;

public class WalletExistException extends Exception {
    public static   final String  DEFAULT_INFO ="Wallet already  exist";
    public static   final String  DEFAULT_ALL_INFO  = "com.alphawizard.hdwallet.alphahdwallet.data.entiry.WalletExistException: Wallet already  exist";
    public WalletExistException() {
        super(DEFAULT_INFO);
    }
    public WalletExistException(String message) {
        super(message);
    }

    public WalletExistException( @Nullable String message, Throwable throwable) {
        super(message, throwable);

    }


}
