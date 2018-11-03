package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet;

import com.alphawizard.hdwallet.alphahdwallet.di.ActivityScoped;
import com.alphawizard.hdwallet.alphahdwallet.di.FragmentScoped;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Account.AccountContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Account.AccountFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Account.AccountPresenter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Accounts.AccountsContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Accounts.AccountsFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Accounts.AccountsPresenter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Dimension.DimensionContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Dimension.DimensionFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Dimension.DimensionPresenter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.dapp.DappContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.dapp.DappFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.dapp.DappPresenter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.setting.SettingContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.setting.SettingFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.setting.SettingPresenter;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link }.
 */
@Module
public abstract class WalletModule {

    @ActivityScoped
    @Binds abstract WalletActivityContract.Presenter walletPresenter(WalletActivityPresenter presenter);

    @ActivityScoped
    @Binds abstract AccountsContract.Presenter accountsPresenter(AccountsPresenter presenter);

    @ActivityScoped
    @Binds abstract AccountContract.Presenter accountPresenter(AccountPresenter presenter);

    @ActivityScoped
    @Binds abstract DimensionContract.Presenter dimensionPresenter(DimensionPresenter presenter);

    @ActivityScoped
    @Binds abstract DappContract.Presenter dappPresenter(DappPresenter presenter);

    @ActivityScoped
    @Binds abstract SettingContract.Presenter settingPresenter(SettingPresenter presenter);

    @FragmentScoped
    @ContributesAndroidInjector
    abstract AccountFragment accountFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract AccountsFragment accountsFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract DimensionFragment dimensionFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract DappFragment dappFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract SettingFragment settingFragment();
}
