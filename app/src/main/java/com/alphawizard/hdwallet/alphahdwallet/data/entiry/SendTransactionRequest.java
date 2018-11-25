package com.alphawizard.hdwallet.alphahdwallet.data.entiry;

import android.os.Parcel;
import android.os.Parcelable;

public class SendTransactionRequest {
    String  form ;
    String  to ;
    String  ethAmount ;
    String  gasPrice ;

    public SendTransactionRequest(String form, String to, String ethAmount, String  gasPrice) {
        this.form = form;
        this.to = to;
        this.ethAmount = ethAmount;
        this.gasPrice = gasPrice;
    }


    public String getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getEthAmount() {
        return ethAmount;
    }

    public void setEthAmount(String ethAmount) {
        this.ethAmount = ethAmount;
    }

}
