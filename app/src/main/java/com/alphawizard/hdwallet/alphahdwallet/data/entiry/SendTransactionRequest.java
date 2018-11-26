package com.alphawizard.hdwallet.alphahdwallet.data.entiry;

import android.os.Parcel;
import android.os.Parcelable;

public class SendTransactionRequest {
    String  form ;
    String  to ;
    String  ethAmount ;


    long  gaslimit ;
//    单位是G wei 要注意
    long  gasPrice ;

    String  data ;

    public SendTransactionRequest(String form, String to, String ethAmount, long  gasPrice,long  gaslimit,String  data) {
        this.form = form;
        this.to = to;
        this.ethAmount = ethAmount;

        this.gaslimit=gaslimit;
        this.gasPrice=gasPrice;
        this.data = data;
    }




    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getGaslimit() {
        return gaslimit;
    }

    public void setGaslimit(long gaslimit) {
        this.gaslimit = gaslimit;
    }

    public long getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(long gasPrice) {
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
