package com.example.jotdown.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jotdown.db.Notes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class NotesViewModel(private val repository: NotesRepository):ViewModel() {
    val inputTitle = MutableLiveData<String>()
    val inputSubTitle = MutableLiveData<String>()
    val inputNoteText = MutableLiveData<String>()

    val displayAllNotes = repository.allnotes()





    fun addNotes(){

        val title = inputTitle.value
        val subtitle = inputSubTitle.value
        val noteText = inputNoteText.value

        insertNote(Notes(0,title!!,subtitle!!,noteText!!)) //double exclamation mark is there bcoz it tells title and all are not null
    }

    fun insertNote(notes: Notes)= viewModelScope.launch{Dispatchers.IO
        repository.insertNote(notes)
        //This means the insertion operation will be performed on a background I/O thread to prevent blocking the main thread.
    // After launching the coroutine, the repository.insertNote(notes) call is executed to insert the note into the database.

    }

    fun editNotes(notes: Notes){
        updateNotes(notes)
    }

     fun updateNotes(notes: Notes) = viewModelScope.launch { Dispatchers.IO
     repository.UpdateNote(notes)

     }
    fun deleteNote(notes: Notes){
        deleteNotes(notes)
    }

    fun deleteNotes(notes: Notes) =viewModelScope.launch { Dispatchers.IO
    repository.deleteNote(notes)}

    fun clearAll(){
        delAll()
    }

    fun delAll()=viewModelScope.launch { Dispatchers.IO
        repository.deleteAll()
    }



}
