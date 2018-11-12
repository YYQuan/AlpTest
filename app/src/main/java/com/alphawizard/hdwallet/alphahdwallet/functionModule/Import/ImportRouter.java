package com.alphawizard.hdwallet.alphahdwallet.functionModule.Import;

import android.content.Context;
import android.content.Intent;

public class ImportRouter {

    public void open(Context context) {
        Intent intent = new Intent(context, ImportActivity.class);
        context.startActivity(intent);
    }
}
