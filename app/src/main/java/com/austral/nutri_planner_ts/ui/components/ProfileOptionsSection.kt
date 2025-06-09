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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Options",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            ProfileOption(
                icon = Icons.Default.Edit,
                title = "Edit Profile",
                subtitle = "Update your personal information",
                onClick = onEditProfile
            )
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            ProfileOption(
                icon = Icons.Default.Settings,
                title = "Settings",
                subtitle = "App preferences and notifications",
                onClick = onSettings
            )
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            ProfileOption(
                icon = Icons.Default.Phone,
                title = "Help & Support",
                subtitle = "Get help and contact support",
                onClick = onHelp
            )
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            ProfileOption(
                icon = Icons.Default.Info,
                title = "About",
                subtitle = "App version and information",
                onClick = onAbout
            )
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
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Navigate",
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
} 