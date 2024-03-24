package com.example.jawwy.alert.viewmodel

import com.example.jawwy.alert.AlertItem
import com.example.jawwy.model.data.JsonPojo

sealed
class AlertApiState {
    class Success(val data: List<AlertItem>) : AlertApiState()
    class Failure(val msg:Throwable) : AlertApiState()
    object Loading : AlertApiState()
}