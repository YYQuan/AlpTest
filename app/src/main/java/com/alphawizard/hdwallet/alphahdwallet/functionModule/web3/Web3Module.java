package com.alphawizard.hdwallet.alphahdwallet.functionModule.web3;

import com.alphawizard.hdwallet.alphahdwallet.di.ActivityScoped;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.send.SendContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.send.SendPresenter;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class Web3Module {

    @ActivityScoped
    @Binds
    abstract Web3Contract.Presenter sendPresenter(Web3Presenter presenter);
}
