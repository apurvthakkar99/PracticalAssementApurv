package com.example.scotiapracticaltest.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.scotiapracticaltest.R
import com.example.scotiapracticaltest.data.model.Repository
import com.example.scotiapracticaltest.ui.common.CommonAppBar
import com.example.scotiapracticaltest.ui.common.CommonRepositoryInfo
import com.example.scotiapracticaltest.ui.common.CommonRepositoryStats

@Composable
fun RepositoryDetailActivity(repository: Repository, navController: NavHostController) {
    Scaffold(
        topBar = { DetailAppBar(navController = navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CommonRepositoryInfo(repository = repository)
            Spacer(modifier = Modifier.height(16.dp))
            CommonRepositoryStats(repository = repository)
        }
    }
}

@Composable
fun DetailAppBar(navController: NavHostController) {
    CommonAppBar(title = stringResource(id = R.string.repository_detail), onBackPressed = { navController.popBackStack() })
}