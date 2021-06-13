package com.industryproject.connfy.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.industryproject.connfy.R
import com.industryproject.connfy.ui.dashboard_activity.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment : Fragment() {
    private val model: DashboardViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.user_profile_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel



        val button = requireView().findViewById<Button>(R.id.profileButtonAdd) as ImageButton


        model.contacts.observe(viewLifecycleOwner, Observer {

            val containsFriend = it.data.any { itUser ->
                itUser.uid == model.selectedPersonProfile.value?.uid
            }

            when {
                model.selectedPersonProfile.value?.uid == model.thisUser.value?.data?.uid -> {
                    button.visibility = View.INVISIBLE
                }
                containsFriend -> {
                    button.setImageResource(R.drawable.ic_baseline_person_add_disabled_24)
                }
                else -> {
                    button.setImageResource(R.drawable.ic_baseline_person_add_24)
                }
            }
        })

        model.assignSelectedPersonProfile(arguments?.getString("PERSON_EMAIL"), arguments?.getString("PERSON_NAME"), arguments?.getString("PERSON_UID"))

        view?.findViewById<TextView>(R.id.profileName)?.text = model.selectedPersonProfile.value?.name
        view?.findViewById<TextView>(R.id.profileEmail)?.text = model.selectedPersonProfile.value?.email




        button.setOnClickListener {
            if(model.selectedPersonProfile.value?.uid != null) {
                model.handleContactAction()
            }
        }
    }
}