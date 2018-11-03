package com.alphawizard.hdwallet.alphahdwallet.functionModule.ManagerAccounts;

import android.content.Context;
import android.content.Intent;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupMnemonicsActivity;

public class ManagerAccountsRouter {




    public void open(Context context) {
        Intent intent = new Intent(context,ManagerAccountsActivity.class);

        context.startActivity(intent);
    }
}
