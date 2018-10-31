package com.alphawizard.hdwallet.alphahdwallet.interact;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.PasswordStore;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepository;
import com.alphawizard.hdwallet.alphahdwallet.db.Repositor.WalletRepositoryType;
import com.alphawizard.hdwallet.alphahdwallet.service.AccountKeystoreService;
import com.alphawizard.hdwallet.alphahdwallet.utils.BalanceUtils;
import com.alphawizard.hdwallet.common.base.ViewModule.entity.ServiceException;
import com.alphawizard.hdwallet.common.util.Log;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SendTransactionInteract {

    WalletRepositoryType walletRepository;
    PasswordStore passwordStore;

    public SendTransactionInteract(WalletRepositoryType walletRepository,
                                   PasswordStore passwordStore) {
        this.walletRepository =  walletRepository;
        this.passwordStore = passwordStore;
    }

//    Single<byte[]> signTransaction(
//            Wallet signer,
//            String signerPassword,
//            String toAddress,
//            BigInteger amount,
//            BigInteger gasPrice,
//            BigInteger gasLimit,
//            long nonce,
//            byte[] data,
//            long chainId);

    public Single<String>  sendTransaction(String toAddress,  BigInteger amount,BigInteger gasPrice,long gasLimit,long nonce,byte[] data){
        long chainId  =4L ;//rinkeby 网络
        BigInteger gasLimitMin = BigInteger.valueOf(2100L);
        BigInteger gasLimitBigInteger = BigInteger.valueOf(50000).add(gasLimitMin);

        Wallet wallet =new Wallet(toAddress);


        final Web3j web3j = Web3jFactory.build(new HttpService("https://rinkeby.infura.io/llyrtzQ3YhkdESt2Fzrk"));

        return walletRepository.signTransaction(wallet,"123", toAddress, amount, gasPrice, gasLimitBigInteger, nonce, data, chainId)
                .flatMap(signedMessage -> Single.fromCallable( () -> {
                    EthSendTransaction raw = web3j
                            .ethSendRawTransaction(Numeric.toHexString(signedMessage))
                            .send();
                    if (raw.hasError()) {
                        throw new ServiceException(raw.getError().getMessage());
                    }
                    return raw.getTransactionHash();
                }))
                .subscribeOn(Schedulers.io());
//        return passwordStore.getPassword(wallet)
//                .flatMap(password ->walletRepository.signTransaction(wallet, password, toAddress, amount, gasPrice, gasLimitBigInteger, nonce, data, chainId))
//                .flatMap(signedMessage -> Single.fromCallable( () -> {
//                    EthSendTransaction raw = web3j
//                            .ethSendRawTransaction(Numeric.toHexString(signedMessage))
//                            .send();
//                    if (raw.hasError()) {
//                        throw new ServiceException(raw.getError().getMessage());
//                    }
//                    return raw.getTransactionHash();
//                }))
//                .subscribeOn(Schedulers.io());

/*
        return Single.fromCallable(() -> {
//      ethGetTransactionCount  是    获取已完成区块中的该账号最后的nonce
//      ethereum是根据nonce按顺序的把交易加入池中的，比如说最后一个nonce是121,如果发送一个nonce为123的交易，那么节点将会拒绝该交易入池（池都没入，那肯定没打包啦。）
//      get the next available nonce   官网对于  ethGetTransactionCount()的注释
            EthGetTransactionCount ethGetTransactionCount = web3j
                    .ethGetTransactionCount(wallet.address, DefaultBlockParameterName.LATEST)
                    .send();

            return ethGetTransactionCount.getTransactionCount();
        })
                .flatMap(nonce1 -> passwordStore.getPassword(wallet)
                        .flatMap(password -> {
                            Log.d("transaction  account password  is " + password );
                            return walletRepository.signTransaction(wallet, password, toAddress, amount, gasPrice, gasLimitBigInteger, nonce, data, chainId);
                        }))
                .flatMap(signedMessage -> Single.fromCallable( () -> {
                    EthSendTransaction raw = web3j
                            .ethSendRawTransaction(Numeric.toHexString(signedMessage))
                            .send();
                    if (raw.hasError()) {
                        throw new ServiceException(raw.getError().getMessage());
                    }
                    return raw.getTransactionHash();
                }))
                .subscribeOn(Schedulers.io());
//        service.signTransaction()
*/
    }

//    返回该交易的hash
    public Single<String>  sendTransaction(String toAddress, String amount ){
//        Wallet wallet = walletRepository.getDefaultWallet().
        Wallet wallet = walletRepository.getDefaultWallet().blockingGet();


        BigInteger subunitAmount = BalanceUtils.baseToSubunit(amount, 18);
        BigInteger gasPriceMin = BigInteger.valueOf(1000000000L); //long GAS_PRICE_MIN = 1000000000L;
        int gasPriceMinGwei = BalanceUtils.weiToGweiBI(gasPriceMin).intValue();
        BigInteger gasPrice = BalanceUtils.gweiToWei(BigDecimal.valueOf(55 + gasPriceMinGwei));
        BigInteger gasLimitMin = BigInteger.valueOf(2100L);
        BigInteger gasLimit = BigInteger.valueOf(50000).add(gasLimitMin);
        byte[] data = null;
        long chainId  =4L ;//rinkeby 网络




        final Web3j web3j = Web3jFactory.build(new HttpService("https://rinkeby.infura.io/llyrtzQ3YhkdESt2Fzrk"));
        return Single.fromCallable(() -> {
//      ethGetTransactionCount  是    获取已完成区块中的该账号最后的nonce
//      ethereum是根据nonce按顺序的把交易加入池中的，比如说最后一个nonce是121,如果发送一个nonce为123的交易，那么节点将会拒绝该交易入池（池都没入，那肯定没打包啦。）
//      get the next available nonce   官网对于  ethGetTransactionCount()的注释
                EthGetTransactionCount ethGetTransactionCount = web3j
                    .ethGetTransactionCount(wallet.address, DefaultBlockParameterName.LATEST)
                    .send();

                return ethGetTransactionCount.getTransactionCount();
        })
                .flatMap(new Function<BigInteger, SingleSource<? extends byte[]>>() {
                    @Override
                    public SingleSource<? extends byte[]> apply(BigInteger nonce) throws Exception {
                        return passwordStore.getPassword(wallet)
                                .flatMap(new Function<String, SingleSource<? extends byte[]>>() {
                                    @Override
                                    public SingleSource<? extends byte[]> apply(String password) throws Exception {
                                        Log.d("transaction  account password  is " + password );
                                        return walletRepository.signTransaction(wallet, password, toAddress, subunitAmount, gasPrice, gasLimit, nonce.longValue(), data, chainId);
                                    }
                                });

                    }})
                .flatMap(signedMessage -> Single.fromCallable( () -> {
                    EthSendTransaction raw = web3j
                            .ethSendRawTransaction(Numeric.toHexString(signedMessage))
                            .send();
                    if (raw.hasError()) {
                        throw new ServiceException(raw.getError().getMessage());
                    }
                    return raw.getTransactionHash();
                })).subscribeOn(Schedulers.io());
//        service.signTransaction()
    }



//    Single<byte[]> signTransaction(
//            Wallet signer,
//            String signerPassword,
//            String toAddress,
//            BigInteger amount,
//            BigInteger gasPrice,
//            BigInteger gasLimit,
//            long nonce,
//            byte[] data,
//            long chainId);

//    wallet, password, toAddress, subunitAmount, gasPrice, gasLimit, nonce.longValue(), data, chainId
    //    返回该交易的hash
    public Single<String>  sendTransaction(String toAddress,BigInteger amount , BigInteger gasPrice, BigInteger gasLimit,long nonce,String dataString,long chainId){
//        Wallet wallet = walletRepository.getDefaultWallet().
        Wallet wallet = walletRepository.getDefaultWallet().blockingGet();
        byte[] data =string2ByteArrays(dataString);
//        byte[] data = {
//                0,
//                0xd,0x7,0x2,0xc,0x6,0x3,0x9
//                ,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0
//                ,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0
//                ,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0
//                ,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0
//                ,0x5,0xa
//                ,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0
//                ,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0
//                ,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0
//                ,0x6,0x4,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0
//                ,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0
//                ,0x3,0x1,0xa,0xf,0x8,0xe,0xa,0xe,0x8,0x0,0x5,0x5,0x8,0x4,0x5,0xc,0xb,0x4
//                ,0x1,0xc,0xc,0x7,0xd,0x4,0x8,0x7,0xf,0xb,0xb,0xb,0x5,0xa,0x9,0x2,0xc,0x9
//                ,0x8,0xb,0x0,0xe,0xd,0x6,0x8,0x6,0x1,0x6,0x6,0xe,0xc,0x4,0xd,0x4,0xc,0xf
//                ,0xd,0xc,0x4,0x4,0x7,0xa,0x5,0x8,0xb,0x1,0xa,0x4,0xf,0x9,0xe,0xc
//                ,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
//                ,0xa
//                ,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
//                ,0x4,0x1,0xb,0x2,0x0,0xe,0x3,0xe,0xa,0xc,0x3,0xb,0xb,0xe,0xb,0xb,0x1,0xe
//                ,0xe,0x4,0x4,0x9,0x4,0xe,0x7,0xd,0xa,0x0,0xc,0x7,0xb,0x3,0xb,0xf,0x1,0x6
//                ,0x9,0x7,0x3,0x8,0xb,0xf,0x0,0x0,0xb,0x4,0x9,0xb,0xf,0x2,0x7,0x8,0xc,0x3
//                ,0x1,0xb,0x5,0x2,0x5,0xe,0xe,0xc,0x2,0x1,0x6,0xb,0x3,0x1,0x7,0xd,0xd,0x3,0x6,0xb,0x1,0xd,0x2,0x7,0x9,0xe,0xb,0xf,0x1,0x4,0xe,0x2,0xe,0x3,0xf,0x7,0x0,0x8,0x8,0x2,0xc,0x9,0x5,0xa,0x2,0x2,0x8,0xa,0xc,0x7,0xc,0x8,0xd,0x2,0xa,0x3,0xb,0x3,0xf,0xa,0x1,0x0,0xf,0x1,0x0,0x5,0xa,0x2,0x1,0x6,0x7,0x5,0x6,0x5,0x2,0xb,0x1,0xb
//
//                ,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
//        };
        final Web3j web3j = Web3jFactory.build(new HttpService("https://rinkeby.infura.io/llyrtzQ3YhkdESt2Fzrk"));

        return Single.fromCallable(() -> {
            EthGetTransactionCount ethGetTransactionCount = web3j
                    .ethGetTransactionCount(wallet.address, DefaultBlockParameterName.LATEST)
                    .send();

            return ethGetTransactionCount.getTransactionCount();
        })
                .flatMap(nonce1 -> passwordStore.getPassword(wallet)
                        .flatMap(new Function<String, SingleSource<? extends byte[]>>() {
                            @Override
                            public SingleSource<? extends byte[]> apply(String password) throws Exception {
                                Log.d("transaction  account password  is " + password );
                                return walletRepository.signTransaction(wallet, password, toAddress, amount, gasPrice, gasLimit, nonce1.longValue(), data, chainId);
                            }
                        }))
                .flatMap(signedMessage -> Single.fromCallable( () -> {
                    EthSendTransaction raw = web3j
                            .ethSendRawTransaction(Numeric.toHexString(signedMessage))
                            .send();
                    if (raw.hasError()) {
                        throw new ServiceException(raw.getError().getMessage());
                    }
                    return raw.getTransactionHash();
                })).subscribeOn(Schedulers.io());
    }


    private byte[]  string2ByteArrays(String   str ){
        byte[] bytes   =str.getBytes();
        byte[] bytes1 =new   byte[((bytes.length-2)/2) ];
        for(int i = 1,j=0;j<bytes1.length;i++,j++){
            int a=0,b = 0;

            if (bytes[2 * i ] >= 'a') {
                a = (int) (bytes[2 * i ] - 'a' + 10);
            } else if(bytes[2 * i ] >= '0'){
                    a = (int) (bytes[2 * i ] - '0');
            }
            if (bytes[2 * i+1] >= 'a') {
                b = (int) (bytes[2 * i+1] - 'a' + 10);
            } else if(bytes[2 * i+1] >= '0'){
                    b = (int) (bytes[2 * i+1] - '0');
            }


            if(((16 *a) +b)<0){
                Log.d("  <0");
            }

            bytes1[j] =(byte) ( (int)((16 *a) +b)& 0xff);
        }
        return bytes1;
    }
}
