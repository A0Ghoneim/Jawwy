package com.example.jawwy.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jawwy.favourites.viewholder.FavouriteViewModel
import com.example.jawwy.model.repo.IWeatherRepository

class SettingsViewModelFactory(private val repository: IWeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SettingsViewModel::class.java)){
            SettingsViewModel(repository) as T
        }
        else{
            throw IllegalArgumentException("illegal")
        }
    }
}