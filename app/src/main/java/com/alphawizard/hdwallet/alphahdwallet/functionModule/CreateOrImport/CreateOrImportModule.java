package com.alphawizard.hdwallet.alphahdwallet.functionModule.CreateOrImport;

import com.alphawizard.hdwallet.alphahdwallet.di.ActivityScoped;


import dagger.Binds;
import dagger.Module;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link }.
 */
@Module
public abstract class CreateOrImportModule {

    @ActivityScoped
    @Binds
    abstract CreateOrImportContract.Presenter taskPresenter(CreateOrImportPresenter presenter);

//    请注意  和activity 关联的注解 要加上static




}

