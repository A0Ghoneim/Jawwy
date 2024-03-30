package com.example.jawwy.alert

import java.time.LocalDateTime

interface AlertScheduler {
    fun schedule(item:AlertItem)
    fun cancel(item: AlertItem)
}