package edu.cuhk.csci3310.ui.habitDetail.customHeatmap

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class Level(val color: Color) {
    Negative(Color(0xFFEF4444)),
    Skipped(Color(0xFFFDE047)),
    Zero(Color(0xFFEBEDF0)),
    One(Color(0xFF9BE9A8)),
    Two(Color(0xFF40C463)),
    Three(Color(0xFF30A14E)),
    Four(Color(0xFF216E3A)),
}

@Composable
fun Tile(color: Color, onClick: (() -> Unit)? = null) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .padding(3.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(color = color)
            .clickable(enabled = onClick != null) { onClick?.invoke() },
    )
}

