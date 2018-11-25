package com.alphawizard.hdwallet.alphahdwallet.functionModule.ConfirmSend;

import com.alphawizard.hdwallet.alphahdwallet.di.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ConfirmSendModule {
    @ActivityScoped
    @Binds
    abstract ConfirmSendContract.Presenter taskPresenter(ConfirmSendPresenter presenter);
}
