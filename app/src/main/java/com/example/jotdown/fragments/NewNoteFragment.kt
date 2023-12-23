package com.example.jotdown.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.content.BroadcastReceiver
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.jotdown.R
import com.example.jotdown.databinding.FragmentNewNoteBinding
import com.example.jotdown.db.NotesDatabase
import com.example.jotdown.mvvm.NotesFactoryViewModel
import com.example.jotdown.mvvm.NotesRepository
import com.example.jotdown.mvvm.NotesViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import java.util.Locale

class NewNoteFragment : Fragment() {
    lateinit var binding: FragmentNewNoteBinding
    lateinit var viewModel:NotesViewModel
    private val REMINDER_REQUEST_CODE = 1080
    val PICK_IMAGE = 1

    @Suppress("DEPRECATION")
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //container is the parent view  in which the fragment view will be placed
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_new_note,container,false)
        (activity as AppCompatActivity).supportActionBar?.setTitle("Create Note")

        createNotificationChannel()

        binding.ReminderAlarmbtn.setOnClickListener {
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
        val dao = NotesDatabase.getInstance(requireContext()).notesDao
        val repository =NotesRepository(dao)
        val factory=NotesFactoryViewModel(repository)
        viewModel = ViewModelProvider(this,factory)[NotesViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner=this
        binding.imagegALLERYbUTN.setOnClickListener {
            val galleryintent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryintent,PICK_IMAGE)
        }

        binding.floatingActionButtonNewNote.setOnClickListener {
            val noteTitle =  viewModel.inputTitle.value?.trim()
            val noteSubTitle =  viewModel.inputSubTitle.value?.trim()
            val noteText = viewModel.inputNoteText.value?.trim()

            if(!noteText.isNullOrEmpty() && !noteSubTitle.isNullOrEmpty() && !noteTitle.isNullOrEmpty()){
                viewModel.addNotes()
                view?.findNavController()?.navigate(R.id.action_newNoteFragment_to_homeFragment)
                Toast.makeText(context,"Notes Saved",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context,"Notes can't be empty ",Toast.LENGTH_SHORT).show()
            }

        }

        return binding.root
    }
//the below function will Check the Android version and creates a notification channel if the version is Android Oreo or higher.
// This is necessary to show notifications from your app.
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

        Toast.makeText(context, "Reminder set successfully", Toast.LENGTH_SHORT).show()
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
//            val selectedImageUri: Uri? = data?.data
//            if (selectedImageUri != null) {
//                // Set the selected image URI to your ImageView
//                binding.imageViewNewNote.setImageURI(selectedImageUri)
//            }
//        }
//    }


}

