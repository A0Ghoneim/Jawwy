package com.example.jawwy.search.ViewModel

import com.example.jawwy.model.searchdata.Features

sealed
class FeatureApiState {
    class Success(val data:List<Features>) : FeatureApiState()
    class Failure(val msg:Throwable) : FeatureApiState()
    object Loading : FeatureApiState()
}
