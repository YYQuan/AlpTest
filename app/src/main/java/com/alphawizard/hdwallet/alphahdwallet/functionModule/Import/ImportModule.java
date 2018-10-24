package com.alphawizard.hdwallet.alphahdwallet.functionModule.Import;

import com.alphawizard.hdwallet.alphahdwallet.di.ActivityScoped;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.fristLaunch.FirstLaunchContract;


import dagger.Binds;
import dagger.Module;

@Module
public abstract class ImportModule {

    @ActivityScoped
    @Binds
    abstract ImportContract.Presenter taskPresenter(ImportPresenter presenter);
}
