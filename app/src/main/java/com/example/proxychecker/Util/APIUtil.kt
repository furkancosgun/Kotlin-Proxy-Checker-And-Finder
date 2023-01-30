package com.example.proxychecker.Util

import com.example.proxychecker.Client.CustomOkHttpClient
import com.example.proxychecker.Client.RetrofitClient
import com.example.proxychecker.DAO.ProxyDAOInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIUtil {
    companion object {
        const val baseUrlForData = "https://proxylist.geonode.com/api/"

        fun getProxyDaoInterface(): ProxyDAOInterface {
            return RetrofitClient.getClient(baseUrlForData).create(ProxyDAOInterface::class.java)
        }

        const val testBaseUrl = "http://www.google.com"
        fun getProxyTestWithDaoInterface(proxy: String, port: String): ProxyDAOInterface {
            return Retrofit.Builder().baseUrl(testBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(CustomOkHttpClient.getOkClient(proxy, port))
                .build()
                .create(ProxyDAOInterface::class.java)
        }
    }
}
