package com.alphawizard.hdwallet.alphahdwallet.db.Repositor;

import android.content.Context;
import android.os.Build;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.ServiceErrorException;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.utils.KS;
import java.security.SecureRandom;
import io.reactivex.Completable;
import io.reactivex.Single;

public class PasswordStoreRepository implements PasswordStore {

	private final Context context;
	private static final  String MNEMONICS_TAG   = "MNEMONICS:";

	public PasswordStoreRepository(Context context) {
		this.context = context;
	}



    @Override
	public Single<String> getPassword(Wallet wallet) {
		return Single.fromCallable(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String password =  new String(KS.get(context,wallet.address));
                return password;
            } else {

                    throw new ServiceErrorException(ServiceErrorException.KEY_STORE_ERROR);

            }
        });
	}

    @Override
    public Single<String> getMnemonics(Wallet wallet) {
        return Single.fromCallable(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String mnemonics =  new String(KS.get(context,MNEMONICS_TAG + wallet.address));
                return mnemonics;
            } else {
                throw new ServiceErrorException(ServiceErrorException.KEY_STORE_ERROR);
            }
        });
    }


    @Override
	public Completable setPassword(Wallet wallet, String password) {
        return Completable.fromAction(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                 KS.put(context, wallet.address, password);

            } else {
                    throw new ServiceErrorException(ServiceErrorException.KEY_STORE_ERROR);
            }
        });
	}

    @Override
    public Completable setMnemonics(Wallet wallet, String mnemonics) {
        return Completable.fromAction(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                KS.put(context, MNEMONICS_TAG + wallet.address, mnemonics);

            } else {
                throw new ServiceErrorException(ServiceErrorException.KEY_STORE_ERROR);
            }
        });
    }


    //产生 随机  password
    @Override
    public Single<String> generatePassword() {
        return Single.fromCallable(() -> {
            byte bytes[] = new byte[256];
            SecureRandom random = new SecureRandom();
            random.nextBytes(bytes);
//			没做password  保存之前， 就先用123 作为 password
//            return "123";
			return new String(bytes);
        });
    }
}
