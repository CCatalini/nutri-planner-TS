package com.austral.nutri_planner_ts.ui.components

import androidx.compose.foundation.layout.*
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
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Edit Profile",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = age,
                        onValueChange = { age = it },
                        label = { Text("Age") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    
                    OutlinedTextField(
                        value = height,
                        onValueChange = { height = it },
                        label = { Text("Height (cm)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
                
                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Weight (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Gender dropdown
                var genderExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = genderExpanded,
                    onExpandedChange = { genderExpanded = !genderExpanded }
                ) {
                    OutlinedTextField(
                        value = gender.name,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Gender") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = genderExpanded,
                        onDismissRequest = { genderExpanded = false }
                    ) {
                        genderOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.name) },
                                onClick = {
                                    gender = option
                                    genderExpanded = false
                                }
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
                        value = activityLevel.description,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Activity Level") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = activityExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = activityExpanded,
                        onDismissRequest = { activityExpanded = false }
                    ) {
                        activityOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.description) },
                                onClick = {
                                    activityLevel = option
                                    activityExpanded = false
                                }
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
                        value = goal.description,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Goal") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = goalExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = goalExpanded,
                        onDismissRequest = { goalExpanded = false }
                    ) {
                        goalOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.description) },
                                onClick = {
                                    goal = option
                                    goalExpanded = false
                                }
                            )
                        }
                    }
                }
                
                // Additional fields for goal-specific data
                if (goal != Goal.MAINTAIN_WEIGHT) {
                    OutlinedTextField(
                        value = targetWeight,
                        onValueChange = { targetWeight = it },
                        label = { Text("Target Weight (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    OutlinedTextField(
                        value = timeFrame,
                        onValueChange = { timeFrame = it },
                        label = { Text("Time Frame (weeks)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                OutlinedTextField(
                    value = weeklyWorkouts,
                    onValueChange = { weeklyWorkouts = it },
                    label = { Text("Weekly Workouts") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
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
                        Text("Save")
                    }
                }
            }
        }
    }
} 