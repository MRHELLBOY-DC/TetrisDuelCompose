package com.example.tetrisduel.domain.logic

import com.example.tetrisduel.domain.models.GameConfig
import com.example.tetrisduel.domain.models.GameResult
import com.example.tetrisduel.domain.models.GameState
import com.example.tetrisduel.domain.models.Player
import com.example.tetrisduel.domain.models.PlayerStatus
import com.example.tetrisduel.domain.models.Position

class TetrisEngine(
    private val config: GameConfig = GameConfig(),
    private val pieceRotationManager: PieceRotationManager = PieceRotationManager(),
    private val collisionDetector: CollisionDetector = CollisionDetector(pieceRotationManager),
    private val boardManager: BoardManager = BoardManager(pieceRotationManager),
    private val pieceMovementManager: PieceMovementManager = PieceMovementManager(collisionDetector),
    private val lineClearManager: LineClearManager = LineClearManager(),
    private val garbageLineManager: GarbageLineManager = GarbageLineManager(),
    private val pieceGenerator: PieceGenerator = PieceGenerator(),
    private val scoreCalculator: ScoreCalculator = ScoreCalculator()
) {

    fun createInitialState(
        localPlayerId: String,
        localPlayerName: String,
        opponentPlayer: Player? = null
    ): GameState {
        val board = boardManager.createEmptyBoard(
            rows = config.boardRows,
            columns = config.boardColumns
        )

        val localPlayer = Player(
            id = localPlayerId,
            name = localPlayerName,
            board = board,
            score = 0,
            linesCleared = 0,
            status = PlayerStatus.PLAYING
        )

        return GameState(
            localPlayer = localPlayer,
            opponentPlayer = opponentPlayer,
            currentPiece = pieceGenerator.generatePiece(getSpawnPosition()),
            nextPiece = pieceGenerator.generatePiece(getSpawnPosition()),
            heldPiece = null,
            isGameRunning = true,
            result = null
        )
    }

    fun tick(gameState: GameState): GameState {
        if (!gameState.isGameRunning) return gameState

        val currentPiece = gameState.currentPiece ?: return spawnNextPiece(gameState)

        return if (pieceMovementManager.canMoveDown(gameState.localPlayer.board, currentPiece)) {
            gameState.copy(
                currentPiece = pieceMovementManager.moveDown(
                    board = gameState.localPlayer.board,
                    piece = currentPiece
                )
            )
        } else {
            lockCurrentPiece(
                gameState = gameState,
                pieceToLock = currentPiece
            )
        }
    }

    fun moveLeft(gameState: GameState): GameState {
        if (!gameState.isGameRunning) return gameState

        val currentPiece = gameState.currentPiece ?: return gameState

        return gameState.copy(
            currentPiece = pieceMovementManager.moveLeft(
                board = gameState.localPlayer.board,
                piece = currentPiece
            )
        )
    }

    fun moveRight(gameState: GameState): GameState {
        if (!gameState.isGameRunning) return gameState

        val currentPiece = gameState.currentPiece ?: return gameState

        return gameState.copy(
            currentPiece = pieceMovementManager.moveRight(
                board = gameState.localPlayer.board,
                piece = currentPiece
            )
        )
    }

    fun moveDown(gameState: GameState): GameState {
        if (!gameState.isGameRunning) return gameState

        val currentPiece = gameState.currentPiece ?: return gameState

        return if (pieceMovementManager.canMoveDown(gameState.localPlayer.board, currentPiece)) {
            gameState.copy(
                currentPiece = pieceMovementManager.moveDown(
                    board = gameState.localPlayer.board,
                    piece = currentPiece
                )
            )
        } else {
            lockCurrentPiece(
                gameState = gameState,
                pieceToLock = currentPiece
            )
        }
    }

    fun hardDrop(gameState: GameState): GameState {
        if (!gameState.isGameRunning) return gameState

        val currentPiece = gameState.currentPiece ?: return gameState

        val droppedPiece = pieceMovementManager.hardDrop(
            board = gameState.localPlayer.board,
            piece = currentPiece
        )

        val cellsDropped = droppedPiece.pivot.row - currentPiece.pivot.row

        val playerWithDropScore = gameState.localPlayer.copy(
            score = gameState.localPlayer.score +
                    scoreCalculator.calculateHardDropScore(cellsDropped)
        )

        return lockCurrentPiece(
            gameState = gameState.copy(
                localPlayer = playerWithDropScore,
                currentPiece = droppedPiece
            ),
            pieceToLock = droppedPiece
        )
    }

    fun rotateClockwise(gameState: GameState): GameState {
        if (!gameState.isGameRunning) return gameState

        val currentPiece = gameState.currentPiece ?: return gameState
        val rotatedPiece = pieceRotationManager.rotateClockwise(currentPiece)

        return if (collisionDetector.canPlacePiece(gameState.localPlayer.board, rotatedPiece)) {
            gameState.copy(currentPiece = rotatedPiece)
        } else {
            gameState
        }
    }

    fun rotateCounterClockwise(gameState: GameState): GameState {
        if (!gameState.isGameRunning) return gameState

        val currentPiece = gameState.currentPiece ?: return gameState
        val rotatedPiece = pieceRotationManager.rotateCounterClockwise(currentPiece)

        return if (collisionDetector.canPlacePiece(gameState.localPlayer.board, rotatedPiece)) {
            gameState.copy(currentPiece = rotatedPiece)
        } else {
            gameState
        }
    }

    fun addGarbageToLocalPlayer(
        gameState: GameState,
        amount: Int
    ): GameState {
        if (!gameState.isGameRunning) return gameState

        val currentBoard = gameState.localPlayer.board

        if (garbageLineManager.wouldCauseGameOver(currentBoard, amount)) {
            return gameState.copy(
                localPlayer = gameState.localPlayer.copy(
                    status = PlayerStatus.GAME_OVER
                ),
                currentPiece = null,
                nextPiece = null,
                isGameRunning = false,
                result = GameResult.LOSE
            )
        }

        val boardWithGarbage = garbageLineManager.addGarbageLines(
            board = currentBoard,
            amount = amount
        )

        return gameState.copy(
            localPlayer = gameState.localPlayer.copy(
                board = boardWithGarbage
            )
        )
    }

    private fun lockCurrentPiece(
        gameState: GameState,
        pieceToLock: com.example.tetrisduel.domain.models.Piece
    ): GameState {
        val boardAfterLock = boardManager.lockPiece(
            board = gameState.localPlayer.board,
            piece = pieceToLock
        )

        val lineClearResult = lineClearManager.clearCompletedLines(boardAfterLock)

        val newTotalLinesCleared =
            gameState.localPlayer.linesCleared + lineClearResult.linesCleared

        val currentLevel = scoreCalculator.calculateLevel(newTotalLinesCleared)

        val scoreToAdd = scoreCalculator.calculateLineClearScore(
            linesCleared = lineClearResult.linesCleared,
            level = currentLevel
        )

        val updatedPlayer = gameState.localPlayer.copy(
            board = lineClearResult.board,
            score = gameState.localPlayer.score + scoreToAdd,
            linesCleared = newTotalLinesCleared
        )

        val newCurrentPiece = gameState.nextPiece
            ?: pieceGenerator.generatePiece(getSpawnPosition())

        val newNextPiece = pieceGenerator.generatePiece(getSpawnPosition())

        if (!collisionDetector.canPlacePiece(lineClearResult.board, newCurrentPiece)) {
            return gameState.copy(
                localPlayer = updatedPlayer.copy(
                    status = PlayerStatus.GAME_OVER
                ),
                currentPiece = null,
                nextPiece = null,
                isGameRunning = false,
                result = GameResult.LOSE
            )
        }

        return gameState.copy(
            localPlayer = updatedPlayer,
            currentPiece = newCurrentPiece,
            nextPiece = newNextPiece
        )
    }

    private fun spawnNextPiece(gameState: GameState): GameState {
        val newCurrentPiece = gameState.nextPiece
            ?: pieceGenerator.generatePiece(getSpawnPosition())

        val newNextPiece = pieceGenerator.generatePiece(getSpawnPosition())

        if (!collisionDetector.canPlacePiece(gameState.localPlayer.board, newCurrentPiece)) {
            return gameState.copy(
                localPlayer = gameState.localPlayer.copy(
                    status = PlayerStatus.GAME_OVER
                ),
                currentPiece = null,
                nextPiece = null,
                isGameRunning = false,
                result = GameResult.LOSE
            )
        }

        return gameState.copy(
            currentPiece = newCurrentPiece,
            nextPiece = newNextPiece
        )
    }

    private fun getSpawnPosition(): Position {
        return Position(
            row = 0,
            column = config.boardColumns / 2
        )
    }
}