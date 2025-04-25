package com.austral.nutri_planner_ts.navigation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.austral.nutri_planner_ts.R
import com.austral.nutri_planner_ts.ui.theme.Dimensions


@Composable
fun BottomBar(
    onNavigate: (String) -> Unit,
) {
    val items = listOf(
        TabBarItem(ScreenNames.Day.name, R.drawable.ic_day),
        TabBarItem(ScreenNames.Week.name, R.drawable.ic_week),
        TabBarItem(ScreenNames.Recipes.name, R.drawable.ic_recipes),
        TabBarItem(ScreenNames.Profile.name, R.drawable.ic_profile),
    )
    TabView(items = items, onNavigate = onNavigate)
}

data class TabBarItem(
    val title: String,
    @DrawableRes val icon: Int,
)

@Composable
fun TabView(
    items: List<TabBarItem>,
    onNavigate: (String) -> Unit,
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    Surface(
        color = MaterialTheme.colorScheme.secondary,
        shape = RoundedCornerShape(
            topStart = Dimensions.CornerRadiusMedium,
            topEnd = Dimensions.CornerRadiusMedium
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.PaddingSmall),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                TabItem(
                    item = item,
                    isSelected = selectedTabIndex == index,
                    onClick = {
                        selectedTabIndex = index
                        onNavigate(item.title)
                    }
                )
            }
        }
    }
}

@Composable
fun TabItem(
    item: TabBarItem,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.surface

    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(Dimensions.PaddingSmall)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(Dimensions.CornerRadiusMedium)
            )
            .padding(
                horizontal = Dimensions.PaddingMedium,
                vertical = Dimensions.PaddingSmall
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = item.icon),
            contentDescription = item.title,
            tint = contentColor,
            modifier = Modifier.size(Dimensions.IconSizeLarge)
        )
        Spacer(modifier = Modifier.height(Dimensions.SpacerTiny))
        Text(
            text = item.title,
            color = contentColor,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
