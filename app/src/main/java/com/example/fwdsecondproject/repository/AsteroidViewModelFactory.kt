package com.example.fwdsecondproject.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fwdsecondproject.main.MainViewModel

class AsteroidViewModelFactory constructor(private val repository: AsteroidRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(this.repository) as T
    }
}
