package com.kova700.zerotomvvm.util

sealed class NetworkResult<T>(open val data: T)
data class Success<T>(
    override val data: T,
    val isLast :Boolean
) : NetworkResult<T>(data)

data class Failure<T>(
    override val data: T,
    val exception: Throwable
) : NetworkResult<T>(data)