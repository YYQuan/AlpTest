package com.alphawizard.hdwallet.alphahdwallet.functionModule.verifyMnemonics;

import android.content.Context;
import android.content.Intent;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupMnemonicsActivity;

public class VerifyMnemonicsRouter {




    public void open(Context context) {
        Intent intent = new Intent(context,BackupMnemonicsActivity.class);

        context.startActivity(intent);
    }
}
