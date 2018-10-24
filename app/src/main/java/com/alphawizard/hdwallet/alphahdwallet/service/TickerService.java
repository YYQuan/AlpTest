package com.alphawizard.hdwallet.alphahdwallet.service;


import io.reactivex.Observable;
import retrofit2.Response;

public interface TickerService {

//    Observable<Response<String>> fetchTickerPrice( );
        String fetchTickerPrice( );
}
