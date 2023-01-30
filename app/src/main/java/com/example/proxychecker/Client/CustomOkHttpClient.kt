package com.example.proxychecker.Client

import okhttp3.OkHttpClient
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.concurrent.TimeUnit

class CustomOkHttpClient {
    companion object {
        fun getOkClient(proxy: String, port: String): OkHttpClient {
            return OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).proxy(
                Proxy(
                    Proxy.Type.HTTP,
                    InetSocketAddress(proxy, port.toInt())
                )
            ).build()
        }
    }
}