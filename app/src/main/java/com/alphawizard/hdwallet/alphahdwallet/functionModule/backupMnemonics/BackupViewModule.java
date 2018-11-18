package com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.verifyMnemonics.VerifyMnemonicsRouter;
import com.alphawizard.hdwallet.alphahdwallet.interact.CreateWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.interact.DefaultWalletInteract;
import com.alphawizard.hdwallet.common.base.ViewModule.BaseViewModel;
import com.alphawizard.hdwallet.common.util.Log;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class BackupViewModule extends BaseViewModel {

    VerifyMnemonicsRouter verifyMnemonicsRouter;




    public BackupViewModule(VerifyMnemonicsRouter verifyMnemonicsRouter)
    {
        this.verifyMnemonicsRouter =verifyMnemonicsRouter;

    }









    public void openVerify(Context context, ArrayList<String> strings){
        verifyMnemonicsRouter.open(context,strings);
    }


}
