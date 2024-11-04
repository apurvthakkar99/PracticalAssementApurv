package com.example.scotiapracticaltest.data.repository

import com.example.scotiapracticaltest.data.model.Repository
import com.example.scotiapracticaltest.data.model.User
import com.example.scotiapracticaltest.data.network.ApiService

class GithubRepository(private val apiService: ApiService) {
    suspend fun getUser(userId: String): Result<User> = safeApiCall { apiService.getUser(userId) }
    suspend fun getUserRepos(userId: String): Result<List<Repository>> =
        safeApiCall { apiService.getUserRepos(userId) }

    private suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> = try {
        Result.success(apiCall())
    } catch (e: Exception) {
        Result.failure(e)
    }
}