package com.example.scotiapracticaltest.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scotiapracticaltest.R
import com.example.scotiapracticaltest.data.model.Repository
import com.example.scotiapracticaltest.ui.common.CommonAppBar
import com.example.scotiapracticaltest.ui.common.CommonProfileSection
import com.example.scotiapracticaltest.ui.common.CommonRepositoryList
import com.example.scotiapracticaltest.ui.common.CommonSearchBar
import com.example.scotiapracticaltest.viewmodel.RepositoryListModel

@Composable
fun RepositoryListActivity(
    repositoryListModel: RepositoryListModel,
    onRepositoryClick: (Repository) -> Unit
) {
    val repositories by repositoryListModel.repositories.collectAsState()
    val loading by repositoryListModel.isLoading.collectAsState()
    val user by repositoryListModel.user.collectAsState()
    val showProfile by repositoryListModel.showProfile.collectAsState()
    val showList by repositoryListModel.showList.collectAsState()
    val uniqueAnimationKey by repositoryListModel.uniqueAnimationKey.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = { CommonAppBar(title = stringResource(id = R.string.repository_list)) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp),
            verticalArrangement = Arrangement.Top,
        ) {
            // Search bar
            CommonSearchBar(
                textFieldValue = repositoryListModel.userId.value,
                onValueChange = { repositoryListModel.userId.value = it },
                onSearchClick = {
                    keyboardController?.hide()
                    repositoryListModel.performSearch()
                })

            Spacer(modifier = Modifier.height(20.dp))

            // Profile section
            key(uniqueAnimationKey) {
                CommonProfileSection(user = user, showProfile = showProfile)
            }

            // Repository list
            key(uniqueAnimationKey) {
                CommonRepositoryList(
                    repositories = repositories,
                    showList = showList,
                    loading = loading,
                    onRepositoryClick = onRepositoryClick
                )
            }
        }
    }
}