package com.example.jawwy.favourites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jawwy.model.repo.IWeatherRepository
import kotlinx.coroutines.Dispatchers

class FavouriteViewModelFactory(private val repository: IWeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavouriteViewModel::class.java)){
            FavouriteViewModel(repository,Dispatchers.IO) as T
        }
        else{
            throw IllegalArgumentException("illegal")
        }
    }
}