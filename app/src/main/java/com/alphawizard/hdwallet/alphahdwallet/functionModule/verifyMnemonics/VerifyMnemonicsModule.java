package com.alphawizard.hdwallet.alphahdwallet.functionModule.verifyMnemonics;

import com.alphawizard.hdwallet.alphahdwallet.di.ActivityScoped;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupContract;

import dagger.Binds;
import dagger.Module;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link }.
 */
@Module
public abstract class VerifyMnemonicsModule {

    @ActivityScoped
    @Binds
    abstract BackupContract.Presenter taskPresenter(VerifyMnemonicsPresenter presenter);

//    请注意  和activity 关联的注解 要加上static




}

