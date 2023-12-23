package com.example.jotdown.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.jotdown.R
import com.example.jotdown.databinding.FragmentHomeBinding
import com.example.jotdown.db.Notes
import com.example.jotdown.db.NotesDatabase
import com.example.jotdown.fragments.adapter.NotesAdapter
import com.example.jotdown.mvvm.NotesFactoryViewModel
import com.example.jotdown.mvvm.NotesRepository
import com.example.jotdown.mvvm.NotesViewModel


class HomeFragment : Fragment() ,MenuProvider{
lateinit var binding:FragmentHomeBinding //fragmentHomebinding is nothing but a homefragment layout
lateinit var viewModel:NotesViewModel
lateinit var adapter:NotesAdapter
var dummylist = listOf<Notes>()
var searchnotes = listOf<Notes>()
var isopened : Boolean = false

    @Suppress("DEPRECATION")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
    (activity as AppCompatActivity).supportActionBar?.setTitle("Home")

    setHasOptionsMenu(true)

    val menuHost: MenuHost = requireActivity()
    menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.CREATED)
        val dao = NotesDatabase.getInstance(requireContext()).notesDao
        val repository = NotesRepository(dao)
        val factory= NotesFactoryViewModel(repository)



        viewModel = ViewModelProvider(this,factory).get(NotesViewModel::class.java)
        adapter = NotesAdapter(dummylist)

        binding.recyclerView.layoutManager=StaggeredGridLayoutManager(2,LinearLayout.VERTICAL)
        viewModel.displayAllNotes.observe(viewLifecycleOwner, Observer {
            searchnotes = it as ArrayList<Notes>
            adapter=NotesAdapter(it)
            binding.recyclerView.adapter= adapter

        })

        binding.floatingActionButton.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_homeFragment_to_newNoteFragment)

        }
        return binding.root
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu,menu)
        val menuItem = menu.findItem(R.id.app_bar_search)
        val searchView = menuItem.actionView as SearchView
        val deleteIcon = menu.findItem(R.id.delete)

        searchView.setOnSearchClickListener {
            deleteIcon.setVisible(false)
            isopened = true
        }
        searchView.setOnCloseListener(SearchView.OnCloseListener {
            deleteIcon.setVisible(true)
            isopened.not()
        })
        searchView.queryHint= "Search Notes"

        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
TODO("NOT YET IMPLEMENTED")
            }

            override fun onQueryTextChange(p0: String?): Boolean {
               noteFilter(p0)
                return true
            }

        })
    }

    private fun noteFilter(p0: String?) {
        val newFilteredList = arrayListOf<Notes>()
        for (i in searchnotes){
            if (i.noteTitle.contains(p0!!) || i.noteSubtitle.contains(p0!!)){
                newFilteredList.add(i)
            }
        }
        adapter.filteredlist(newFilteredList)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId==R.id.delete){
            viewModel.clearAll()
            Toast.makeText(context,"all notes cleared",Toast.LENGTH_SHORT).show()
        }
        return true
    }

}