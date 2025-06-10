package com.austral.nutri_planner_ts.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.res.stringResource
import com.austral.nutri_planner_ts.R
import com.austral.nutri_planner_ts.ui.theme.Dimensions
import com.austral.nutri_planner_ts.ui.screens.profile.UserProfile
import com.austral.nutri_planner_ts.ui.screens.profile.Gender
import com.austral.nutri_planner_ts.ui.screens.profile.ActivityLevel
import com.austral.nutri_planner_ts.ui.screens.profile.Goal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileDialog(
    profile: UserProfile,
    onDismiss: () -> Unit,
    onSave: (UserProfile) -> Unit
) {
    var age by remember { mutableStateOf(profile.age.toString()) }
    var height by remember { mutableStateOf(profile.height.toString()) }
    var weight by remember { mutableStateOf(profile.weight.toString()) }
    var gender by remember { mutableStateOf(profile.gender) }
    var activityLevel by remember { mutableStateOf(profile.activityLevel) }
    var goal by remember { mutableStateOf(profile.goal) }
    var targetWeight by remember { mutableStateOf(profile.targetWeight.toString()) }
    var timeFrame by remember { mutableStateOf(profile.timeFrame.toString()) }
    var weeklyWorkouts by remember { mutableStateOf(profile.weeklyWorkouts.toString()) }
    
    val genderOptions = Gender.values().toList()
    val activityOptions = ActivityLevel.values().toList()
    val goalOptions = Goal.values().toList()
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.DialogPadding),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = Dimensions.CardElevationDialog)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimensions.DialogContentPadding),
                verticalArrangement = Arrangement.spacedBy(Dimensions.DialogFieldSpacing)
            ) {
                Text(
                    text = stringResource(R.string.edit_profile_title),
                    fontSize = Dimensions.FontSizeTitle,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimensions.DialogButtonSpacing)
                ) {
                    OutlinedTextField(
                        value = age,
                        onValueChange = { age = it },
                        label = { Text(stringResource(R.string.edit_profile_age_label)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    
                    OutlinedTextField(
                        value = height,
                        onValueChange = { height = it },
                        label = { Text(stringResource(R.string.edit_profile_height_label)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
                
                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text(stringResource(R.string.edit_profile_weight_label)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Gender dropdown
                var genderExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = genderExpanded,
                    onExpandedChange = { genderExpanded = !genderExpanded }
                ) {
                    OutlinedTextField(
                        value = stringResource(gender.stringResourceId),
                        onValueChange = { },
                        readOnly = true,
                        label = { Text(stringResource(R.string.edit_profile_gender_label)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = genderExpanded,
                        onDismissRequest = { genderExpanded = false },
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.secondary)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(4.dp)
                            )
                    ) {
                        genderOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { 
                                    Text(
                                        text = stringResource(option.stringResourceId),
                                        color = MaterialTheme.colorScheme.onSecondary
                                    ) 
                                },
                                onClick = {
                                    gender = option
                                    genderExpanded = false
                                },
                                colors = MenuDefaults.itemColors(
                                    textColor = MaterialTheme.colorScheme.onSecondary
                                )
                            )
                        }
                    }
                }
                
                // Activity level dropdown
                var activityExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = activityExpanded,
                    onExpandedChange = { activityExpanded = !activityExpanded }
                ) {
                    OutlinedTextField(
                        value = stringResource(activityLevel.stringResourceId),
                        onValueChange = { },
                        readOnly = true,
                        label = { Text(stringResource(R.string.edit_profile_activity_level_label)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = activityExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = activityExpanded,
                        onDismissRequest = { activityExpanded = false },
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.secondary)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(4.dp)
                            )
                    ) {
                        activityOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { 
                                    Text(
                                        text = stringResource(option.stringResourceId),
                                        color = MaterialTheme.colorScheme.onSecondary
                                    ) 
                                },
                                onClick = {
                                    activityLevel = option
                                    activityExpanded = false
                                },
                                colors = MenuDefaults.itemColors(
                                    textColor = MaterialTheme.colorScheme.onSecondary
                                )
                            )
                        }
                    }
                }
                
                // Goal dropdown
                var goalExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = goalExpanded,
                    onExpandedChange = { goalExpanded = !goalExpanded }
                ) {
                    OutlinedTextField(
                        value = stringResource(goal.stringResourceId),
                        onValueChange = { },
                        readOnly = true,
                        label = { Text(stringResource(R.string.edit_profile_goal_label)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = goalExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = goalExpanded,
                        onDismissRequest = { goalExpanded = false },
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.secondary)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(4.dp)
                            )
                    ) {
                        goalOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { 
                                    Text(
                                        text = stringResource(option.stringResourceId),
                                        color = MaterialTheme.colorScheme.onSecondary
                                    ) 
                                },
                                onClick = {
                                    goal = option
                                    goalExpanded = false
                                },
                                colors = MenuDefaults.itemColors(
                                    textColor = MaterialTheme.colorScheme.onSecondary
                                )
                            )
                        }
                    }
                }
                
                // Additional fields for goal-specific data
                if (goal != Goal.MAINTAIN_WEIGHT) {
                    OutlinedTextField(
                        value = targetWeight,
                        onValueChange = { targetWeight = it },
                        label = { Text(stringResource(R.string.edit_profile_target_weight_label)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    OutlinedTextField(
                        value = timeFrame,
                        onValueChange = { timeFrame = it },
                        label = { Text(stringResource(R.string.edit_profile_time_frame_label)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                OutlinedTextField(
                    value = weeklyWorkouts,
                    onValueChange = { weeklyWorkouts = it },
                    label = { Text(stringResource(R.string.edit_profile_weekly_workouts_label)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = stringResource(R.string.edit_profile_cancel),
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(Dimensions.DialogButtonSpacing))
                    
                    Button(
                        onClick = {
                            val updatedProfile = UserProfile(
                                age = age.toIntOrNull() ?: profile.age,
                                gender = gender,
                                weight = weight.toFloatOrNull() ?: profile.weight,
                                height = height.toFloatOrNull() ?: profile.height,
                                activityLevel = activityLevel,
                                goal = goal,
                                targetWeight = targetWeight.toFloatOrNull() ?: profile.targetWeight,
                                timeFrame = timeFrame.toIntOrNull() ?: profile.timeFrame,
                                weeklyWorkouts = weeklyWorkouts.toIntOrNull() ?: profile.weeklyWorkouts
                            )
                            onSave(updatedProfile)
                        }
                    ) {
                        Text(stringResource(R.string.edit_profile_save))
                    }
                }
            }
        }
    }
} 