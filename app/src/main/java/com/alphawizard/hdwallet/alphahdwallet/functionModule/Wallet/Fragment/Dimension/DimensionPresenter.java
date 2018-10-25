package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Dimension;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.widget.Toast;

import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.di.ActivityScoped;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.send.SendContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.send.SendViewModule;
import com.alphawizard.hdwallet.common.base.App.Activity;
import com.alphawizard.hdwallet.common.base.App.Fragment;
import com.alphawizard.hdwallet.common.presenter.BasePresenter;
import com.alphawizard.hdwallet.common.presenter.BaseRecyclerPresenter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import javax.inject.Inject;

@ActivityScoped
public class DimensionPresenter extends BasePresenter<DimensionContract.View,WalletViewModule> implements DimensionContract.Presenter  {


    @Inject
    public DimensionPresenter() {
    }

    @Override
    public Bitmap createQRImage(String address,int imageSize) {
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
