package com.griddynamics.connectedapps.util

import com.griddynamics.connectedapps.gateway.network.api.ApiResponse
import com.griddynamics.connectedapps.gateway.network.api.ApiSuccessResponse

fun <T> ApiResponse<T>.unwrapApiResponse(): T? {
    return when (this) {
        is ApiSuccessResponse<T> -> this.body
        else -> null
    }
}
