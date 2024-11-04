package com.example.scotiapracticaltest.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.scotiapracticaltest.singelton.AppContainer
import com.example.scotiapracticaltest.ui.main.RepositoryDetailActivity
import com.example.scotiapracticaltest.ui.main.RepositoryListActivity

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val repositoryListModel = AppContainer.repositoryListModel

    NavHost(navController = navController, startDestination = "repositoryList") {
        composable("repositoryList") {
            RepositoryListActivity(repositoryListModel) { repository ->
                navController.navigate("repositoryDetail/${repository.id}")
            }
        }

        composable(
            "repositoryDetail/{repositoryId}",
            arguments = listOf(navArgument("repositoryId") { type = NavType.IntType })
        ) { backStackEntry ->
            val repositoryId = backStackEntry.arguments?.getInt("repositoryId")
            val repository = repositoryListModel.getRepositoryById(repositoryId)
            repository?.let {
                RepositoryDetailActivity(repository = it, navController)
            }
        }
    }
}
