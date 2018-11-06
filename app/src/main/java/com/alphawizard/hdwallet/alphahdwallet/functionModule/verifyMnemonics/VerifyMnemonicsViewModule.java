package com.alphawizard.hdwallet.alphahdwallet.functionModule.verifyMnemonics;

import android.content.Context;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.common.base.ViewModule.BaseViewModel;

public class VerifyMnemonicsViewModule extends BaseViewModel {

    WalletRouter walletRouter;

    public VerifyMnemonicsViewModule(WalletRouter walletRouter )
    {
        this.walletRouter =  walletRouter;
    }


    public void openWallet(Context context){
        walletRouter.open(context);
    }



}
