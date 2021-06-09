package com.industryproject.connfy

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class AuthLoginFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
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
            startActivity(Intent(context, DashboardActivity::class.java))
        }
    }
}