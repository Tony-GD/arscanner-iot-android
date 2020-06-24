package com.griddynamics.connectedapps.util

import java.text.SimpleDateFormat

private const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"


fun getTimestamp(date: String): Long {
    return SimpleDateFormat(DATE_FORMAT)
        .parse(date).time
}