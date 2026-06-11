package com.example.tetrisduel.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tetrisduel.ui.components.ConnectionStatusView
import com.example.tetrisduel.ui.components.GameControls
import com.example.tetrisduel.ui.components.NextPieceCanvas
import com.example.tetrisduel.ui.components.ScorePanel
import com.example.tetrisduel.ui.components.TetrisBoardCanvas
import com.example.tetrisduel.ui.states.ResultUiState
import com.example.tetrisduel.ui.viewmodels.GameViewModel

@Composable
fun GameScreen(
    viewModel: GameViewModel = hiltViewModel(),
    onNavigateToResult: (ResultUiState) -> Unit
) {
    val gameState by viewModel.gameState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigateToResult.collect { result ->
            onNavigateToResult(result)
        }
    }

    val level = (gameState.localPlayer.linesCleared / 10) + 1

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0F172A))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Main game area
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Board (2/3 of width)
                TetrisBoardCanvas(
                    board = gameState.localPlayer.board,
                    currentPiece = gameState.currentPiece,
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxHeight()
                )

                // Right panel (1/3 of width)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "SIGUIENTE",
                        color = Color(0xFF9CA3AF),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                    NextPieceCanvas(
                        piece = gameState.nextPiece,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    ScorePanel(
                        score = gameState.localPlayer.score,
                        linesCleared = gameState.localPlayer.linesCleared,
                        level = level,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (uiState.isOnline) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "VS",
                            color = Color(0xFF9CA3AF),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = uiState.opponentName,
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        ConnectionStatusView(
                            isConnected = uiState.isOpponentConnected,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Controls
            GameControls(
                onMoveLeft = viewModel::moveLeft,
                onMoveRight = viewModel::moveRight,
                onRotateClockwise = viewModel::rotateClockwise,
                onRotateCounterClockwise = viewModel::rotateCounterClockwise,
                onMoveDown = viewModel::moveDown,
                onHardDrop = viewModel::hardDrop,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Easter egg: "37" banner
        AnimatedVisibility(
            visible = uiState.show37Banner,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Surface(
                color = Color(0xFFFFD700),
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 12.dp
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "★ 37 ★",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1F2937)
                    )
                    Text(
                        text = "¡37 LÍNEAS!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                }
            }
        }
    }
}