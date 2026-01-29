package com.heroesandmore.app.util

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)

    fun isSuccess() = this is Success
    fun isError() = this is Error
    fun isLoading() = this is Loading

    fun getOrNull(): T? = data
    fun getOrThrow(): T = data ?: throw IllegalStateException(message ?: "No data available")

    fun <R> map(transform: (T) -> R): Resource<R> {
        return when (this) {
            is Success -> Success(transform(data!!))
            is Error -> Error(message!!, data?.let { transform(it) })
            is Loading -> Loading(data?.let { transform(it) })
        }
    }

    companion object {
        fun <T> success(data: T) = Success(data)
        fun <T> error(message: String, data: T? = null) = Error(message, data)
        fun <T> loading(data: T? = null) = Loading(data)
    }
}

suspend fun <T> safeApiCall(apiCall: suspend () -> retrofit2.Response<T>): Resource<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            response.body()?.let {
                Resource.success(it)
            } ?: Resource.error("Empty response body")
        } else {
            Resource.error("Error: ${response.code()} ${response.message()}")
        }
    } catch (e: Exception) {
        Resource.error(e.message ?: "Unknown error occurred")
    }
}
