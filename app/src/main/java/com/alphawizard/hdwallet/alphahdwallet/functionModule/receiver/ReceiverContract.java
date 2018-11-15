package com.alphawizard.hdwallet.alphahdwallet.functionModule.receiver;

import android.graphics.Bitmap;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.Launch.LaunchViewModule;
import com.alphawizard.hdwallet.common.presenter.BaseContract;

public interface ReceiverContract {
    interface View extends BaseContract.BaseView<ReceiverContract.Presenter, ReceiverViewModule> {
    }

    interface Presenter extends BaseContract.BasePresenter<ReceiverContract.View, ReceiverViewModule> {
        Bitmap createQRImage(String address, int imageSize);
    }
}
