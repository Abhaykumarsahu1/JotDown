package com.example.jotdown.fragments

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.jotdown.R
import com.example.jotdown.databinding.FragmentUpatedNoteBinding
import com.example.jotdown.db.Notes
import com.example.jotdown.db.NotesDatabase
import com.example.jotdown.mvvm.NotesFactoryViewModel
import com.example.jotdown.mvvm.NotesRepository
import com.example.jotdown.mvvm.NotesViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UpatedNoteFragment : Fragment(), MenuProvider {
    lateinit var binding : FragmentUpatedNoteBinding
    lateinit var viewModel : NotesViewModel
    lateinit var title :String
    lateinit var Subtitle :String
    lateinit var NoteText :String
    private var id1: Int = 0
    private val REMINDER_REQUEST_CODE = 1080
    @Suppress("DEPRECATION")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_upated_note,container,false)
        (activity as AppCompatActivity).supportActionBar?.setTitle("Edit Note")
        setHasOptionsMenu(true)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.CREATED)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentDateTime = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val currentDate = dateFormat.format(currentDateTime.time)
        val currentTime = timeFormat.format(currentDateTime.time)
        val dao = NotesDatabase.getInstance(requireContext()).notesDao
        val repository = NotesRepository(dao)
        val factory= NotesFactoryViewModel(repository)
        viewModel = ViewModelProvider(this,factory).get(NotesViewModel::class.java)
        binding.viewModel = viewModel

        title = requireArguments().getString("notesTitle").toString()
        Subtitle = requireArguments().getString("notesSubTitle").toString()
        NoteText = requireArguments().getString("notesText").toString()
        id1 = requireArguments().getInt("id", 0)
        createNotificationChannel()
        binding.TitleEditNotes.setText(title)
        binding.Editsubtitle.setText(Subtitle)
        binding.EditnotesText.setText(NoteText)

        binding.ReminderAlarmbtnupdate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute= calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { _, selectedHour, selectedMinute ->
                    // Set the selected time to the calendar instance
                    calendar.apply {
                        set(Calendar.HOUR_OF_DAY, selectedHour)
                        set(Calendar.MINUTE, selectedMinute)
                        set(Calendar.SECOND, 0)
                    }

                    // Schedule the reminder using AlarmManager
                    scheduleReminder(calendar.timeInMillis)
                },
                hour,
                minute,
                false
            )
            timePickerDialog.show()
        }
        binding.floatingActionButtonNupdateNote.setOnClickListener {

            val newTitle = binding.TitleEditNotes.text
            val newSubTitle = binding.Editsubtitle.text
            val newText = binding.EditnotesText.text

            viewModel.editNotes (Notes(id1,
                    newTitle.toString(),
                    newSubTitle.toString(),
                    newText.toString()))


            Log.d("Mytag","newid : $id1, newTitle: $newTitle,newSubtitle : $newSubTitle,newText : $newText")
            Toast.makeText(context, "Notes Updated", Toast.LENGTH_SHORT).show()
            view.findNavController().navigate(R.id.action_upatedNoteFragment_to_homeFragment)
        }

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Reminder Channel"
            val descriptionText = "Channel for reminder notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("reminder_channel_id", name, importance).apply {
                description = descriptionText
            }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Function to schedule the reminder using AlarmManager
    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleReminder(reminderTimeMillis: Long) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), ReminderBroadcastReceiver::class.java)
        // Pass any necessary data with the intent if needed

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            REMINDER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            reminderTimeMillis,
            pendingIntent
        )

        Toast.makeText(context, "Reminder updated successfully", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
      menuInflater.inflate(R.menu.menu,menu)
        val searchView = menu.findItem(R.id.app_bar_search)
        val deleteIcon = menu.findItem(R.id.delete)
        if (deleteIcon.isVisible){ //in update fragment there sould not be search icon that's why search view is turned false
            searchView.setVisible(false)
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        val idtoDelete = id1
        Log.d("DeleteNote", "Attempting to delete note with ID: $idtoDelete")
        if (menuItem.itemId==R.id.delete){
            viewModel.deleteNotes(Notes(idtoDelete,title,Subtitle,NoteText))
            Toast.makeText(context,"Note deleted",Toast.LENGTH_SHORT).show()
            view?.findNavController()?.navigate(R.id.action_upatedNoteFragment_to_homeFragment)
        }
        return true
    }
}