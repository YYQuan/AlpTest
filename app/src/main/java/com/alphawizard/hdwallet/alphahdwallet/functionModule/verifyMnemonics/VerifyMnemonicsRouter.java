package com.alphawizard.hdwallet.alphahdwallet.functionModule.verifyMnemonics;

import android.content.Context;
import android.content.Intent;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupMnemonicsActivity;

import java.util.ArrayList;

public class VerifyMnemonicsRouter {

    public static  final  String MNEMONICS_STRING = "MNEMONICS_STRING";


    public void open(Context context) {
        Intent intent = new Intent(context,VerifyMnemonicsActivity.class);

        context.startActivity(intent);
    }

    public void open(Context context,  ArrayList<String> stringArr) {
        Intent intent = new Intent(context,VerifyMnemonicsActivity.class);
        intent.putStringArrayListExtra(MNEMONICS_STRING, stringArr);
        context.startActivity(intent);
    }
}
