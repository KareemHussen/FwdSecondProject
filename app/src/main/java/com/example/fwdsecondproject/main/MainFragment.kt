package com.example.fwdsecondproject.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fwdsecondproject.R
import com.example.fwdsecondproject.adapters.AsteroidAdapter
import com.example.fwdsecondproject.databinding.FragmentMainBinding
import com.example.fwdsecondproject.db.AsteroidDatabase
import com.example.fwdsecondproject.repository.AsteroidRepository
import com.example.fwdsecondproject.repository.AsteroidViewModelFactory


class MainFragment : Fragment() {


    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(requireActivity() , AsteroidViewModelFactory(repository))[MainViewModel::class.java]
    }

    private val adapter = AsteroidAdapter(AsteroidAdapter.AsteroidListener { asteroid ->
        viewModel.onAsteroidClicked(asteroid)
    })
    private val repository by lazy {
        AsteroidRepository(AsteroidDatabase.getDatabase(requireContext()).asteroidDao)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.asteroidRecycler.adapter = adapter
        binding.asteroidRecycler.layoutManager = LinearLayoutManager(requireContext())

        viewModel.asteroids.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }

        viewModel.loading.observe(viewLifecycleOwner){ isLoading ->
            if (isLoading){
                binding.loadingDataSpinner.visibility = View.VISIBLE
            }else{
                binding.loadingDataSpinner.visibility = View.GONE
            }
        }

        viewModel.navigateToAsteroidDetail!!.observe(viewLifecycleOwner){ asteroid ->
            asteroid?.let {
                val action = MainFragmentDirections.actionMainFragmentToDetailFragment(asteroid)
                findNavController().navigate(action)
                viewModel.onAsteroidDetailNavigated()
            }
        }



        binding.executePendingBindings()
        setHasOptionsMenu(true)

        return binding.root
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_today -> {
                adapter.submitList(adapter.currentList.filter {
                    it.closeApproachDate == viewModel.getToday()
                })
            }
            R.id.show_next_week -> {
                adapter.submitList(viewModel.asteroids.value)
            }
            R.id.show_all->{
                adapter.submitList(viewModel.allAsteroids.value)
            }
            else -> return true
        }
        return true
    }

}
