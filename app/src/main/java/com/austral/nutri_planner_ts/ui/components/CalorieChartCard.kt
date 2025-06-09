package com.austral.nutri_planner_ts.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.austral.nutri_planner_ts.ui.screens.profile.DailyEntry
import com.austral.nutri_planner_ts.ui.screens.profile.MacroRecommendation

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
            .height(200.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Calorie Intake (Last 7 Days)",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            if (dailyHistory.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No data available",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            } else {
                CalorieChart(
                    data = dailyHistory.take(7), // Last 7 days
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun CalorieChart(
    data: List<DailyEntry>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val maxCalories = data.maxOfOrNull { it.consumedCalories }?.toFloat() ?: 2000f
        val minCalories = data.minOfOrNull { it.consumedCalories }?.toFloat() ?: 0f
        val range = maxCalories - minCalories
        
        if (data.size > 1) {
            val points = data.mapIndexed { index, entry ->
                val x = (index.toFloat() / (data.size - 1)) * size.width
                val y = size.height - ((entry.consumedCalories - minCalories) / range) * size.height
                Offset(x, y)
            }
            
            // Draw lines connecting points
            for (i in 0 until points.size - 1) {
                drawLine(
                    color = Color.Blue,
                    start = points[i],
                    end = points[i + 1],
                    strokeWidth = 3.dp.toPx()
                )
            }
            
            // Draw points
            points.forEach { point ->
                drawCircle(
                    color = Color.Blue,
                    radius = 4.dp.toPx(),
                    center = point
                )
            }
        }
    }
} 