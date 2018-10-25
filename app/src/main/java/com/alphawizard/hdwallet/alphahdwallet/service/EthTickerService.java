package com.alphawizard.hdwallet.alphahdwallet.service;

import android.widget.Toast;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Transaction;
import com.alphawizard.hdwallet.common.util.Log;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

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
        Retrofit retrofit = new Retrofit.Builder()
                //使用自定义的mGsonConverterFactory
//                            .addConverterFactory(GsonConverterFactory.create(buildGson()))
                .addConverterFactory(GsonConverterFactory.create())
//                            .baseUrl("http://apis.baidu.com/txapi/")
                .baseUrl("https://rinkeby.etherscan.io/")
                .build();
        apiClient = retrofit.create(ApiClient.class);
    }

    String result = "0.00";
    OkHttpClient okHttpClient = new OkHttpClient();
    Request request = new Request.Builder()
            .url(API_URL)
            .build();
    okhttp3.Call call= okHttpClient.newCall(request);;
    okhttp3.Response response = null;
    @Override
    public  String  fetchTickerPrice() {


//            call = okHttpClient.newCall(request);
        try {
//            response = call.execute();
            response = call.clone().execute();
            if(response!=null) {
                result = response.body().string();
//                call =null;
                Log.d(result);
            }else{
                result = "0.00";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }

    public Call<Transaction> fetchTransactions(String address){


        Retrofit retrofit = new Retrofit.Builder()
                //使用自定义的mGsonConverterFactory
//                            .addConverterFactory(GsonConverterFactory.create(buildGson()))
                .addConverterFactory(GsonConverterFactory.create())
//                            .baseUrl("http://apis.baidu.com/txapi/")
                .baseUrl("https://rinkeby.etherscan.io/")
                .build();
        apiClient = retrofit.create(ApiClient.class);
        apiClient.getTransaction("account", "txlist",address,"desc")
                .enqueue(new Callback<Transaction>() {
                    @Override
                    public void onResponse(Call<Transaction> call, Response<Transaction> response) {
                        Transaction body = response.body();
                        Log.d("body ");
                    }

                    @Override
                    public void onFailure(Call<Transaction> call, Throwable t) {
                        Log.d("fail ");
                    }
                });
        return null;

    }

    public interface ApiClient {
//        @GET("/")
//        Observable<Response<String>> fetchTickerPrice();
        @GET("api")
        Call<Transaction> getTransaction(@Query("module") String module, @Query("action") String action, @Query("address") String address,@Query("sort") String sort);
    }


}
