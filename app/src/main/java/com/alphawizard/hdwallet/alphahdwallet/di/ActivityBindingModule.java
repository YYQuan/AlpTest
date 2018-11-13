package com.alphawizard.hdwallet.alphahdwallet.di;



import com.alphawizard.hdwallet.alphahdwallet.functionModule.CreateOrImport.CreateOrImportModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportActivity;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Import.ImportModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Launch.LaunchActivity;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Launch.LaunchModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ManagerAccounts.ManagerAccountsActivity;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ManagerAccounts.ManagerAccountsModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletActivity;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.WalletDetail.WalletDetailActivity;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.WalletDetail.WalletDetailModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupMnemonicsActivity;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.CreateOrImport.CreateOrImportActivity;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.send.SendActivity;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.send.SendModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.verifyMnemonics.VerifyMnemonicsActivity;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.verifyMnemonics.VerifyMnemonicsModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.web3.Web3Activity;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.web3.Web3Module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * We want Dagger.Android to create a Subcomponent which has a parent Component of whichever module ActivityBindingModule is on,
 * in our case that will be AppComponent. The beautiful part about this setup is that you never need to tell AppComponent that it is going to have all these subcomponents
 * nor do you need to tell these subcomponents that AppComponent exists.
 * We are also telling Dagger.Android that this generated SubComponent needs to include the specified modules and be aware of a scope annotation @ActivityScoped
 * When Dagger.Android annotation processor runs it will create 4 subcomponents for us.
 */
@Module
public abstract class ActivityBindingModule {



    @ActivityScoped
    @ContributesAndroidInjector(modules = CreateOrImportModule.class)
    abstract CreateOrImportActivity firstLaunchActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = WalletModule.class)
    abstract WalletActivity walletActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = SendModule.class)
    abstract SendActivity sendActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = ImportModule.class)
    abstract ImportActivity importActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = Web3Module.class)
    abstract Web3Activity web3Activity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = BackupModule.class)
    abstract BackupMnemonicsActivity backupMnemonicsActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = VerifyMnemonicsModule.class)
    abstract VerifyMnemonicsActivity verifyMnemonicsActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = WalletDetailModule.class)
    abstract WalletDetailActivity walletDetailActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = ManagerAccountsModule.class)
    abstract ManagerAccountsActivity managerAccountsActivity();


    @ActivityScoped
    @ContributesAndroidInjector(modules = LaunchModule.class)
    abstract LaunchActivity launchActivity();

}
