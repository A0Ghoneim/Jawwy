package com.example.jawwy.search.ViewModel

import com.example.jawwy.model.searchdata.Features

sealed
class ApiState {
    class Success(val data:List<Features>) : ApiState()
    class Failure(val msg:Throwable) : ApiState()
    object Loading : ApiState()
}
