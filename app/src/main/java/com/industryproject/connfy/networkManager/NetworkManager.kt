package com.industryproject.connfy.networkManager

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import javax.xml.transform.ErrorListener

 class NetworkManager(context: Context) {

    private val TAG = "NetworkManager"
    private var instance: NetworkManager? = null

    private val prefixURL = "https://192.168.0.106:3000/users/sssh"

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

    fun ugabuga(param1: Any?, listener: NetworkListener<String?>) {
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
     fun test() {
         val url = "https://connfy.azurewebsites.net/"
         val stringRequest = StringRequest(Request.Method.GET, url,
                 Response.Listener<String> { response ->
                     // Display the first 500 characters of the response string.
                    Log.d("ugabuga",response)
                 },
                 Response.ErrorListener { error -> Log.d("ugabuga",error.toString())})
         requestQueue!!.add(stringRequest)
     }
}