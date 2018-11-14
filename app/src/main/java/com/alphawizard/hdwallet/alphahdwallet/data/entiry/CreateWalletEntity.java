package com.alphawizard.hdwallet.alphahdwallet.data.entiry;

public class CreateWalletEntity {
    private String   password ;
    private String  mnenonics ;
    private String walletName;


    public CreateWalletEntity(String mnenonics , String  password,String walletName) {
        this.password = password;
        this.mnenonics = mnenonics;
        this.walletName = walletName;
    }

    public String getPassword() {

        return password;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMnenonics() {
        return mnenonics;
    }

    public void setMnenonics(String mnenonics) {
        this.mnenonics = mnenonics;
    }
}
