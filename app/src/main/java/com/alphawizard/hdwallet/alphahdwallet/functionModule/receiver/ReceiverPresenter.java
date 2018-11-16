package com.alphawizard.hdwallet.alphahdwallet.functionModule.receiver;

import android.graphics.Bitmap;

import com.alphawizard.hdwallet.alphahdwallet.di.ActivityScoped;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Launch.LaunchContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Launch.LaunchViewModule;
import com.alphawizard.hdwallet.common.presenter.BasePresenter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import javax.inject.Inject;

@ActivityScoped
public class ReceiverPresenter extends BasePresenter<ReceiverContract.View,ReceiverViewModule> implements ReceiverContract.Presenter {

    @Inject
    public ReceiverPresenter() {
    }

    @Override
    public Bitmap createQRImage(String address, int imageSize) {
//        Point size = new Point();

//        getWindowManager().getDefaultDisplay().getSize(size);

        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                    address,
                    BarcodeFormat.QR_CODE,
                    imageSize,
                    imageSize,
                    null);

            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();

            return barcodeEncoder.createBitmap(bitMatrix);
        } catch (Exception e) {

        }
        return null;
    }
}
