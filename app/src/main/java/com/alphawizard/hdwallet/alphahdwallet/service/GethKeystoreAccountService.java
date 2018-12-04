package com.alphawizard.hdwallet.alphahdwallet.service;



import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.WalletExistException;
import com.alphawizard.hdwallet.common.base.ViewModule.entity.ServiceException;
import com.alphawizard.hdwallet.common.util.Log;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.ethereum.geth.Geth;
import org.ethereum.geth.KeyStore;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.AdminFactory;
import org.web3j.protocol.http.HttpService;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.List;

import io.github.novacrypto.bip32.ExtendedPrivateKey;
import io.github.novacrypto.bip32.networks.Bitcoin;
import io.github.novacrypto.bip39.MnemonicGenerator;
import io.github.novacrypto.bip39.SeedCalculator;
import io.github.novacrypto.bip39.Words;
import io.github.novacrypto.bip39.wordlists.English;
import io.github.novacrypto.bip44.AddressIndex;
import io.github.novacrypto.bip44.BIP44;
import io.reactivex.schedulers.Schedulers;

import org.ethereum.geth.Accounts;
import org.ethereum.geth.Address;
import org.ethereum.geth.BigInt;
import org.ethereum.geth.Geth;
import org.ethereum.geth.KeyStore;
import org.ethereum.geth.Transaction;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletFile;

import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.AdminFactory;
import org.web3j.protocol.admin.JsonRpc2_0Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalListAccounts;
import org.web3j.protocol.http.HttpService;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.schedulers.Schedulers;

import static org.web3j.crypto.Wallet.create;


public class GethKeystoreAccountService implements AccountKeystoreService {
    private static final int PRIVATE_KEY_RADIX = 16;
    /**
     * CPU/Memory cost parameter. Must be larger than 1, a power of 2 and less than 2^(128 * r / 8).
     */
    private static final int N = 1 << 9;
    /**
     * Parallelization parameter. Must be a positive integer less than or equal to Integer.MAX_VALUE / (128 * r * 8).
     */
    private static final int P = 1;

    private final KeyStore keyStore;

    public GethKeystoreAccountService(File keyStoreFile) {
        keyStore = new KeyStore(keyStoreFile.getAbsolutePath(), Geth.LightScryptN, Geth.LightScryptP);
    }

    public GethKeystoreAccountService(KeyStore keyStore) {
        this.keyStore = keyStore;
    }

