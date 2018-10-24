package com.alphawizard.hdwallet.alphahdwallet.service;

import com.alphawizard.hdwallet.common.util.Log;
import com.google.gson.Gson;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;

public class EthTickerService implements TickerService {

    private static final String API_URL = "http://etherscan.io/";

    private final OkHttpClient httpClient;
    private final Gson gson;
    private ApiClient apiClient;

    public EthTickerService(
            OkHttpClient httpClient,
            Gson gson) {
        this.httpClient = httpClient;
        this.gson = gson;
        buildApiClient(API_URL);
    }

    private void buildApiClient(String baseUrl) {
//        apiClient = new Retrofit.Builder().baseUrl(baseUrl)  //设置域名
//                .addConverterFactory(SimpleXmlConverterFactory.create()) //添加数据解析器,需要添加对应依赖
//                .build()
//                .create(ApiClient.class);

//        apiClient = new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .client(httpClient)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build()
//                .create(ApiClient.class);



    }

    @Override
    public  String  fetchTickerPrice() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(API_URL)
                .build();
        okhttp3.Call call =  okHttpClient.newCall(request);
        okhttp3.Response response = null;
        String result = null;
        try {
            response = call.execute();
            result = response.body().string();
            Log.d(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public interface ApiClient {
        @GET("/")
        Observable<Response<String>> fetchTickerPrice();

    }


}
