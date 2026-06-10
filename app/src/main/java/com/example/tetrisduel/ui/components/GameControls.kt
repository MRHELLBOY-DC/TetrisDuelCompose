package com.example.tetrisduel.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameControls(
    onMoveLeft: () -> Unit,
    onMoveRight: () -> Unit,
    onRotateClockwise: () -> Unit,
    onRotateCounterClockwise: () -> Unit,
    onMoveDown: () -> Unit,
    onHardDrop: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ControlButton(label = "↺", onClick = onRotateCounterClockwise)
        ControlButton(label = "←", onClick = onMoveLeft)
        ControlButton(label = "↓", onClick = onMoveDown)
        ControlButton(label = "→", onClick = onMoveRight)
        ControlButton(label = "↻", onClick = onRotateClockwise)
        DropButton(onClick = onHardDrop)
    }
}

@Composable
private fun ControlButton(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(52.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF374151))
    ) {
        Text(text = label, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun DropButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
    ) {
        Text(text = "DROP", fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}