package com.learningnotes.service.exception

import java.text.SimpleDateFormat


class ExceptionResponse(
    val status: Int,
    val message: String?,
    private val timeStamp: Long
) {
    fun getTimeStamp(): String = SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
        .format(timeStamp)
}