package com.industryproject.connfy.ui.create_meeting

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.industryproject.connfy.R
import com.industryproject.connfy.models.GeoLocation
import com.industryproject.connfy.ui.dashboard_activity.DashboardViewModel

class CreateMeeting : Fragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener,
    GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {
    private val model: DashboardViewModel by activityViewModels()
    private var googleMap: GoogleMap? = null
    private var routeList: MutableList<GeoLocation> = ArrayList<GeoLocation>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // google map initialization
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Init event callers
        registerEvents()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.create_meeting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var btnCreateMeeting: Button = view.findViewById(R.id.button2)
        btnCreateMeeting.setOnClickListener {
            Toast.makeText(context, "works", Toast.LENGTH_LONG).show()
            model.createMeeting(routeList)
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        googleMap?.setOnMapClickListener(this)
        googleMap?.setOnMapLongClickListener(this)
        googleMap?.setOnMarkerClickListener(this)
    }

    override fun onMapClick(latLng: LatLng) {
        Toast.makeText(
            context,
            "onMapClick:${latLng.latitude} : ${latLng.longitude}",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onMapLongClick(latLng: LatLng) {
        Toast.makeText(
            context,
            "onMapLongClick:${latLng.latitude} : ${latLng.longitude}",
            Toast.LENGTH_LONG
        ).show()

        //Add marker on LongClick position
        val markerOptions = MarkerOptions().position(latLng).title(latLng.toString())
        this.googleMap?.addMarker(
            markerOptions.icon(
                BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_AZURE
                )
            )
        )
        routeList.add(GeoLocation(latLng.latitude, latLng.longitude))
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        marker.remove()
        routeList.remove(GeoLocation(marker.position.latitude, marker.position.longitude))
        return false
    }

    private fun registerEvents() {
        requireView().findViewById<Button>(R.id.create_meeting_add_to_meeting).setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        // TODO
        lateinit var dialog: AlertDialog


        val choices = mutableListOf<String>()
        val checked = mutableListOf<Boolean>()
        for (contact in model.contacts.value?.data!!) {

            choices.add(contact.name ?: "Unknown")

            if (model.creatingMeeting.invitedUsers?.any { it -> it.uid == contact.uid } == true) {
                checked.add(true)
            } else {
                checked.add(false)
            }
        }

        val choicesArray: Array<String> = choices.toTypedArray()
        val checkedArray: Array<Boolean> = checked.toTypedArray()

        val builder = AlertDialog.Builder(context)

        builder.setTitle("Choose members")

        builder.setMultiChoiceItems(
            choicesArray,
            checkedArray.toBooleanArray()
        ) { dialog, which, isChecked ->
            checkedArray[which] = isChecked

            val selected = choicesArray[which]
            Toast.makeText(
                context,
                "$selected $isChecked", Toast.LENGTH_SHORT
            ).show();
        }

        builder.setPositiveButton("OK") { _, _ ->
            var queueUsersToAdd = mutableListOf<String>()
            for (i in choicesArray.indices) {
                val checked = checkedArray[i]

                if (checked) {

                    if (model.contacts.value != null) {
                        model.contacts.value!!.data[i].uid?.let { queueUsersToAdd.add(it) }
                    }
                }
            }
            model.creatingMeeting.invitedUsersIds = queueUsersToAdd
        }

        dialog = builder.create()

        dialog.show()
    }
}