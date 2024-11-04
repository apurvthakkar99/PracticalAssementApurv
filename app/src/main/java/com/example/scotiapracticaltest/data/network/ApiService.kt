package com.example.scotiapracticaltest.data.network

import com.example.scotiapracticaltest.data.model.Repository
import com.example.scotiapracticaltest.data.model.User
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("users/{userId}")
    suspend fun getUser(@Path("userId") userId: String): User

    @GET("users/{userId}/repos")
    suspend fun getUserRepos(@Path("userId") userId: String): List<Repository>
}