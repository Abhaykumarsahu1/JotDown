package com.example.jotdown.db

import android.icu.text.CaseMap.Title
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
//it is simply a code of database schema creation with table name
// and some columns which will hold the data
@Entity("Notes")
data class Notes(
//there has to be a primary key in table and that should be integer
    @ColumnInfo("noteId")
    @PrimaryKey(autoGenerate = true)
    val noteId : Int,
    @ColumnInfo("noteTitle")
    val noteTitle: String,
    @ColumnInfo("noteSubtitle")
    val noteSubtitle: String,
    @ColumnInfo("noteText")
    val noteText:String

)