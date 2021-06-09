package com.industryproject.connfy.networkManager

interface NetworkListener<T> {
    fun getResult(`object`: T)
}