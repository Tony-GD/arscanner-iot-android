package com.griddynamics.connectedapps.util

import com.griddynamics.connectedapps.gateway.api.ApiResponse
import com.griddynamics.connectedapps.gateway.api.ApiSuccessResponse

fun <T> ApiResponse<T>.unwrapApiResponse(): T? {
    return when (this) {
        is ApiSuccessResponse<T> -> this.body
        else -> null
    }
}
