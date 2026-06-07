package com.example.tetrisduel.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tetrisduel.ui.viewmodels.GameViewModel

@Composable
fun GameScreen(
    viewModel: GameViewModel = viewModel()
){
    val gameState by viewModel.

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Score: ${gameState.}"
        )
    }
}


