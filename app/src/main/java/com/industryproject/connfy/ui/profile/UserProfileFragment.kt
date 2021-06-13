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
import com.industryproject.connfy.ui.dashboard_activity.SelectedPersonStatus
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


        model.selectedPersonStatus.observe(viewLifecycleOwner, Observer {
            when(model.selectedPersonStatus.value) {
                SelectedPersonStatus.InContacts -> {
                    val s = 5
                    button.setImageResource(R.drawable.ic_baseline_person_add_disabled_24)
                }
                SelectedPersonStatus.NotInContacts -> {
                    val s = 5

                    button.setImageResource(R.drawable.ic_baseline_person_add_24)
                }
                SelectedPersonStatus.IsUser -> {
                    val s = 5

                    button.visibility = View.INVISIBLE
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