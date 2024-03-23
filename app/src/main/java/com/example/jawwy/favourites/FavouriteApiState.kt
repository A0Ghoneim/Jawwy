package com.example.jawwy.favourites

import com.example.jawwy.model.data.JsonPojo

sealed
class FavouriteApiState {
    class Success(val data: List<JsonPojo>) : FavouriteApiState()
    class Failure(val msg:Throwable) : FavouriteApiState()
    object Loading : FavouriteApiState()
}