package com.austral.nutri_planner_ts.user

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.austral.nutri_planner_ts.R
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import com.austral.nutri_planner_ts.ui.theme.Dimensions

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun UserSection() {
    val viewModel = hiltViewModel<UserViewModel>()
    val userState = viewModel.userData.collectAsStateWithLifecycle()

    if (userState.value == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            val context = LocalContext.current
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Dimensions.PaddingLarge, vertical = Dimensions.PaddingLarge),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(Dimensions.SpacerLarge))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = null,
                        modifier = Modifier.size(Dimensions.IconSizeExtraLarge)
                    )
                    Spacer(modifier = Modifier.width(Dimensions.SpacerSmall))
                    Text(
                        text = stringResource(id = R.string.app_name_display),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.height(Dimensions.SpacerMedium))

                Text(
                    text = stringResource(id = R.string.login_welcome),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                )

                Spacer(modifier = Modifier.weight(1f))

                GoogleLoginButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = { viewModel.launchCredentialManager(context) }
                )
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = userState.value?.photoUrl,
                contentDescription = null,
                modifier = Modifier.size(Dimensions.ProfileAvatarSize),
            )
            Spacer(modifier = Modifier.height(Dimensions.HeightLarge))
            Text(text = userState.value?.displayName ?: "", color = MaterialTheme.colorScheme.onBackground)
            Text(text = userState.value?.email ?: "", color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(Dimensions.SpacerLarge))
            ElevatedButton(onClick = { viewModel.signOut() }) {
                Text(stringResource(id = R.string.button_sign_out))
            }
        }
    }
}

@Composable
private fun GoogleLoginButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    GoogleButtonUI(
        modifier = modifier,
        onClick = onClick,
    )
}

@Composable
private fun GoogleButtonUI(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    ElevatedButton(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = RoundedCornerShape(Dimensions.PaddingSmallMed),
        elevation = ButtonDefaults.elevatedButtonElevation(),
        contentPadding = PaddingValues(horizontal = Dimensions.PaddingLarge, vertical = Dimensions.PaddingMedium)
    ) {
        Text(
            stringResource(id = R.string.button_continue_google),
            style = MaterialTheme.typography.titleSmall)
    }
} 