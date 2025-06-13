package com.austral.nutri_planner_ts.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import com.austral.nutri_planner_ts.R
import com.austral.nutri_planner_ts.ui.theme.Dimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileOptionsSection(
    profile: com.austral.nutri_planner_ts.ui.screens.profile.UserProfile,
    recommendation: com.austral.nutri_planner_ts.ui.screens.profile.MacroRecommendation?,
    isGenerating: Boolean = false,
    onGenerateRecommendation: () -> Unit,
    onEditProfile: () -> Unit,
    onSettings: () -> Unit = {},
    onHelp: () -> Unit = {},
    onAbout: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimensions.CornerRadiusMedium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimensions.CardElevationDefault)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.PaddingMedium)
        ) {
            Text(
                text = stringResource(R.string.profile_options_title),
                fontSize = Dimensions.FontSizeSubtitle,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(bottom = Dimensions.PaddingMedium)
            )
            
            ProfileOption(
                icon = Icons.Default.Edit,
                title = stringResource(R.string.profile_option_edit_title),
                subtitle = stringResource(R.string.profile_option_edit_subtitle),
                onClick = onEditProfile
            )
            
            // No more options below.
        }
    }
}

@Composable
private fun ProfileOption(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = Dimensions.SpacerSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(Dimensions.ProfileOptionsIconSize)
        )
        
        Spacer(modifier = Modifier.width(Dimensions.PaddingMedium))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = Dimensions.FontSizeBody,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Text(
                text = subtitle,
                fontSize = Dimensions.FontSizeCaption,
                color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.7f)
            )
        }
        
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = stringResource(R.string.content_description_navigate),
            tint = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.6f),
            modifier = Modifier.size(Dimensions.ProfileOptionsArrowSize)
        )
    }
} 