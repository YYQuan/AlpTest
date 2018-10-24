package com.alphawizard.hdwallet.alphahdwallet.service;


import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Transaction;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;

public interface TickerService {

//    Observable<Response<String>> fetchTickerPrice( );
        String fetchTickerPrice( );

        Call<Transaction> fetchTransactions(String address);
}
