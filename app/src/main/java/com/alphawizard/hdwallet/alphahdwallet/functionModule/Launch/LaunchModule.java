package com.alphawizard.hdwallet.alphahdwallet.functionModule.Launch;

import com.alphawizard.hdwallet.alphahdwallet.di.ActivityScoped;
import dagger.Binds;
import dagger.Module;


@Module
public abstract class LaunchModule {

    @ActivityScoped
    @Binds
    abstract LaunchContract.Presenter taskPresenter(LaunchPresenter presenter);

//    请注意  和activity 关联的注解 要加上static



}