package com.example.proxychecker.DAO

import com.example.proxychecker.Model.Proxies
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ProxyDAOInterface {
    @GET("proxy-list")
    fun getAllProxies(
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("sort_by") sort_by: String,
        @Query("sort_type") sort_type: String
    ): Call<Proxies>

    @GET("/")
    fun doTest(): Call<String>
}

enum class SortType(val type: String) {
    ASC("asc"),
    DESC("desc")
}

enum class SortBy(val by: String) {
    LASTCHECKED("lastChecked"),
}