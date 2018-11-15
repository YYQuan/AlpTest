package com.alphawizard.hdwallet.alphahdwallet.functionModule.receiver;

import com.alphawizard.hdwallet.alphahdwallet.di.ActivityScoped;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Launch.LaunchContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Launch.LaunchPresenter;

import dagger.Binds;
import dagger.Module;


@Module
public abstract class ReceiverModule {

    @ActivityScoped
    @Binds
    abstract ReceiverContract.Presenter taskPresenter(ReceiverPresenter presenter);

//    请注意  和activity 关联的注解 要加上static



}