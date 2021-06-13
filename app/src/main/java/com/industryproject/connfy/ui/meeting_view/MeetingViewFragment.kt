package com.industryproject.connfy.ui.meeting_view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.industryproject.connfy.R
import com.industryproject.connfy.adapters.ContactRecyclerViewAdapter
import com.industryproject.connfy.adapters.MeetingUsersAdapter
import com.industryproject.connfy.models.User
import com.industryproject.connfy.ui.dashboard_activity.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeetingViewFragment : Fragment() {

    private var locationPermissionGranted: Boolean = false
    private lateinit var contactsAdapter: MeetingUsersAdapter

    private val model: MeetingViewViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.meeting_view_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        checkLocationPermissions()

        // Get meeting details
        model.getMeeting(arguments?.getString("MEETING_ID")!!)

        //google map initialization
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        // Invited Users adapter
        initUserRecyclerView()

    }


    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        if (locationPermissionGranted) {
            googleMap.isMyLocationEnabled = true
        }
        val eindhoven = LatLng(51.441642, 5.4697225)
        googleMap.addMarker(MarkerOptions().position(eindhoven).title("Marker in Eindhoven"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eindhoven, 14f))
    }

    private fun initUserRecyclerView() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.createMeetingUsersRecyclerView)
        contactsAdapter = MeetingUsersAdapter()

        recyclerView.adapter = contactsAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Add invited users to this method :)

        //contactsAdapter.setInvitedUsers(model.)

        val dividerItemDecoration = DividerItemDecoration(recyclerView.context,
                LinearLayoutManager(context).orientation)

        recyclerView.addItemDecoration(dividerItemDecoration)

        contactsAdapter.setOnContactButtonClickListener(object: MeetingUsersAdapter.OnContactButtonClickListener {
            override fun onContactButtonClick(position: Int, person: User) {
                TODO("Not yet implemented")
            }
        })

        contactsAdapter.setOnItemClickListener(object: MeetingUsersAdapter.OnItemClickListener {
            override fun onContactButtonClick(position: Int, person: User) {

                val bundle = bundleOf(
                        Pair("PERSON_NAME", person.name),
                        Pair("PERSON_EMAIL", person.email),
                        Pair("PERSON_UID", person.uid)
                )
                findNavController().navigate(R.id.nav_profile_view, bundle)

            }
        })

        model.currentMeeting.observe(viewLifecycleOwner, Observer { it ->
            it?.data?.let {it1 ->
                if(it1.invitedUsers != null && it1.ownerName != null && it1.ownerUid != null) {
                    contactsAdapter.setUserContent(it1.invitedUsers, it1.ownerUid, it1. ownerName)
                }
            }
        })
    }


    private fun checkLocationPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                locationPermissionGranted = true
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
            -> {}
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (!isGranted) {
                Toast.makeText(
                    requireContext(),
                    "Permission denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
}