package com.alphawizard.hdwallet.alphahdwallet.db.Repositor;

import android.content.Context;
import android.os.Build;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.ServiceErrorException;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.service.AccountKeystoreService;
import com.alphawizard.hdwallet.alphahdwallet.utils.KS;
import com.alphawizard.hdwallet.common.util.Log;

import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import java.security.SecureRandom;

import io.github.novacrypto.bip32.ExtendedPrivateKey;
import io.github.novacrypto.bip32.networks.Bitcoin;
import io.github.novacrypto.bip39.SeedCalculator;
import io.github.novacrypto.bip44.AddressIndex;
import io.github.novacrypto.bip44.BIP44;
import io.reactivex.Completable;
import io.reactivex.Single;

public class PasswordStoreRepository implements PasswordStore {

    private static final  String MNEMONICS_TAG   = "MNEMONICS:";
    private static final  String WALLET_NAME_TAG   = "WalletName:";

    private final Context context;
    AccountKeystoreService accountKeystoreService;

	public PasswordStoreRepository(Context context,
                                   AccountKeystoreService accountKeystoreService){
		this.context = context;
        this.accountKeystoreService =  accountKeystoreService;
	}



    @Override
	public Single<String> getPassword(Wallet wallet) {
		return Single.fromCallable(() -> {
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
                String password =  new String(KS.get(context,wallet.address));
                return password;
            } else {
                String password = SharedPreferenceRepository.getPassword(wallet.address);
//                    throw new ServiceErrorException(ServiceErrorException.KEY_STORE_ERROR);
                return password;
            }
        });
	}

    @Override
    public Single<String> getMnemonics(Wallet wallet) {
        return Single.fromCallable(() -> {
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
                String mnemonics =  new String(KS.get(context,MNEMONICS_TAG + wallet.address));
                return mnemonics;
            } else {

                String mnemonics = SharedPreferenceRepository.getMnemonics(wallet.address);
                return mnemonics;
//                throw new ServiceErrorException(ServiceErrorException.KEY_STORE_ERROR);
            }
        });
    }


    @Override
	public Completable setPassword(Wallet wallet, String password) {
        return Completable.fromAction(() -> {
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
                 KS.put(context, wallet.address, password);

            } else {
                SharedPreferenceRepository.setPassword(wallet.address,password);
//                    throw new ServiceErrorException(ServiceErrorException.KEY_STORE_ERROR);
            }
        });
	}

    @Override
    public Completable setMnemonics(Wallet wallet, String mnemonics) {
        return Completable.fromAction(() -> {
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
                KS.put(context, MNEMONICS_TAG + wallet.address, mnemonics);

            } else {
                SharedPreferenceRepository.setMnemonics(wallet.address,mnemonics);
//                throw new ServiceErrorException(ServiceErrorException.KEY_STORE_ERROR);
            }
        });
    }

    @Override
    public Completable setWalletName(Wallet wallet, String name) {
        return Completable.fromAction(() -> {
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
                KS.put(context, WALLET_NAME_TAG + wallet.address, name);

            } else {
                SharedPreferenceRepository.setName(wallet.address,name);
//                throw new ServiceErrorException(ServiceErrorException.KEY_STORE_ERROR);
            }
        });
    }

    @Override
    public Single<String> getWalletName(Wallet wallet) {
        return Single.fromCallable(() -> {
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
                String name =  new String(KS.get(context,WALLET_NAME_TAG + wallet.address));
                return name;
            } else {
                String name =SharedPreferenceRepository.getName(wallet.address);
                return  name;
//                throw new ServiceErrorException(ServiceErrorException.KEY_STORE_ERROR);
            }
        });
    }


    //产生 随机  password
    @Override
    public Single<String>
          generatePassword() {
        return Single.fromCallable(() -> {
//            byte bytes[] = new byte[256];
//          aes加密算法 长度不能长过128
            byte bytes[] = new byte[128];
            SecureRandom random = new SecureRandom();
            random.nextBytes(bytes);
//			没做password  保存之前， 就先用123 作为 password
//            return "123";
			return new String(bytes);
        });
    }

    @Override
    public Single<String> generateWalletName(){
        return accountKeystoreService.fetchAccounts()
                .flatMap(wallets ->  Single.just( new  String("钱包 #"+wallets.length)) );

//        return Single.fromCallable(() -> new String("Wallet"));
    }

    @Override
    public Single<String> getPrivateKey(Wallet   wallet) {
	            return getPassword(wallet)
                        .flatMap( password->
                                    accountKeystoreService.exportAccount(wallet,password,password)
                                            .flatMap(keystore -> accountKeystoreService.getPrivateKeyFromKeystore(keystore,password))
                                );





//        return Single.fromCallable(() -> {
//            accountKeystoreService.getPrivateKeyFromKeystore()

//            accountKeystoreService.getPrivateKeyFromKeystore("123","123");
////        mnemonics = "puppy puppy menu menu menu menu menu menu menu menu menu menu";
//            // 1. we just need eth wallet for now
//            AddressIndex addressIndex = BIP44
//                    .m()
//                    .purpose44()
//                    .coinType(60)
//                    .account(0)
//                    .external()
////                这个address就是 该助记词对应的hd seed 的第几个私钥
//                    .address(0);
//            // 2. calculate seed from mnemonics , then get master/root key ; Note that the bip39 passphrase we set "" for common
//            ExtendedPrivateKey rootKey = ExtendedPrivateKey.fromSeed(new SeedCalculator().calculateSeed(mnemonics, ""), Bitcoin.MAIN_NET);
////        Logger.i("mnemonics:" + mnemonics);
//            String extendedBase58 = rootKey.extendedBase58();
////        Logger.i("extendedBase58:" + extendedBase58);
//
//            // 3. get child private key deriving from master/root key
//            ExtendedPrivateKey childPrivateKey = rootKey.derive(addressIndex, AddressIndex.DERIVATION);
//            String childExtendedBase58 = childPrivateKey.extendedBase58();
////        Logger.i("childExtendedBase58:" + childExtendedBase58);
////        Logger.i("childExtendedBase58:" + childExtendedBase58);
//
//            // 4. get key pair
//            byte[] privateKeyBytes = childPrivateKey.getKey();
//            ECKeyPair keyPair = ECKeyPair.create(privateKeyBytes);
//
//
//            // we 've gotten what we need
//            String privateKey = childPrivateKey.getPrivateKey();
//            Log.d("privateKey:" + privateKey);
//            String publicKey = childPrivateKey.neuter().getPublicKey();
//            String address = Keys.getAddress(keyPair);
//            return privateKey;
//        });

    }
}
