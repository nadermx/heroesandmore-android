package com.heroesandmore.app.data.dto.common

import com.google.gson.annotations.SerializedName

data class PaginatedResponse<T>(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<T>
)

data class CursorPaginatedResponse<T>(
    val next: String?,
    val previous: String?,
    val results: List<T>
)

data class ErrorResponse(
    val detail: String?,
    val errors: Map<String, List<String>>?
)
