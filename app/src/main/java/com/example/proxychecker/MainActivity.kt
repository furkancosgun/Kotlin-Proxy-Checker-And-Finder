package com.example.proxychecker

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proxychecker.DAO.ProxyDAOInterface
import com.example.proxychecker.DAO.SortBy
import com.example.proxychecker.DAO.SortType
import com.example.proxychecker.Model.Proxies
import com.example.proxychecker.Util.APIUtil
import com.example.proxychecker.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val TAG = "XFC"
    private val work = "Çalışıyor"
    private val notWork = "Çalışmıyor"

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Retrofit Interface
        val pdi = APIUtil.getProxyDaoInterface()

        binding.btnChk.setOnClickListener {
            binding.txtResult.text = ""
            val proxy = binding.edtProxy.editText?.text.toString()
            val port = binding.edtPort.editText?.text.toString()
            if (proxy.isNotEmpty() && port.isNotEmpty()) {
                CoroutineScope(Dispatchers.Unconfined).run {
                    doTestProxy(proxy, port) {
                        if (it) {
                            binding.txtResult.text = work
                            binding.txtResult.setTextColor(Color.parseColor("#00ff00"))
                        } else {
                            binding.txtResult.text = notWork
                            binding.txtResult.setTextColor(Color.parseColor("#ff0000"))
                        }
                        binding.txtResult.visibility = View.VISIBLE
                    }
                }
            }
        }
        binding.btnGetIp.setOnClickListener {
            val list = mutableListOf<String>()
            if (binding.edtSize.editText?.text.toString().isNotEmpty()) {
                val size = binding.edtSize.editText?.text.toString().toInt()
                getAllProxies(pdi, size) { res ->
                    res.body().data.forEach { body ->
                        CoroutineScope(Dispatchers.IO).run {
                            body?.let { item ->
                                doTestProxy(item.ip.toString(), item.port.toString()) {

                                    //isWork?
                                    if (it) {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "${item.ip} $work",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        list.add(item.ip + " : " + item.port)
                                    } else {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "${item.ip} $notWork",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                    binding.listView.adapter =
                                        ArrayAdapter(
                                            this@MainActivity,
                                            android.R.layout.simple_list_item_1,
                                            list
                                        )
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    fun getAllProxies(pdi: ProxyDAOInterface, size: Int, completion: (Response<Proxies>) -> Unit) {

        pdi.getAllProxies(size, 1, SortBy.LASTCHECKED.by, SortType.DESC.type)
            .enqueue(object : Callback<Proxies> {
                override fun onResponse(call: Call<Proxies>?, response: Response<Proxies>?) {
                    Log.d(TAG, response?.headers().toString())
                    response?.let {
                        Log.d(TAG, it.body().data.toString())
                        completion(it)
                    }
                }

                override fun onFailure(call: Call<Proxies>?, t: Throwable?) {
                    t?.printStackTrace()
                    t?.let {
                        it.localizedMessage?.let { it1 -> Log.e(TAG, it1) }
                    }
                }
            })
    }

    fun doTestProxy(proxy: String, port: String, completion: (Boolean) -> Unit) {
        APIUtil.getProxyTestWithDaoInterface(proxy, port).doTest()
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>?, response: Response<String>?) {
                    completion(true)
                }

                override fun onFailure(call: Call<String>?, t: Throwable?) {
                    completion(false)
                }
            })
    }
}