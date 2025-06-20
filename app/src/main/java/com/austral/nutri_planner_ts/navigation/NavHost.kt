package com.austral.nutri_planner_ts.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.compose.ui.Modifier
import androidx.navigation.compose.composable
import com.austral.nutri_planner_ts.ui.screens.day.Day
import com.austral.nutri_planner_ts.ui.screens.profile.Profile
import com.austral.nutri_planner_ts.ui.screens.recipe.Recipes
import com.austral.nutri_planner_ts.ui.theme.Dimensions.PaddingLarge


@Composable
fun NavHostComposable(innerPadding: PaddingValues, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = ScreenNames.Day.name,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(PaddingLarge)
    ){
        composable(route = ScreenNames.Day.name) { Day(navController) }
        composable(route = ScreenNames.Recipes.name) { Recipes() }
        composable(route = ScreenNames.Profile.name) { Profile() }
        composable(route = ScreenNames.Notification.name) { com.austral.nutri_planner_ts.ui.screens.notification.NotificationScreen() }
    }
}


