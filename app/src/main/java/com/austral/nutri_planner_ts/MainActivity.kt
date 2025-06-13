package com.austral.nutri_planner_ts

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.austral.nutri_planner_ts.navigation.BottomBar
import com.austral.nutri_planner_ts.navigation.NavHostComposable
import com.austral.nutri_planner_ts.ui.theme.NutriplannerTSTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.austral.nutri_planner_ts.ui.viewmodel.AuthViewModel

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutriplannerTSTheme {
                val context = LocalContext.current
                val authViewModel = hiltViewModel<AuthViewModel>()
                val isAuthenticated by authViewModel.isAuthenticated.collectAsStateWithLifecycle()

                // Launch the authentication prompt when the composable is first composed
                LaunchedEffect(Unit) {
                    authViewModel.authenticate(context)
                }

                if (isAuthenticated) {
                    val navController = rememberNavController()
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = { BottomBar(navController::navigate) }
                    ) { innerPadding ->
                        NavHostComposable(innerPadding, navController)
                    }
                } else {
                    Text(text = "Autenticando...")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NutriplannerTSTheme {
        Greeting("Android")
    }
}

