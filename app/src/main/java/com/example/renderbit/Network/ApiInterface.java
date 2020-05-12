package com.example.renderbit.Network;

import com.example.renderbit.Models.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("orders")
    Call<List<Order>> getOrders();


}
