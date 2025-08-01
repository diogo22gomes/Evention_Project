package com.example.evention

import UserPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.evention.di.NetworkModule
import com.example.evention.ui.navigation.AppNavHost
import com.example.evention.ui.theme.EventionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userPreferences = UserPreferences(applicationContext)
        NetworkModule.init(userPreferences)
        enableEdgeToEdge()
        setContent {
            EventionTheme {
                AppNavHost()
            }
        }
    }
}