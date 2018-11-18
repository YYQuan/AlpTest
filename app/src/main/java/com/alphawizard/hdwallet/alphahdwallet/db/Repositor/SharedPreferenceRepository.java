package com.alphawizard.hdwallet.alphahdwallet.db.Repositor;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.utils.AESUtils;

import io.reactivex.Completable;
import io.reactivex.Single;


public class SharedPreferenceRepository implements PreferenceRepositoryType {

	private static final String CURRENT_ACCOUNT_ADDRESS_KEY = "current_account_address";
	private static final String DEFAULT_NETWORK_NAME_KEY = "default_network_name";
	private static final String WALLET_NAME_KEY = "WALLET_NAME_KEY";
	private static final String WALLET_PASSWORD_KEY = "WALLET_PASSWORD_KEY";
	private static final String WALLET_MNEMONICS_KEY = "WALLET_MNEMONICS_KEY";
	private static final String AES_PRIVATE_KEY = "AES_PRIVATE_KEY";
	private static final String GAS_PRICE_KEY  ="gas_price";
    private static final String GAS_LIMIT_KEY  ="gas_limit";
	private static final String GAS_LIMIT_FOR_TOKENS_KEY = "gas_limit_for_tokens";

	private final static SharedPreferences pref  = PreferenceManager.getDefaultSharedPreferences(App.getInstance());

	public SharedPreferenceRepository(Context context) {
//		pref = PreferenceManager.getDefaultSharedPreferences(context);
	}

	@Override
	public String getCurrentWalletAddress() {

//		String result = pref.getString(CURRENT_ACCOUNT_ADDRESS_KEY, null);
		String result = pref.getString(CURRENT_ACCOUNT_ADDRESS_KEY, "");
		return result;
	}

	@Override
	public void setCurrentWalletAddress(String address) {
		pref.edit().putString(CURRENT_ACCOUNT_ADDRESS_KEY, address).apply();
	}

	@Override
	public String getDefaultNetwork() {
		return pref.getString(DEFAULT_NETWORK_NAME_KEY, null);
	}

	@Override
	public void setDefaultNetwork(String netName) {
		pref.edit().putString(DEFAULT_NETWORK_NAME_KEY, netName).apply();
	}


	public static String setAESPrivatekey(String privateKey){

		pref.edit().putString(AES_PRIVATE_KEY, privateKey).apply();
		return privateKey;
	}

	public  static String getAESPrivatekey(){
		/**
		 * 后面把私钥放到jni 中 用c去储存，现在先这样，毕竟这些都是用户之间的数据，丢了也关系不是特别大
		 */
		return pref.getString(AES_PRIVATE_KEY, "alpha_ward");
	}


	public static  String setPassword(String address,String password){
		String key = getAESPrivatekey();

		try {
			password = AESUtils.encrypt(key,password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		pref.edit().putString(WALLET_PASSWORD_KEY+address, password).apply();
		return password;
	}


	public  static String getPassword(String address){
//		return pref.getString(WALLET_PASSWORD_KEY+address, null);
		String  encode =   pref.getString(WALLET_PASSWORD_KEY+address, null);
		String key = getAESPrivatekey();
		String password =null;
		try {
			password = AESUtils.decrypt(key,encode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return password;
	}

	public static  String setName(String address,String name){
		String key = getAESPrivatekey();

//		name = AESUtils.encrypt(name,key);
		pref.edit().putString(WALLET_NAME_KEY+address, name).apply();
		return name;
	}


	public static  String getName(String address){
		return pref.getString(WALLET_NAME_KEY+address, null);
//		String  encode =  pref.getString(WALLET_NAME_KEY+address, null);
//		String key = getAESPrivatekey();
//		return AESUtils.decrypt(encode,key);
	}

	public static  String setMnemonics(String addresss,String mnemonics){
		String key = getAESPrivatekey();
		mnemonics = AESUtils.encrypt(key,mnemonics);
		pref.edit().putString(WALLET_MNEMONICS_KEY+addresss, mnemonics).apply();
		return mnemonics;
	}

	public static  String getMnemonics(String address){
//		return pref.getString(WALLET_MNEMONICS_KEY+address, null);
		String  encode = pref.getString(WALLET_MNEMONICS_KEY+address, null);
				String key = getAESPrivatekey();
		return AESUtils.decrypt(key,encode);
	}

}
