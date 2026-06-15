package ir.javid.sattar.todolist.feature.todo_list

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DayHeader(
    dayName: String,
    date: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = dayName,
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 48.sp
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(Color(0xFFE57373), CircleShape)
                    .align(Alignment.CenterVertically)
            )
        }
        
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = date.split(" ")[0] + " " + date.split(" ")[1],
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            )
            Text(
                text = date.split(" ").last(),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.LightGray,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun WeekCalendar(
    selectedDayIndex: Int = 4, // 0-6
    modifier: Modifier = Modifier
) {
    val days = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")
    val dates = listOf(5, 6, 7, 8, 9, 10, 11)
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        days.forEachIndexed { index, day ->
            CalendarDayItem(
                day = day,
                date = dates[index],
                isSelected = index == selectedDayIndex
            )
        }
    }
}

@Composable
fun CalendarDayItem(
    day: String,
    date: Int,
    isSelected: Boolean
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) Color.White else Color.Transparent,
        shadowElevation = if (isSelected) 2.dp else 0.dp,
        modifier = Modifier.width(45.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = date.toString(),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color(0xFFE57373) else Color.LightGray
                )
            )
            Text(
                text = day,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = if (isSelected) Color(0xFFE57373) else Color.LightGray,
                    fontSize = 10.sp
                )
            )
        }
    }
}

@Composable
fun DashedDivider(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxWidth().height(1.dp)) {
        drawLine(
            color = Color.LightGray.copy(alpha = 0.5f),
            start = Offset(0f, 0.5f),
            end = Offset(size.width, 0.5f),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
    }
}

@Composable
fun TodoItemRow(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    time: String? = null,
    isCompleted: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        DashedDivider(modifier = Modifier.padding(vertical = 12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = if (isCompleted) Color.LightGray else Color.Black
                ),
                modifier = Modifier.weight(1f)
            )
            if (time != null) {
                Text(
                    text = time,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.LightGray
                    )
                )
            }
        }
    }
}

@Composable
fun ModernBottomBar(
    onAddClick: () -> Unit,
    onCalendarClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        color = Color.White.copy(alpha = 0.9f)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(onClick = onCalendarClick) {
                Icon(Icons.Default.DateRange, contentDescription = "Calendar", tint = Color.LightGray)
            }
            
            Surface(
                modifier = Modifier
                    .size(56.dp)
                    .clickable { onAddClick() },
                shape = CircleShape,
                color = Color(0xFFF5F5F5),
                shadowElevation = 2.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Add, contentDescription = "Add Todo", modifier = Modifier.size(32.dp))
                }
            }
            
            IconButton(onClick = onSettingsClick) {
                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.LightGray)
            }
        }
    }
}
