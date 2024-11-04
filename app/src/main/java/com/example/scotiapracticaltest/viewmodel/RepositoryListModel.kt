package com.example.scotiapracticaltest.viewmodel

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scotiapracticaltest.data.repository.GithubRepository
import com.example.scotiapracticaltest.data.model.Repository
import com.example.scotiapracticaltest.data.model.User
import com.example.scotiapracticaltest.singelton.AppContainer
import com.example.scotiapracticaltest.ui.common.RealToastNotifier
import com.example.scotiapracticaltest.ui.common.ToastNotifier
import com.example.scotiapracticaltest.utils.isInternetAvailable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class RepositoryListModel(
    private val applicationContext: Context,
    private val toastNotifier: ToastNotifier = RealToastNotifier(applicationContext),
    private val isTest: Boolean = false
) : ViewModel() {

    // User input for GitHub user ID
    val userId = mutableStateOf("")
    var lastSearchId: String? = null // Keep track of the last searched user ID

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    // Additional state to reset animations
    private val _uniqueAnimationKey = MutableStateFlow(UUID.randomUUID().toString())
    val uniqueAnimationKey: StateFlow<String> = _uniqueAnimationKey

    private val _repositories = MutableStateFlow<List<Repository>>(emptyList())
    val repositories: StateFlow<List<Repository>> = _repositories

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _showProfile = MutableStateFlow(false)
    val showProfile: StateFlow<Boolean> = _showProfile

    private val _showList = MutableStateFlow(false)
    val showList: StateFlow<Boolean> = _showList

    var repository: GithubRepository = AppContainer.githubRepository

    // Method to perform search and set visibility flags
    fun performSearch() {
        // Check if the current input is the same as the last search input
        if (userId.value == lastSearchId) {
            // Skip API call if there's no change in input
            return
        }

        lastSearchId = userId.value // Update the last search ID

        viewModelScope.launch {
            if (!isTest && !isInternetAvailable(context = applicationContext)) {
                _isLoading.value = false
                toastNotifier.showToast("Internet is not available!")
                return@launch
            }

            if (userId.value.isNotBlank()) {
                resetAnimations()

                _isLoading.value = true
                // Fetch user data
                fetchUserData()
                //Loading delay between profile info and repo list
                delay(400)
                // Fetch repositories
                fetchUserRepositories()

                _isLoading.value = false
            }
        }
    }

    suspend fun fetchUserData() {
        repository.getUser(userId.value).onSuccess { user ->
            _user.value = user
            _showProfile.value = true
        }.onFailure { error ->
            toastNotifier.showToast("Failed to fetch user: ${error.message}")
        }
    }

    suspend fun fetchUserRepositories() {
        repository.getUserRepos(userId.value).onSuccess { repos ->
            _repositories.value = repos
            _showList.value = true
        }.onFailure { error ->
            toastNotifier.showToast("Failed to fetch repositories: ${error.message}")
        }
    }

    fun getRepositoryById(id: Int?): Repository? {
        return _repositories.value.find { it.id == id }
    }

    fun resetAnimations() {
        _showProfile.value = false
        _showList.value = false
        _uniqueAnimationKey.value =
            UUID.randomUUID().toString() // Force re-composition with a new key
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun setTestData(repositories: List<Repository>, showList: Boolean) {
        _repositories.value = repositories
        _showList.value = showList
    }
}

