package edu.cuhk.csci3310.ui.habitDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.cuhk.csci3310.data.Habit

@Composable
fun DataTile(figure: String, name: String) {
    Column(verticalArrangement = Arrangement.Center) {
        Text(text = figure, style = MaterialTheme.typography.displaySmall)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = name, fontWeight = FontWeight.Light, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun HabitDescription(habit: Habit) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            habit.title,
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            habit.description ?: "No Description Provided",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
            DataTile(figure = "${habit.until}", name = "Effective Until")
            DataTile(figure = "${habit.nextTime}", name = "Next Time")
        }
    }
}