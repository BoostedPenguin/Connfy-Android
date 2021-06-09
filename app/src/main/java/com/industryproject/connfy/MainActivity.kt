package com.industryproject.connfy

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.industryproject.connfy.networkManager.NetworkManager
import org.json.JSONObject


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }


    fun sendcall(tokenId: String) {
        //RequestQueue initialized
        val mRequestQueue = Volley.newRequestQueue(this)

        //String Request initialized
        val mStringRequest = object : StringRequest(Request.Method.POST, "https://192.168.0.106:3000/users/sssh", Response.Listener { response ->
            Toast.makeText(applicationContext, "Logged In Successfully", Toast.LENGTH_SHORT).show()


        }, Response.ErrorListener { error ->
            Log.i("mytag", "Error :" + error.toString())
            Toast.makeText(applicationContext, "Please make sure you enter correct password and username", Toast.LENGTH_SHORT).show()
        }) {
            override fun getBodyContentType(): String {
                return "application/json"
            }

            override fun getBody(): ByteArray {
                val params2 = HashMap<String, String>()
                params2["idToken"] = tokenId

                return JSONObject(params2 as Map<String, String>).toString().toByteArray()
            }

        }
        mRequestQueue!!.add(mStringRequest)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}