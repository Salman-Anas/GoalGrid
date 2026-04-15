package com.example.goalgrid.widget
import androidx.compose.runtime.Composable
import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.*
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.goalgrid.data.GoalGridDao
import com.example.goalgrid.data.Habit
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
class GoalGridWidget : GlanceAppWidget() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WidgetEntryPoint {
        fun dao(): GoalGridDao
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            WidgetEntryPoint::class.java
        )
        val dao = entryPoint.dao()
        val topHabits = dao.getTopHabits().first()

        provideContent {
            WidgetContent(topHabits)
        }
    }

    @Composable
    private fun WidgetContent(habits: List<Habit>) {
        val darkBg = Color(0xFF0F0F12)
        val cyanNeon = Color(0xFF00F5FF)
        
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(darkBg)
                .padding(12.dp)
        ) {
            Text(
                text = "Top Habits",
                style = TextStyle(
                    color = ColorProvider(Color.White),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = GlanceModifier.height(8.dp))
            
            habits.forEach { habit ->
                HabitWidgetItem(habit, cyanNeon)
                Spacer(modifier = GlanceModifier.height(4.dp))
            }
        }
    }

    @Composable
    private fun HabitWidgetItem(habit: Habit, accentColor: Color) {
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = GlanceModifier.defaultWeight()) {
                Text(
                    text = habit.name,
                    style = TextStyle(color = ColorProvider(Color.White), fontSize = 14.sp)
                )
                Text(
                    text = "${habit.currentCountToday}/${habit.dailyTarget}",
                    style = TextStyle(color = ColorProvider(Color.LightGray), fontSize = 12.sp)
                )
            }
            
            Text(
                text = "P: ${habit.priority}",
                style = TextStyle(color = ColorProvider(accentColor), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}

class GoalGridWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = GoalGridWidget()
}
