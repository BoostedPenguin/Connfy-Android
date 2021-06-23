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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.industryproject.connfy.R
import com.industryproject.connfy.ui.dashboard_activity.DashboardActivity
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AuthRegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class AuthRegisterFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private val model: AuthViewModel by activityViewModels()

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

            val email: String = view.findViewById<EditText>(R.id.createEmail).text.toString()
            val password: String = view.findViewById<EditText>(R.id.createPassword).text.toString()
            val name: String = view.findViewById<EditText>(R.id.createName).text.toString()

            if(name.isEmpty()) {
                Toast.makeText(context, "Name is empty", Toast.LENGTH_SHORT).show()
            }
            else if(!isEmailValid(email)) {
                Toast.makeText(context, "Email isn't valid", Toast.LENGTH_SHORT).show()
            }
            else if(password.isEmpty()) {
                Toast.makeText(context, "Password isn't valid", Toast.LENGTH_SHORT).show()
            }
            else {
                createUser(email, password, name)
            }
        }

        model.onCreationComplete.observe(viewLifecycleOwner, Observer {
            if(it) {
                view.findViewById<ProgressBar>(R.id.progressBarRegister).visibility = View.INVISIBLE

                model.onCreationComplete.value = false;
                val intent = Intent(context, DashboardActivity::class.java)
                signOut.launch(intent)
            }
        })
    }

    private fun isEmailValid(email: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun createUser(email: String, password: String, name: String) {

        requireView().findViewById<ProgressBar>(R.id.progressBarRegister).visibility = View.VISIBLE

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information

                        auth.currentUser?.getIdToken(false)
                                ?.addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        val idToken: String? = it.result?.token
                                        Log.d("IDTOKEN", idToken.toString())

                                        model.createUserInDBEmail("EMAIL", name, email)
                                    } else {
                                        Log.d("create_user", task.exception?.message.toString())
                                    }
                                }

                    } else {
                        requireView().findViewById<ProgressBar>(R.id.progressBarRegister).visibility = View.INVISIBLE

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