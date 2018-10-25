package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Dimension;

import android.graphics.Bitmap;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Account.AccountContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.common.presenter.BaseContract;

public interface DimensionContract {

    interface View extends BaseContract.BaseView<DimensionContract.Presenter,WalletViewModule> {

    }

    interface Presenter extends BaseContract.BasePresenter<DimensionContract.View,WalletViewModule> {
         Bitmap createQRImage(String address,int imageSize) ;

    }
}
