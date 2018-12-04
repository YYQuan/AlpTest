package com.alphawizard.hdwallet.alphahdwallet.service;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;

import org.web3j.crypto.ECKeyPair;

import java.math.BigInteger;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface AccountKeystoreService {
	/**
	 * Create account in keystore
	 * @param password account password
	 * @return new {@link Wallet}
	 */
	Single<Wallet> createAccount(String password);


	Single<Wallet> createMnemonicsAccount(String  mnemonics,String password);

    Single<String> generateMnemonics();

	/**
	 * Include new existing keystore
	 * @param store store to include
	 * @param password store password
	 * @return included {@link Wallet} if success
	 */
	Single<Wallet> importKeystore(String store, String password, String newPassword);
    Single<Wallet> importPrivateKey(String privateKey, String newPassword);
	Single<Wallet>  importMnenonics(String  mnenonics,String password);


	/**
	 * Export wallet to keystore
	 * @param wallet wallet to export
	 * @param password password from wallet
	 * @param newPassword new password to store
	 * @return store data
	 */
	Single<String> exportAccount(Wallet wallet, String password, String newPassword);

	/**
	 * Delete account from keystore
	 * @param address account address
	 * @param password account password
	 */
	Completable deleteAccount(String address, String password);

	/**
	 * Sign transaction
	 * @param signer {@link Wallet}
	 * @param signerPassword password from {@link Wallet}
	 * @param toAddress transaction destination address
	 * @param gasPrice  wei
	 * @param nonce
	 * @return sign data
	 */
	Single<byte[]> signTransaction(
            Wallet signer,
            String signerPassword,
            String toAddress,
            BigInteger amount,
            BigInteger gasPrice,
            BigInteger gasLimit,
            long nonce,
            byte[] data,
            long chainId);

//	Single<byte[]> signPerson(
//			Wallet signer,
//			String  password,
//			byte[] data);


	/**
	 * Check if there is an address in the keystore
	 * @param address {@link Wallet} address
	 */
	boolean hasAccount(String address);

	/**
	 * Return all {@link Wallet} from keystore
	 * @return wallets
	 */
	Single<Wallet[]> fetchAccounts();

	Single<String>  getPrivateKeyFromKeystore(String keystore,String password);

}
