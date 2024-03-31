package com.example.jawwy.connectivity

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observe(): Flow<Status>

    fun getCurrentStatus():ConnectivityObserver.Status

    enum class Status {
        Available, Unavailable, Losing, Lost
    }
}