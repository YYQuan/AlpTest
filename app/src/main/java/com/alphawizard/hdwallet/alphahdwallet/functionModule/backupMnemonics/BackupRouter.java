package com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

public class BackupRouter {

    public static  final  String MNEMONICS_STRING = "MNEMONICS_STRING";


    public void open(Context context, String[] stringArr) {
        Intent intent = new Intent(context,BackupMnemonicsActivity.class);
        intent.putExtra(MNEMONICS_STRING ,stringArr);

        context.startActivity(intent);
    }


    public void open(Context context,  ArrayList<String> stringArr) {
        Intent intent = new Intent(context,BackupMnemonicsActivity.class);
//        intent.putExtra(MNEMONICS_STRING ,stringArr);
        intent.putStringArrayListExtra(MNEMONICS_STRING, stringArr);
        context.startActivity(intent);
    }
}
