package com.example.jawwy.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jawwy.favourites.viewholder.FavouriteViewModel
import com.example.jawwy.model.repo.IWeatherRepository

class LocationViewModelFactory(private val repository: IWeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LocationViewModel::class.java)){
            LocationViewModel(repository) as T
        }
        else{
            throw IllegalArgumentException("illegal")
        }
    }
}