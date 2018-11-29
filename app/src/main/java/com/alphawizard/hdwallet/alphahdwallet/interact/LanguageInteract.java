package com.alphawizard.hdwallet.alphahdwallet.interact;

import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PasswordStore;

import io.reactivex.Completable;
import io.reactivex.Single;


public class LanguageInteract {
	public static final String    LANGUAGE_CHINA  =  "zh-CN";
	public static final String    LANGUAGE_ENGLISH  =  "en-US";

	PasswordStore passwordStore;

	public LanguageInteract(PasswordStore passwordStore) {

		this.passwordStore = passwordStore;
	}


	public Completable setCurrentLanguage(String str ){
		 return  passwordStore.setLanguage(str);
	}


	public Single<String> getCurrentLanguage(){
		return passwordStore.getLanguage();
	}


}
