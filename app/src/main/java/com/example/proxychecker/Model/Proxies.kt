package com.example.proxychecker.Model


data class Proxies(
    val data: List<Proxy?>
)


data class Proxy(
    val ip: String?,
    val port: String?,
    val anonymityLevel: String?,
    val asn: String?,
    val city: String?,
    val country: String?,
    val google: Boolean?,
    val isp: String?
)
