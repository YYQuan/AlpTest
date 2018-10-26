package com.alphawizard.hdwallet.alphahdwallet.functionModule.Import;

import com.alphawizard.hdwallet.alphahdwallet.di.ActivityScoped;
import com.alphawizard.hdwallet.alphahdwallet.di.FragmentScoped;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importKeyStore.ImportKeyStoreContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importKeyStore.ImportKeyStoreFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importKeyStore.ImportKeyStorePresenter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importPrivateKey.ImportPrivateKeyContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importPrivateKey.ImportPrivateKeyFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.fragment.importPrivateKey.ImportPrivateKeyPresenter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Account.AccountFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Dimension.DimensionContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Dimension.DimensionPresenter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.fristLaunch.FirstLaunchContract;


import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ImportModule {

    @ActivityScoped
    @Binds
    abstract ImportContract.Presenter taskPresenter(ImportPresenter presenter);

    @ActivityScoped
    @Binds abstract ImportKeyStoreContract.Presenter importKeyStorePresenter(ImportKeyStorePresenter presenter);

    @ActivityScoped
    @Binds abstract ImportPrivateKeyContract.Presenter importPrivateKeyPresenter(ImportPrivateKeyPresenter presenter);

    @FragmentScoped
    @ContributesAndroidInjector
    abstract ImportKeyStoreFragment importKeyStoreFragment();


    @FragmentScoped
    @ContributesAndroidInjector
    abstract ImportPrivateKeyFragment importPrivateKeyFragment();
}
