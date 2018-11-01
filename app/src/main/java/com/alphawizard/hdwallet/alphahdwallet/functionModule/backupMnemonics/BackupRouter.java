package com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics;

import android.content.Context;
import android.content.Intent;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.fristLaunch.FirstLaunchActivity;

public class BackupRouter {




    public void open(Context context) {
        Intent intent = new Intent(context,BackupMnemonicsActivity.class);

        context.startActivity(intent);
    }
}
