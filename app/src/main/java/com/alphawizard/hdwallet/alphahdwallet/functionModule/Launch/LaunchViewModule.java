package com.alphawizard.hdwallet.alphahdwallet.functionModule.Launch;

import android.content.Context;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletRouter;
import com.alphawizard.hdwallet.common.base.ViewModule.BaseViewModel;

public class LaunchViewModule extends BaseViewModel {
    WalletRouter  mWalletRouter;

    public LaunchViewModule(WalletRouter mWalletRouter) {
        this.mWalletRouter = mWalletRouter;
    }


    public void  openWallet(Context context){
        mWalletRouter.open(context);
    }
}
