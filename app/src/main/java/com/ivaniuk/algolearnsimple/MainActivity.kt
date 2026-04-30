package com.ivaniuk.algolearnsimple

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import com.ivaniuk.algolearnsimple.core.theme.AlgoLearnTheme
import com.ivaniuk.algolearnsimple.data.local.HistoryDatabase
import com.ivaniuk.algolearnsimple.presentation.navigation.AppNavigation
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AlgoLearnTheme {
                AppNavigation()
            }
        }
    }
}