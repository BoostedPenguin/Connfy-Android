package com.industryproject.connfy.ui.create_meeting

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.textfield.TextInputEditText
import com.industryproject.connfy.R
import com.industryproject.connfy.models.GeoLocation
import com.industryproject.connfy.ui.dashboard_activity.DashboardViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class CreateMeeting : Fragment(), OnMapReadyCallback,
    GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private val model: DashboardViewModel by activityViewModels()
    private var googleMap: GoogleMap? = null
    private var routeList: MutableList<GeoLocation> = ArrayList<GeoLocation>()
    private var invitedUsers: MutableList<String> = ArrayList<String>()

    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0
    private var hour: Int = 0
    private var minutes: Int = 0
    private var dateTime: String = ""

    private lateinit var dateTimeTxtVw: TextView
    private lateinit var dateTimeEditTxt: EditText

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnCreateMeeting: Button = view.findViewById(R.id.btnCreateMeeting)
        val btnDatePicker: ImageButton = view.findViewById(R.id.create_meeting_date_button)
        dateTimeEditTxt = view.findViewById(R.id.create_meeting_title)
        dateTimeTxtVw = view.findViewById(R.id.create_meeting_title2)
        btnCreateMeeting.setOnClickListener {
            val title: String = dateTimeEditTxt.text.toString()

            model.createMeeting(routeList, LocalDateTime.of(year, month, day, hour, minutes), title, invitedUsers)
        }

        btnDatePicker.setOnClickListener{
            val calendar: Calendar = Calendar.getInstance()
            context?.let { it1 -> DatePickerDialog(it1, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)) }?.show()
        }

        model.isCreated.observe(viewLifecycleOwner, Observer {
            if(it) {
                model.isCreated.value = false;
                findNavController().navigate(R.id.action_nav_create_meeting_to_nav_home)
            }
        })

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap

        googleMap?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                (LatLng(
                    51.4416,
                    5.4697
                )), 14f
            )
        )

        googleMap?.setOnMapLongClickListener(this)
        googleMap?.setOnMarkerClickListener(this)
    }


    override fun onMapLongClick(latLng: LatLng) {

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
        requireView().findViewById<Button>(R.id.create_meeting_add_to_meeting2).setOnClickListener {
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
        ) { _, which, isChecked ->
            checkedArray[which] = isChecked

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
            //model.creatingMeeting.invitedUsersIds = queueUsersToAdd
            invitedUsers.addAll(queueUsersToAdd)
        }

        dialog = builder.create()

        dialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        day = dayOfMonth;
        this.month = month+1;
        this.year = year;

        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minutes = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(context, this, hour, minutes, true)
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        hour = hourOfDay;
        minutes = minute;

        dateTime = "$day/$month/$year $hour:$minutes"
        dateTimeTxtVw.text = dateTime
    }
}