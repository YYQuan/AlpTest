package com.example.web3lib;

import trust.core.entity.Transaction;

public interface OnSignTransactionListener {
    void onSignTransaction(Transaction transaction);
}
