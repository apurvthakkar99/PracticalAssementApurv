package com.example.scotiapracticaltest.singelton

import android.content.Context
import com.example.scotiapracticaltest.data.network.ApiService
import com.example.scotiapracticaltest.data.network.RetrofitInstance
import com.example.scotiapracticaltest.data.repository.GithubRepository
import com.example.scotiapracticaltest.viewmodel.RepositoryListModel

object AppContainer {
    // Initialize Retrofit instance
    private val retrofitInstance: RetrofitInstance by lazy {
        RetrofitInstance
    }

    // Provide ApiService instance
    private val apiService: ApiService by lazy {
        retrofitInstance.api
    }

    // Provide GithubRepository instance
    val githubRepository: GithubRepository by lazy {
        GithubRepository(apiService)
    }

    // Lateinit to hold RepositoryListModel; initialized with application context
    lateinit var repositoryListModel: RepositoryListModel

    fun initializeRepositoryListModel(context: Context) {
        if (!::repositoryListModel.isInitialized) {
            repositoryListModel = RepositoryListModel(context)
        }
    }
}