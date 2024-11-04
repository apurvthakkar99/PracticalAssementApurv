package com.example.scotiapracticaltest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.scotiapracticaltest.navigation.AppNavigation
import com.example.scotiapracticaltest.singelton.AppContainer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppContainer.initializeRepositoryListModel(applicationContext)

        setContent {
            MaterialTheme {
                Surface {
                    AppNavigation()
                }
            }
        }
    }
}