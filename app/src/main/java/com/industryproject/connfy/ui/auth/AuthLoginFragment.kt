package com.industryproject.connfy.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.industryproject.connfy.R
import com.industryproject.connfy.ui.dashboard_activity.DashboardActivity
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class AuthLoginFragment : Fragment() {
    private val model: AuthViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        auth = Firebase.auth

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auth_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.backToMainAuth).setOnClickListener {
            findNavController().navigateUp()
        }

        view.findViewById<TextView>(R.id.loginSignUpText).setOnClickListener {
            findNavController().navigate(R.id.action_fragment_auth_login_to_fragment_auth_register)
        }


        //TODO authenticate user with FireBase
        view.findViewById<Button>(R.id.buttonLoginEmail).setOnClickListener {
            val email: String = view.findViewById<EditText>(R.id.loginEmail).text.toString()
            val password: String = view.findViewById<EditText>(R.id.loginPassword).text.toString()

            if(!isEmailValid(email)) {
                Toast.makeText(context, "Email isn't valid", Toast.LENGTH_SHORT).show()
            }
            else if(password.isEmpty()) {
                Toast.makeText(context, "Password isn't valid", Toast.LENGTH_SHORT).show()
            }
            else {
                signIn(email, password)
            }
        }
    }

    private fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information

                        auth.currentUser?.getIdToken(false)
                                ?.addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        val idToken: String? = it.result?.token


                                        val intent = Intent(context, DashboardActivity::class.java)
                                        signOut.launch(intent)

                                    } else {
                                        Log.d("edit_user", task.exception?.message.toString())
                                    }
                                }

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(context, task.exception?.message.toString(), Toast.LENGTH_SHORT).show()

                    }
                }
    }

    private val signOut = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // Handle the returned Uri
        if(result.resultCode == 301) {
            FirebaseAuth.getInstance().signOut()
        }
    }
}