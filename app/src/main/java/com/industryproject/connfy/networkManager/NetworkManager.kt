package com.industryproject.connfy.networkManager

import android.content.Context
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class NetworkManager(context: Context) {

    private val TAG = "NetworkManager"
    private var instance: NetworkManager? = null

    private val prefixURL = "https://connfy.azurewebsites.net"

    //for Volley API
    var requestQueue: RequestQueue? = null


    init {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext())
    }


    companion object{
        private var instance: NetworkManager? = null

        @Synchronized
        fun getInstance(context: Context): NetworkManager? {
            if (null == instance) instance = NetworkManager(context)
            return instance
        }

        //this is so you don't need to pass context each time
        @Synchronized
        fun getInstance(): NetworkManager? {
            checkNotNull(instance) {
                NetworkManager::class.java.simpleName +
                        " is not initialized, call getInstance(...) first"
            }
            return instance
        }
    }

    fun ugabuga(param1: Any?, listener: NetworkListener) {
        val url = prefixURL + "this/request/suffix"
        val jsonParams: MutableMap<String?, Any?> = HashMap()
        jsonParams["param1"] = param1
        val request = JsonObjectRequest(Request.Method.POST, url, JSONObject(jsonParams),
                Response.Listener<JSONObject?> { response ->
                    Log.d("$TAG: ", "somePostRequest Response : $response")
                    listener.getResult(response.toString())
                },
                Response.ErrorListener { error ->
                    Log.i("mytag", "Error :" + error.toString())
                })
        requestQueue!!.add(request)
    }


     fun test(jwtToken: String, listener: NetworkListener) {
         val url = "$prefixURL/contacts"

         //String Request initialized
         val mStringRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { response ->
             listener.getResult(response.toString())

         }, Response.ErrorListener { error ->
             Log.i("mytag", "Error :$error")
         }) {

             override fun getHeaders(): Map<String, String>? {
                 val headers: MutableMap<String, String> =
                     HashMap()
                 headers["Authorization"] = "Bearer $jwtToken"
                 return headers
             }

         }

         requestQueue!!.add(mStringRequest)
     }
}