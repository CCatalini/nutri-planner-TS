package com.austral.nutri_planner_ts.ui.screens.notification

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.austral.nutri_planner_ts.notification.ScheduleNotificationViewModel
import com.austral.nutri_planner_ts.R
import com.austral.nutri_planner_ts.ui.theme.Dimensions
import androidx.hilt.navigation.compose.hiltViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationScreen() {

    // Permiso para mostrar notificaciones en Android 13+
    val postNotificationPermission = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    val viewModel: ScheduleNotificationViewModel = hiltViewModel()

    LaunchedEffect(key1 = true) {
        if (!postNotificationPermission.status.isGranted) {
            postNotificationPermission.launchPermissionRequest()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimensions.PaddingMedium),
        verticalArrangement = Arrangement.spacedBy(Dimensions.SpacerMedium, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { viewModel.scheduleOneTimeNotification() }) {
            Text(text = stringResource(R.string.schedule_notification_in_seconds, 3))
        }

        Button(onClick = { viewModel.scheduleDailyNotification() }) {
            Text(text = stringResource(R.string.schedule_daily_notification_at_time, "20:00"))
        }
    }
} 