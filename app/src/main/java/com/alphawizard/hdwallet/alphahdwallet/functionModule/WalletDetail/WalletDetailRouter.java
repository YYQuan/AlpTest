package com.alphawizard.hdwallet.alphahdwallet.functionModule.WalletDetail;

import android.content.Context;
import android.content.Intent;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupMnemonicsActivity;

public class WalletDetailRouter {




    public void open(Context context) {
        Intent intent = new Intent(context,BackupMnemonicsActivity.class);

        context.startActivity(intent);
    }
}
