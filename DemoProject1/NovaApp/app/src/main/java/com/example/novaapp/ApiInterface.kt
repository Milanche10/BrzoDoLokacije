package com.example.novaapp

import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {

    @GET(
        "user/profile"
    )
    fun getData(): Call<List<MyDataItem>>

    @FormUrlEncoded
    @PUT("user/update/{id}")
    fun updateData(
        @Path("id") user_id: Int,
        @Field("email") email: String,
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("password") password: String
    ): Call<MyDataItem>


    @DELETE("{email}")
    fun deleteData(@Path("email") email: String): Call<Unit>


}