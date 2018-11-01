package com.alphawizard.hdwallet.alphahdwallet.functionModule.WalletDetail;

import com.alphawizard.hdwallet.alphahdwallet.di.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link }.
 */
@Module
public abstract class WalletDetailModule {

    @ActivityScoped
    @Binds
    abstract WalletDetailContract.Presenter taskPresenter(WalletDetailPresenter presenter);

//    请注意  和activity 关联的注解 要加上static




}

