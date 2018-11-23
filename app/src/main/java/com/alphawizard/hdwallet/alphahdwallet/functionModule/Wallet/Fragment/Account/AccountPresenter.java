package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Account;


import android.graphics.Bitmap;
import android.support.v7.util.DiffUtil;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Transaction;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.di.ActivityScoped;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.common.presenter.BaseRecyclerPresenter;
import com.alphawizard.hdwallet.common.util.DiffUtilCallback;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.List;

import javax.inject.Inject;
@ActivityScoped
public class AccountPresenter extends BaseRecyclerPresenter<Transaction.TransactionBean,WalletViewModule,AccountContract.View> implements AccountContract.Presenter {

    private boolean isStartGetBalance  =false;

    @Inject
    public AccountPresenter() {
    }

    @Override
    public void getDefaultWallet() {
        getViewModule().getDefaultWallet();
    }

    @Override
    public void getBalanceCyclical() {
        if(!isStartGetBalance) {
            isStartGetBalance = true;
            getViewModule().getBalanceCyclical();
        }
    }

    @Override
    public void getTransactions(){
        getViewModule().fetchTransactions();
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


    public void refresh(List<Transaction.TransactionBean> list){
        // 差异对比
        List<Transaction.TransactionBean> old = getView().getRecyclerViewAdapter().getDataList();
        DiffUtilCallback<Transaction.TransactionBean> callback = new DiffUtilCallback<>(old, list);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        refreshData(result,list);
    }
}
