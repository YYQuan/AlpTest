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



    //    返回该交易的hash
    public Single<String>  sendTransaction(String toAddress, String amount ,String contractAddress){
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
}
