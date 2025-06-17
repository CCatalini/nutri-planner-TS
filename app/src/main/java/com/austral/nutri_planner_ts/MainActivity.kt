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
import androidx.compose.ui.res.stringResource
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
import com.austral.nutri_planner_ts.user.UserViewModel
import com.austral.nutri_planner_ts.user.UserSection
import com.google.firebase.FirebaseApp

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            NutriplannerTSTheme {
                val context = LocalContext.current
                val authViewModel = hiltViewModel<AuthViewModel>()
                val userViewModel = hiltViewModel<UserViewModel>()
                val user by userViewModel.userData.collectAsStateWithLifecycle()
                val isAuthenticated by authViewModel.isAuthenticated.collectAsStateWithLifecycle()

                if (user == null) {
                    // Usuario no ha iniciado sesión con Google
                    UserSection() // muestra botón Google y controla sign-in
                } else {
                    // Una vez hay usuario Firebase, lanzamos biometría (si no se hizo aún)
                    LaunchedEffect(user) {
                        if (!isAuthenticated) authViewModel.authenticate(context)
                    }

                    if (!isAuthenticated) {
                        Text(text = stringResource(id = R.string.authenticate_biometrics))
                    } else {
                        val navController = rememberNavController()
                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            bottomBar = { BottomBar(navController::navigate) }
                        ) { innerPadding ->
                            NavHostComposable(innerPadding, navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = R.string.greeting_hello_name, name),
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

