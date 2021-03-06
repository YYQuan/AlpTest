package com.example.web3lib;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.google.gson.Gson;

import java.math.BigInteger;
import java.util.Base64;

import trust.core.entity.Address;
import trust.core.entity.Message;
import trust.core.entity.Transaction;
import trust.core.entity.TypedData;
import trust.core.util.Hex;

public class SignCallbackJSInterface {

    private final WebView webView;
    @NonNull
    private final OnSignTransactionListener onSignTransactionListener;
    @NonNull
    private final OnSignMessageListener onSignMessageListener;
    @NonNull
    private final OnSignPersonalMessageListener onSignPersonalMessageListener;
    @NonNull
    private final OnSignTypedMessageListener onSignTypedMessageListener;

    @NonNull
    private final OnOpenInfoChangeListener onOpenInfoChangeListener;

    @NonNull
    private final OnLanguageChangeListener onLanguageChangeListener;

    public SignCallbackJSInterface(
            WebView webView,
            @NonNull OnSignTransactionListener onSignTransactionListener,
            @NonNull OnSignMessageListener onSignMessageListener,
            @NonNull OnSignPersonalMessageListener onSignPersonalMessageListener,
            @NonNull OnSignTypedMessageListener onSignTypedMessageListener,
            @NonNull OnLanguageChangeListener onLanguageChangeListener,
            @NonNull OnOpenInfoChangeListener onOpenInfoChangeListener) {
        this.webView = webView;
        this.onSignTransactionListener = onSignTransactionListener;
        this.onSignMessageListener = onSignMessageListener;
        this.onSignPersonalMessageListener = onSignPersonalMessageListener;
        this.onSignTypedMessageListener = onSignTypedMessageListener;
        this.onLanguageChangeListener = onLanguageChangeListener;
        this.onOpenInfoChangeListener =onOpenInfoChangeListener;
    }

    @JavascriptInterface
    public void signTransaction(
            int callbackId,
            String recipient,
            String value,
            String nonce,
            String gasLimit,
            String gasPrice,
            String payload)
    {
        Transaction transaction = new Transaction(
                TextUtils.isEmpty(recipient) ? Address.EMPTY : new Address(recipient),
                null,
                Hex.hexToBigInteger(value),
                Hex.hexToBigInteger(gasPrice, BigInteger.ZERO),
                Hex.hexToLong(gasLimit, 0),
                Hex.hexToLong(nonce, -1),
                payload,
                callbackId);
        Log.d("YYQ","signing a  signTransaction   js injector");
        onSignTransactionListener.onSignTransaction(transaction);

    }

    @JavascriptInterface
    public void signMessage(int callbackId, String data) {
        webView.post(() -> onSignMessageListener.onSignMessage(new Message<>(data, getUrl(), callbackId)));
    }

    @JavascriptInterface
    public void signPersonalMessage(int callbackId, String data) {
        webView.post(() -> onSignPersonalMessageListener.onSignPersonalMessage(new Message<>(data, getUrl(), callbackId)));
    }

    @JavascriptInterface
    public void signTypedMessage(int callbackId, String data) {
        webView.post(() -> {
            TrustProviderTypedData[] rawData = new Gson().fromJson(data, TrustProviderTypedData[].class);
            int len = rawData.length;
            TypedData[] typedData = new TypedData[len];
            for (int i = 0; i < len; i++) {
                typedData[i] = new TypedData(rawData[i].name, rawData[i].type, rawData[i].value);
            }
            onSignTypedMessageListener.onSignTypedMessage(new Message<>(typedData, getUrl(), callbackId));
        });
    }

    @JavascriptInterface
    public void changeLanguage(String params) {
        Log.d("YYQ","  js    language "+params);
        onLanguageChangeListener.onLanguageChange(params);
//        webView.post(() -> onSignPersonalMessageListener.onSignPersonalMessage(new Message<>(data, getUrl(), callbackId)));
    }


    @JavascriptInterface
    public void openURLOnSystermBrowser(String params) {
        Log.d("YYQ","  js    openURLOnSystermBrowser "+params);
        onOpenInfoChangeListener.OnOpenInfoChange(params);
//        onLanguageChangeListener.onLanguageChange(params);
//        webView.post(() -> onSignPersonalMessageListener.onSignPersonalMessage(new Message<>(data, getUrl(), callbackId)));
    }


    private String getUrl() {
        return webView == null ? "" : webView.getUrl();
    }

    private static class TrustProviderTypedData {
        public String name;
        public String type;
        public Object value;
    }
}
