package com.alphawizard.hdwallet.alphahdwallet.functionModule.CreateOrImport;

import android.content.Context;
import android.content.Intent;

public class CreateOrImportRouter {

    public void open(Context context) {
        open(context,false);
    }


    public void open(Context context,boolean isFirstOpen) {
        Intent intent = new Intent(context, CreateOrImportActivity.class);
        intent.putExtra(CreateOrImportActivity.FIRST_OPEN,isFirstOpen);
        context.startActivity(intent);
    }
}