    @Override
    public Single<Wallet> createAccount(String password) {
        return Single.fromCallable(() -> {
            return new Wallet(
                    keyStore.newAccount(password).getAddress().getHex().toLowerCase());
//            Wallet wallet = new Wallet(createNewAccount());
//            return  wallet;
        })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Wallet> createMnemonicsAccount(String  mnemonics,String password){
        String  privateKey = getPrivateKey(mnemonics);
        return importPrivateKey(privateKey,password);
    }


    /**
     * 生成一组随机的助记词
     */
    public Single<String> generateMnemonics() {

        return Single.fromCallable(()->{
            StringBuilder sb = new StringBuilder();
            byte[] entropy = new byte[Words.TWELVE.byteLength()];
            new SecureRandom().nextBytes(entropy);
            new MnemonicGenerator(English.INSTANCE)
                    .createMnemonic(entropy, sb::append);
            return sb.toString();
        })
                .subscribeOn(Schedulers.io());
    }

    /**
     * 生成KeyPair , 用于创建钱包
     */
    public String getPrivateKey(String mnemonics) {
//        mnemonics = "puppy puppy menu menu menu menu menu menu menu menu menu menu";
        // 1. we just need eth wallet for now
        AddressIndex addressIndex = BIP44
                .m()
                .purpose44()
                .coinType(60)
                .account(0)
                .external()
//                这个address就是 该助记词对应的hd seed 的第几个私钥
                .address(0);
        // 2. calculate seed from mnemonics , then get master/root key ; Note that the bip39 passphrase we set "" for common
        ExtendedPrivateKey rootKey = ExtendedPrivateKey.fromSeed(new SeedCalculator().calculateSeed(mnemonics, ""), Bitcoin.MAIN_NET);
//        Logger.i("mnemonics:" + mnemonics);
        String extendedBase58 = rootKey.extendedBase58();
//        Logger.i("extendedBase58:" + extendedBase58);

        // 3. get child private key deriving from master/root key
        ExtendedPrivateKey childPrivateKey = rootKey.derive(addressIndex, AddressIndex.DERIVATION);
        String childExtendedBase58 = childPrivateKey.extendedBase58();
//        Logger.i("childExtendedBase58:" + childExtendedBase58);
//        Logger.i("childExtendedBase58:" + childExtendedBase58);

        // 4. get key pair
        byte[] privateKeyBytes = childPrivateKey.getKey();
        ECKeyPair keyPair = ECKeyPair.create(privateKeyBytes);


        // we 've gotten what we need
        String privateKey = childPrivateKey.getPrivateKey();
        Log.d("privateKey:" + privateKey);
        String publicKey = childPrivateKey.neuter().getPublicKey();
        String address = Keys.getAddress(keyPair);




        return privateKey;
//        Logger.i("privateKey:" + privateKey);
//        Logger.i("publicKey:" + publicKey);
//        Logger.i("address:" + Constant.PREFIX_16 + address);

//        return keyPair;
    }


//    http://54.219.176.229:8545/   一哥  提供的节点地址
    private static Admin admin = AdminFactory.build(new HttpService("http://54.219.176.229:8545/")) ;
//    private static Admin admin =new JsonRpc2_0Admin(new HttpService("http://54.219.176.229:8545"));
//            Admin.build(new HttpService("https://mainnet.infura.io/llyrtzQ3YhkdESt2Fzrk"));


    /**
     * web3j 创建账号
     */
    private static String createNewAccount(String  password) {
        String address = "";
        try {
            NewAccountIdentifier newAccountIdentifier = admin.personalNewAccount(password).send();
            address = newAccountIdentifier.getAccountId();
            System.out.println("new account address " + address);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return address;

    }

    /**
     * 使用web3  来获取账号列表
     */
    private static void getAccountList() {
        try {
            PersonalListAccounts personalListAccounts = admin.personalListAccounts().send();
            List<String> addressList;
            addressList = personalListAccounts.getAccountIds();
            System.out.println("account size " + addressList.size());
            for (String address : addressList) {
                System.out.println("account   : " +address);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String getAddressFromKeystore(String store){
        String addressStr ="\"address\"";
        int addressIndex = store.indexOf(addressStr);
        int crypToIndex = store.indexOf("\"crypto\"");
        int idIndex = store.indexOf("\"id\"");
        int clientIndex = store.indexOf("\"client\"");
        int versionIndex = store.indexOf("\"version\"");

        String subStore = store.substring(addressIndex+addressStr.length());
//        String subStore = store.substring(addressIndex+addressStr.length(),crypToIndex);

        int firstIndex = subStore.indexOf("\"");
        subStore = subStore.substring(firstIndex+1);
        firstIndex = subStore.indexOf("\"");
        String address = subStore.substring(0,firstIndex);

        return address;
    }

    /**
     * 判断 import的地址 在geth中是否已经存在了
     * @param address    eth地址
     * @param accounts      geth 管理的账户集合
     * @return
     */
    private  boolean  isEnableImportKeyStore(String address, Accounts accounts){

        address = "0x"+address;

        int len = (int) accounts.size();
        for (int i = 0; i < len; i++) {
            org.ethereum.geth.Account gethAccount = null;
            try {
                gethAccount = accounts.get(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
//                result[i] = new Wallet(gethAccount.getAddress().getHex().toLowerCase());
            if(gethAccount.getAddress().getHex().equalsIgnoreCase(address)){
                return false;
            }
        }
        return true;
    }

    @Override
    public Single<Wallet> importKeystore(String store, String password, String newPassword) {
        return Single.fromCallable(() -> {

            if(!isEnableImportKeyStore(getAddressFromKeystore(store),keyStore.getAccounts())){
                throw  new WalletExistException();
            }

            org.ethereum.geth.Account account = keyStore
                    .importKey(store.getBytes(Charset.forName("UTF-8")), password, newPassword);

            return new Wallet(account.getAddress().getHex().toLowerCase());
        })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Wallet> importPrivateKey(String privateKey, String newPassword) {
        return Single.fromCallable(() -> {
            BigInteger key = new BigInteger(privateKey, PRIVATE_KEY_RADIX);
            ECKeyPair keypair = ECKeyPair.create(key);
            WalletFile walletFile = create(newPassword, keypair, N, P);
            if(!isEnableImportKeyStore(walletFile.getAddress(),keyStore.getAccounts())){
                throw  new  WalletExistException();
            }
            return new ObjectMapper().writeValueAsString(walletFile);
        }).compose(upstream -> GethKeystoreAccountService.this.importKeystore(upstream.blockingGet(), newPassword, newPassword));
    }


    public Single<Wallet>  importMnenonics(String  mnenonics,String password){
//        String  privateKey = getPrivateKey(mnemonics);
        return importPrivateKey(getPrivateKey(mnenonics),password);
    }



    @Override
    public Single<String> exportAccount(Wallet wallet, String password, String newPassword) {
        return Single
                .fromCallable(() -> findAccount(wallet.address))
                .flatMap(account1 -> Single.fromCallable(()
                        -> new String(keyStore.exportKey(account1, password, newPassword))))
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable deleteAccount(String address, String password) {

        return Single.fromCallable(() -> findAccount(address))
                .flatMapCompletable(account -> Completable.fromAction(
                        () -> keyStore.deleteAccount(account, password)))
                .subscribeOn(Schedulers.io());
    }



//    @Override
//    public Single<byte[]> signPerson(
//            String keystore,
//            Wallet signer,
//            String  password,
//            byte[] data){
//
//        return Single.fromCallable(() -> {
//
////            org.ethereum.geth.Account gethAccount = findAccount(signer.address);
////            keyStore.unlock(gethAccount, password);
////
////            byte[]  resultBytes = keyStore.signHash(gethAccount.getAddress(),data);
////            keyStore.lock(gethAccount.getAddress());
////            return resultBytes;
//            ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
//            byte[]  resultBytes   =  new byte[32] ;
//            try {
//                WalletFile walletFile = objectMapper.readValue(keystore, WalletFile.class);
//                ECKeyPair ecKeyPair = null;
//                ecKeyPair = org.web3j.crypto.Wallet.decrypt(password, walletFile);
//                Sign.SignatureData  signatureData  = Sign.signMessage(data,ecKeyPair);
//
//
//            } catch (CipherException e) {
//                if ("Invalid password provided".equals(e.getMessage())) {
//                    System.out.println("密码错误");
//                }
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            }).subscribeOn(Schedulers.io());
//    }

    @Override
    public Single<byte[]> signTransaction(Wallet signer, String signerPassword, String toAddress, BigInteger amount, BigInteger gasPrice, BigInteger gasLimit, long nonce, byte[] data, long chainId) {
        return Single.fromCallable(() -> {
            BigInt value = new BigInt(0);
            value.setString(amount.toString(), 10);

            BigInt gasPriceBI = new BigInt(0);
            gasPriceBI.setString(gasPrice.toString(), 10);

            BigInt gasLimitBI = new BigInt(0);
            gasLimitBI.setString(gasLimit.toString(), 10);

            Transaction tx = new Transaction(
                    nonce,
                    new Address(toAddress),
                    value,
                    gasLimitBI,
                    gasPriceBI,
                    data);

            BigInt chain = new BigInt(chainId); // Chain identifier of the main net
            org.ethereum.geth.Account gethAccount = findAccount(signer.address);
            keyStore.unlock(gethAccount, signerPassword);
            Transaction signed = keyStore.signTx(gethAccount, tx, chain);
            keyStore.lock(gethAccount.getAddress());

            return signed.encodeRLP();
        })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public boolean hasAccount(String address) {
        return keyStore.hasAddress(new Address(address));
    }

    @Override
    public Single<Wallet[]> fetchAccounts() {
        return Single.fromCallable(() -> {
//            这里用geth的 getAccounts  api 返回的是当前钱包里所有的账户，
            Accounts accounts = keyStore.getAccounts();
            int len = (int) accounts.size();
            Wallet[] result = new Wallet[len];

            for (int i = 0; i < len; i++) {
                org.ethereum.geth.Account gethAccount = accounts.get(i);
                result[i] = new Wallet(gethAccount.getAddress().getHex().toLowerCase());
            }
            return result;
        })
                .subscribeOn(Schedulers.io());
    }

    private org.ethereum.geth.Account findAccount(String address) throws ServiceException {
        Accounts accounts = keyStore.getAccounts();
        int len = (int) accounts.size();
        for (int i = 0; i < len; i++) {
            try {
//                android.util.Log.d("ACCOUNT_FIND", "Address: " + accounts.get(i).getAddress().getHex());
                if (accounts.get(i).getAddress().getHex().equalsIgnoreCase(address)) {
                    return accounts.get(i);
                }
            } catch (Exception ex) {
                /* Quietly: interest only result, maybe next is ok. */
            }
        }
        throw new ServiceException("Wallet with address: " + address + " not found");
    }


    /**
     * 通过keystore  获取私钥
     */
    public Single<String>  getPrivateKeyFromKeystore(String keystore,String password){

        return Single.fromCallable(()-> {
            String privateKey = null;
            ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
            try {
                WalletFile walletFile = objectMapper.readValue(keystore, WalletFile.class);
                ECKeyPair ecKeyPair = null;
                ecKeyPair = org.web3j.crypto.Wallet.decrypt(password, walletFile);
                privateKey = ecKeyPair.getPrivateKey().toString(16);
                System.out.println(privateKey);
            } catch (CipherException e) {
                if ("Invalid password provided".equals(e.getMessage())) {
                    System.out.println("密码错误");
                }
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return privateKey;
        });
    }
}
