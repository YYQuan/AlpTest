package com.alphawizard.hdwallet.alphahdwallet.functionModule.send;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.interact.SendTransactionInteract;
import com.alphawizard.hdwallet.common.base.ViewModule.BaseViewModel;

public class SendViewModule extends BaseViewModel {

    SendTransactionInteract  mSendTransactionInteract;

    public SendViewModule(SendTransactionInteract mSendTransactionInteract) {
        this.mSendTransactionInteract = mSendTransactionInteract;
    }


    public  void  sendTransaction(String  to , String amount){
        mSendTransactionInteract.sendTransaction(to,amount);
    }
}
