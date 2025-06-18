package com.austral.nutri_planner_ts.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import com.austral.nutri_planner_ts.R
import com.austral.nutri_planner_ts.ui.theme.Dimensions
import com.austral.nutri_planner_ts.ui.screens.profile.DailyEntry
import com.austral.nutri_planner_ts.ui.screens.profile.MacroRecommendation
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.PathEffect



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalorieChartCard(
    recommendation: MacroRecommendation? = null,
    dailyHistory: List<DailyEntry>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimensions.CalorieChartHeight),
        shape = RoundedCornerShape(Dimensions.CornerRadiusMedium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimensions.CardElevationDefault)
    ) {
        var selectedEntry by remember { mutableStateOf<DailyEntry?>(null) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimensions.PaddingMedium)
        ) {
            Text(
                text = stringResource(R.string.calorie_chart_title),
                fontSize = Dimensions.FontSizeSubtitle,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(bottom = Dimensions.PaddingMedium)
            )
            
            if (dailyHistory.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.chart_no_data),
                        fontSize = Dimensions.FontSizeCaption,
                        color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.7f)
                    )
                }
            } else {
                CalorieChartWithLabels(
                    data = dailyHistory.take(7).reversed(),
                    modifier = Modifier.fillMaxSize(),
                    onPointClick = { entry -> selectedEntry = entry }
                )
            }
        }

        // Bottom sheet with macro details
        if (selectedEntry != null) {
            ModalBottomSheet(
                onDismissRequest = { selectedEntry = null },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ) {
                MacroDetailsContent(entry = selectedEntry!!)
            }
        }
    }
}

@Composable
private fun CalorieChartWithLabels(
    data: List<DailyEntry>,
    modifier: Modifier = Modifier,
    onPointClick: (DailyEntry) -> Unit
) {


    Column(modifier = modifier) {
        val chartHeight = Modifier.weight(1f)

        Row(modifier = chartHeight.fillMaxWidth()) {
            // Y-axis labels fixed
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(Dimensions.CalorieChartYAxisLabelWidth),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                listOf(2500, 2250, 2000, 1750, 1500, 1250).forEach { value ->
                    Text(
                        text = value.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }

            // Chart and labels filling remaining width
            Column(modifier = Modifier.fillMaxWidth()) {
                CalorieChart(
                    data = data,
                    modifier = Modifier
                        .height(Dimensions.CalorieChartHeight)
                        .fillMaxWidth(),
                    onPointClick = onPointClick
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimensions.CalorieChartLabelPadding),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    data.forEach { entry ->
                        Text(
                            text = entry.date.takeLast(2),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CalorieChart(
    data: List<DailyEntry>,
    modifier: Modifier = Modifier,
    onPointClick: (DailyEntry) -> Unit
) {
    // Remember calculated points for click detection
    val points = remember(data) { mutableListOf<Pair<Offset, DailyEntry>>() }

    val primaryColor = MaterialTheme.colorScheme.primary
    val lineColor = primaryColor.copy(alpha = 0.5f)
    // Pre-compute colors that rely on MaterialTheme as they cannot be accessed inside Canvas draw scope
    val referenceLineColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)

    Canvas(modifier = modifier.pointerInput(data) {
        detectTapGestures { offset ->
            // find nearest point within tolerance
            val target = points.minByOrNull { (pt, _) -> (pt - offset).getDistance() }
            if (target != null && (target.first - offset).getDistance() < Dimensions.CalorieChartDashLength.toPx()) {
                onPointClick(target.second)
            }
        }
    }) {
        // Fixed display range 1200-2500 kcal as solicitado
        val minCalories = 1250f
        val maxCalories = 2500f
        val range = (maxCalories - minCalories).let { if (it == 0f) 1f else it }
        
        points.clear()
        points.addAll(data.mapIndexed { index, entry ->
            val x = if (data.size > 1) index.toFloat() / (data.size - 1) * size.width else size.width / 2f
            val y = size.height - ((entry.consumedCalories - minCalories) / range) * size.height
            Pair(Offset(x, y), entry)
        })
        
        // Draw horizontal reference lines
        val yLines = listOf(2500f, 2250f, 2000f, 1750f, 1500f, 1250f)
        yLines.forEach { value ->
            val y = size.height - ((value - minCalories) / range) * size.height
            drawLine(
                color = referenceLineColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = Dimensions.fineLine.toPx(),
                pathEffect = null
            )
        }

        if (points.isNotEmpty()) {
            if (points.size > 1) {
                for (i in 0 until points.size - 1) {
                    drawLine(
                        color = lineColor,
                        start = points[i].first,
                        end = points[i + 1].first,
                        strokeWidth = Dimensions.CalorieChartLineStroke.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(Dimensions.CalorieChartDashLength.toPx(), Dimensions.CalorieChartDashLength.toPx()), 0f)
                    )
                }
            }

            // Draw points (bigger)
            points.forEach { (pos, _) ->
                drawCircle(
                    color = primaryColor,
                    radius = Dimensions.CalorieChartPointRadius.toPx(),
                    center = pos
                )
            }
        }
    }
}

@Composable
private fun MacroDetailsContent(entry: DailyEntry) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.PaddingLarge),
        verticalArrangement = Arrangement.spacedBy(Dimensions.SpacerMedium)
    ) {
        Text(
            text = entry.date,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Divider()
        MacroRow(
            label = stringResource(R.string.calories_label),
            value = stringResource(
                R.string.macro_detail_kcal_format,
                entry.consumedCalories,
                entry.recommendedCalories
            )
        )
        MacroRow(
            label = stringResource(R.string.protein_label),
            value = stringResource(
                R.string.macro_detail_gram_format,
                entry.consumedProtein,
                entry.recommendedProtein
            )
        )
        MacroRow(
            label = stringResource(R.string.carbs_label),
            value = stringResource(
                R.string.macro_detail_gram_format,
                entry.consumedCarbs,
                entry.recommendedCarbs
            )
        )
        MacroRow(
            label = stringResource(R.string.fat_label),
            value = stringResource(
                R.string.macro_detail_gram_format,
                entry.consumedFat,
                entry.recommendedFat
            )
        )
        Spacer(modifier = Modifier.height(Dimensions.SpacerLarge))
    }
}

@Composable
private fun MacroRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
    }
} 