package com.example.jawwy.alert

import java.time.LocalDateTime

interface AlertScheduler {
    fun schedule(time:LocalDateTime)
    fun cancel(time:LocalDateTime)
}