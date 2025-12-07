package com.ivaniuk.algolearnsimple

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ivaniuk.algolearnsimple.core.theme.AlgoLearnTheme
import com.ivaniuk.algolearnsimple.presentation.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AlgoLearnTheme {
                AppNavigation(appContainer = AppContainer())
            }
        }
    }
}