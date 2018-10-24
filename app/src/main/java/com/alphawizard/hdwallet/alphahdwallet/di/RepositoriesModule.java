package com.alphawizard.hdwallet.alphahdwallet.di;

import android.content.Context;

import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PreferenceRepositoryType;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.SharedPreferenceRepository;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepository;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepositoryType;
import com.alphawizard.hdwallet.alphahdwallet.service.AccountKeystoreService;
import com.alphawizard.hdwallet.alphahdwallet.service.EthTickerService;
import com.alphawizard.hdwallet.alphahdwallet.service.GethKeystoreAccountService;
import com.alphawizard.hdwallet.alphahdwallet.service.TickerService;
import com.google.gson.Gson;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class RepositoriesModule {
	@Singleton
	@Provides
	PreferenceRepositoryType providePreferenceRepository(Context context) {
		return new SharedPreferenceRepository(context);
	}

	@Singleton
	@Provides
	AccountKeystoreService provideAccountKeyStoreService(Context context) {
        File file = new File(context.getFilesDir(), "keystore/keystore");
		return new GethKeystoreAccountService(file);
	}


	@Singleton
	@Provides
	TickerService provideTickerService(OkHttpClient client , Gson  gson) {
		return new EthTickerService(client,gson);
	}

//	@Singleton
//	@Provides
//	WalletRepositoryType provideWalletRepositoryType(OkHttpClient client) {
//		return new WalletRepository(client);
//	}

	@Singleton
	@Provides
	WalletRepositoryType provideWalletRepositoryType(OkHttpClient httpClient,
													 PreferenceRepositoryType preferenceRepositoryType,
													 AccountKeystoreService accountKeystoreService,
													 TickerService tickerService) {
		return new WalletRepository(httpClient,preferenceRepositoryType,accountKeystoreService,tickerService);
	}

}
