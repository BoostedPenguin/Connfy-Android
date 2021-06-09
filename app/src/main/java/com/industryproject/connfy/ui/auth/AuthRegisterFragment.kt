package com.industryproject.connfy.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.industryproject.connfy.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AuthRegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AuthRegisterFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        auth = Firebase.auth

        return inflater.inflate(R.layout.fragment_auth_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.signInText).setOnClickListener {
            findNavController().navigateUp()
        }

        view.findViewById<ImageView>(R.id.backToLogin).setOnClickListener {
            findNavController().navigateUp()
        }

        view.findViewById<Button>(R.id.buttonCreateEmail).setOnClickListener {
            createuser()
        }
    }


    private fun createuser() {
        auth.createUserWithEmailAndPassword("myrandomemail@abv.bg", "testing")
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser

                        auth.currentUser?.getIdToken(false)
                                ?.addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        val idToken: String? = it.result?.token
                                        Log.d("IDTOKEN", idToken.toString())
                                    } else {
                                        // Handle error -> task.getException();
                                    }
                                }

                    } else {
                        // If sign in fails, display a message to the user.

                    }
                }
    }
}