package com.industryproject.connfy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = Firebase.auth


        // TODO
        // This needs to be called on every request API request to backend.
        // Token is most often cached so it is optimized
        // Have to create a wrapper around it
        auth.currentUser?.getIdToken(false)
            ?.addOnCompleteListener {
                if (it.isSuccessful) {
                    val idToken: String? = it.result?.token

                    idToken

                    // Send token to your backend via HTTPS
                    // ...
                } else {
                    // Handle error -> task.getException();
                }
            }

        if( auth.currentUser != null){
            val intent = Intent(applicationContext, DashboardActivity::class.java)
            signOut.launch(intent)
        }

        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.buttonSignGoogle).setOnClickListener {
            signIn()
        }
    }

    private val signOut = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // Handle the returned Uri
        if(result.resultCode == 301) {
            FirebaseAuth.getInstance().signOut()
            googleSignInClient.signOut().addOnCompleteListener(this
            ) { Log.i("mytag", "Logged out") }
        }
    }

//    private val signInIntent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        // Handle the returned Uri
//        if (result.resultCode == 9001) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                val account = task.getResult(ApiException::class.java)!!
//
//
//                Log.d("mytag", "firebaseAuthWithGoogle:" + account.id)
//                firebaseAuthWithGoogle(account.idToken!!)
//                //sendcall(account.idToken!!)
//            } catch (e: ApiException) {
//                // Google Sign In failed, update UI appropriately
//                Log.w("mytag", "Google sign in failed", e)
//            }
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 9001) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!


                Log.d("mytag", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
                //sendcall(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("mytag", "Google sign in failed", e)
            }
        }
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


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("mytag", "signInWithCredential:success")
                    val user = auth.currentUser

                    val intent = Intent(applicationContext, DashboardActivity::class.java)
                    signOut.launch(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("mytag", "signInWithCredential:failure", task.exception)
                }
            }
    }



    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 9001)
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