package com.industryproject.connfy

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AuthMainFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient



    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_auth_main, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGoogleAuth()


        view.findViewById<Button>(R.id.buttonSignGoogle).setOnClickListener {
            signIn()
        }

        view.findViewById<Button>(R.id.buttonSignEmail).setOnClickListener {
            findNavController().navigate(R.id.action_fragment_auth_main_to_fragment_auth_login)
        }
    }

    private fun setupGoogleAuth() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

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
            val intent = Intent(context, DashboardActivity::class.java)
            signOut.launch(intent)
        }
        else {
            updateUI()
        }
    }

    private fun updateUI() {
        requireView().findViewById<Button>(R.id.buttonSignGoogle).visibility = View.VISIBLE
        requireView().findViewById<Button>(R.id.buttonSignOutlook).visibility = View.VISIBLE
        requireView().findViewById<Button>(R.id.buttonSignEmail).visibility = View.VISIBLE
    }

    private fun signIn() {
        val intent = googleSignInClient.signInIntent
        //startActivityForResult(signInIntent, 9001)

        signInIntent.launch(intent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("mytag", "signInWithCredential:success")
                        val user = auth.currentUser

                        val intent = Intent(context, DashboardActivity::class.java)
                        signOut.launch(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("mytag", "signInWithCredential:failure", task.exception)
                    }
                }
    }


    private val signOut = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // Handle the returned Uri
        if(result.resultCode == 301) {
            FirebaseAuth.getInstance().signOut()
            googleSignInClient.signOut().addOnCompleteListener(requireActivity()
            ) {
                Log.i("mytag", "Logged out")
                updateUI()

            }
        }
    }

    private val signInIntent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // Handle the returned Uri
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
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
}