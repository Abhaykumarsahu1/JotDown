package com.example.jotdown.mvvm

import androidx.lifecycle.LiveData
import com.example.jotdown.db.Notes
import com.example.jotdown.db.NotesDao

class NotesRepository(val dao:NotesDao) {
    //this repository kind of thing is needed because when we access data from notesDao and database then we have to store it in this repo
    //database->dao(works on our database)->we store though the methods of dao into our repo and we will attach that data with our view in view in viewdatamodel

    //to display all note (we are storing database into rep)
    fun allnotes():LiveData<List<Notes>>{
        return dao.getAllNotes()
    }

    //insertNotes
    suspend fun insertNote(notes: Notes){
        dao.insertNotes(notes)
    }

    //updatenotes
    suspend fun UpdateNote(notes: Notes){
        dao.updateNotes(notes)
    }

    //delete note
    suspend fun deleteNote(notes: Notes){

        dao.deleteNotes(notes)
    }

    suspend fun deleteAll(){
        dao.deleteAll()
    }


}