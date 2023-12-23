package com.example.jotdown.fragments.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.jotdown.R
import com.example.jotdown.databinding.NotesItemListBinding
import com.example.jotdown.db.Notes
import kotlin.random.Random

class NotesAdapter(private var notesList: List<Notes>) : //here notesListcontaining the whole data/list of the actual notes
    RecyclerView.Adapter<MyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val layoutinflater = LayoutInflater.from(parent.context)
        val binding: NotesItemListBinding=DataBindingUtil.inflate(layoutinflater, R.layout.notes_item_list,parent,false)
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
       holder.bindTheView(notesList[position])

    }

    override fun getItemCount(): Int {
        return notesList.size
    }


    fun filteredlist(newFilteredList: ArrayList<Notes>) {
        notesList = newFilteredList
        notifyDataSetChanged()

    }


}

class MyHolder(val binding: NotesItemListBinding ) : RecyclerView.ViewHolder(
    binding.root
){
        fun bindTheView(notes : Notes){
            binding.noteTitle.text=notes.noteTitle
            binding.noteSubtitle.text=notes.noteSubtitle

//            val randomColor = randomColor()
//            binding.listItemLayout.setBackgroundResource(randomColor)
//click listener on list which will be present on our home will take us to update fragment
            binding.listItemLayout.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("notesTitle",notes.noteTitle)
                bundle.putString("notesSubTitle",notes.noteSubtitle)
                bundle.putString("notesText",notes.noteText)
                bundle.putInt("id", notes.noteId) // Assuming noteId is the integer ID of the note

                Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_upatedNoteFragment,bundle)


            }
        }

//    private fun randomColor(): Int {
//        val list = ArrayList<Int>()
//        list.add(R.color.Notes1)
//        list.add(R.color.Notes2)
//        list.add(R.color.Notes3)
//        list.add(R.color.Notes4)
//        list.add(R.color.Notes5)
//        list.add(R.color.Notes6)
//
//        val seed = System.currentTimeMillis().toInt()
//        val randomIndex = Random(seed).nextInt(list.size)
//        return list[randomIndex]
//
//    }
}

