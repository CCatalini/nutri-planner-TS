package com.austral.nutri_planner_ts.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.compose.ui.Modifier
import androidx.navigation.compose.composable
import com.austral.nutri_planner_ts.api.Api
import com.austral.nutri_planner_ts.ui.screens.day.Day
import com.austral.nutri_planner_ts.ui.screens.profile.Profile
import com.austral.nutri_planner_ts.ui.screens.recipe.Recipes
import com.austral.nutri_planner_ts.ui.screens.week.Week
import com.austral.nutri_planner_ts.ui.theme.Dimensions.PaddingLarge


@Composable
fun NavHostComposable(innerPadding: PaddingValues, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = ScreenNames.API.name,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(PaddingLarge)
    ){
        composable(route = ScreenNames.API.name) {
            Api()
        }
        composable(route = ScreenNames.Day.name) {
            Day()
        }
        composable(route = ScreenNames.Week.name) { Week() }
        composable(route = ScreenNames.Recipes.name) { Recipes() }
        composable(route = ScreenNames.Profile.name) { Profile() }
    }
}




      //  composable(route = NutriPlannerScreen.Day.name) {
      //      val dayViewModel: DayViewModel = viewModel()
      //      DayScreen(viewModel = dayViewModel)
      //  }
