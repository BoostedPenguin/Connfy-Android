package com.industryproject.connfy.ui.meeting_view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.industryproject.connfy.R

class MeetingViewFragment : Fragment() {

    companion object {
        fun newInstance() = MeetingViewFragment()
    }

    private lateinit var viewModel: MeetingViewViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.meeting_view_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MeetingViewViewModel::class.java)
        // TODO: Use the ViewModel
    }


}