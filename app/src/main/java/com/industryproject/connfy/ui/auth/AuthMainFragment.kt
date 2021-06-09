package com.industryproject.connfy.ui.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.industryproject.connfy.DashboardActivity
import com.industryproject.connfy.R
import com.industryproject.connfy.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class AuthMainFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val model: AuthViewModel by activityViewModels()



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

        view.findViewById<Button>(R.id.buttonSignOutlook).setOnClickListener {
            model.getContacts()
        }
    }

    private fun setupGoogleAuth() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        auth = Firebase.auth

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