package com.austral.nutri_planner_ts.ui.screens.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.austral.nutri_planner_ts.R
import com.austral.nutri_planner_ts.ui.components.CalorieChartCard
import com.austral.nutri_planner_ts.ui.components.EditProfileDialog
import com.austral.nutri_planner_ts.ui.components.ProfileOptionsSection
import com.austral.nutri_planner_ts.ui.theme.Dimensions
import com.austral.nutri_planner_ts.user.UserViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.ui.platform.LocalContext
import com.austral.nutri_planner_ts.ui.components.MacroGoalsCard

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile() {
    val viewModel = hiltViewModel<ProfileViewModel>()
    val activity = LocalContext.current as androidx.activity.ComponentActivity
    val userViewModel = hiltViewModel<UserViewModel>(activity)
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val firebaseUser = userViewModel.userData.collectAsStateWithLifecycle().value
    val userName = firebaseUser?.displayName ?: stringResource(R.string.profile_user_name)
    
    when (uiState) {
        is ProfileUiState.Loading -> {
            LoadingScreen()
        }
        is ProfileUiState.Error -> {
            ErrorScreen(
                message = uiState.message,
                onRetry = { /* Retry logic */ }
            )
        }
        is ProfileUiState.Success -> {
            ProfileContent(
                uiState = uiState,
                onUpdateProfile = viewModel::updateProfile,
                onGenerateRecommendation = viewModel::generateMacroRecommendation,
                onLogout = userViewModel::signOut,
                userName = userName
            )
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = Dimensions.ProfileLoadingStrokeWidth
        )
    }
}

@Composable
private fun ErrorScreen(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimensions.SpacerMedium)
        ) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
            
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = stringResource(R.string.retry_button),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileContent(
    uiState: ProfileUiState.Success,
    onUpdateProfile: (UserProfile) -> Unit,
    onGenerateRecommendation: () -> Unit,
    onLogout: () -> Unit,
    userName: String
) {
    var showEditDialog by remember { mutableStateOf(false) }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = Dimensions.PaddingMedium),
        verticalArrangement = Arrangement.spacedBy(Dimensions.SpacerMedium)
    ) {
        // Header
        item {
            ProfileHeader()
        }
        
        // Calorie Chart Section
        item {
            CalorieChartCard(
                recommendation = uiState.macroRecommendation,
                dailyHistory = uiState.dailyHistory
            )
        }
        
        // User Info Section
        item {
            UserInfoSection(userName = userName)
        }
        
        // Macro Goals Section (shows current recommended macros)
        item {
            uiState.macroRecommendation?.let { rec ->
                MacroGoalsCard(
                    recommendation = rec,
                    targetWeight = uiState.userProfile.targetWeight
                )
            }
        }
        
        // Profile Options
        item {
            ProfileOptionsSection(
                profile = uiState.userProfile,
                recommendation = uiState.macroRecommendation,
                isGenerating = uiState.isGeneratingRecommendation,
                onGenerateRecommendation = onGenerateRecommendation,
                onEditProfile = { showEditDialog = true }
            )
        }
        
        // Logout Button
        item {
            Spacer(modifier = Modifier.height(Dimensions.SpacerMedium))
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimensions.PaddingMedium),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                border = BorderStroke(Dimensions.fineLine, MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Filled.ExitToApp,
                        contentDescription = stringResource(id = R.string.button_sign_out)
                    )
                    Spacer(modifier = Modifier.width(Dimensions.SpacerSmall))
                    Text(stringResource(id = R.string.button_sign_out))
                }
            }
        }
        
        // Bottom spacing for navigation
        item {
            Spacer(modifier = Modifier.height(Dimensions.ProfileBottomSpacing))
        }
    }
    
    // Edit Profile Dialog
    if (showEditDialog) {
        EditProfileDialog(
            profile = uiState.userProfile,
            onDismiss = { showEditDialog = false },
            onSave = { profile ->
                onUpdateProfile(profile)
                showEditDialog = false
            }
        )
    }
}

@Composable
private fun ProfileHeader() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(Dimensions.SpacerSmall))
        
        Text(
            text = stringResource(R.string.profile_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun UserInfoSection(userName: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimensions.PaddingMedium, vertical = Dimensions.SpacerMedium),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = userName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(Dimensions.SpacerSmall))
        Divider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f))
    }
}

fun UserProfile.isComplete(): Boolean {
    return age > 0 && weight > 0 && height > 0 && 
           (goal == Goal.MAINTAIN_WEIGHT || (targetWeight > 0 && timeFrame > 0))
}