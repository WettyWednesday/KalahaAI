package org.example;

import java.util.Random;

public class MinimaxAgent {

    private int depth;

    public MinimaxAgent(int depth) {
        this.depth = depth;
    }



    public int getBestMove(BoardLogic board) {
        int bestMove = -1;
        int bestValue = Integer.MIN_VALUE;

        for (int i = 0; i < 6; i++) {
            if (board.isValidMove(i, true)) {
                BoardLogic tempBoard = board.copy();
                tempBoard.checkMove(i, true);
                int moveValue = minimax(tempBoard, depth - 1, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                if (moveValue > bestValue) {
                    bestValue = moveValue;
                    bestMove = i;
                }
            }
        }



        return bestMove;

    }

    private int minimax(BoardLogic board, int depth, boolean isMaximizing, int alpha, int beta) {
        if (depth == 0 || board.isGameOver()) {
            return evaluateBoard(board);
        }

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int i = 0; i < 6; i++) {
                if (board.isValidMove(i, true)) {
                    BoardLogic tempBoard = board.copy();
                    tempBoard.checkMove(i, true);
                    int eval = minimax(tempBoard, depth - 1, false, alpha, beta);
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, eval);
                    if (beta <= alpha) break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int i = 6 + 1; i < BoardLogic.PLAYER_STORE; i++) {
                if (board.isValidMove(i, false)) {
                    BoardLogic tempBoard = board.copy();
                    tempBoard.checkMove(i, false);
                    int eval = minimax(tempBoard, depth - 1, true, alpha, beta);
                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, eval);
                    if (beta <= alpha) break;
                }
            }
            return minEval;
        }
    }

private int evaluateBoard(BoardLogic board) {
    int[] boardState = board.getBoard();
    int aiScore = boardState[BoardLogic.AI_STORE];
    int playerScore = boardState[BoardLogic.PLAYER_STORE];

    int aiStones = 0;
    int playerStones = 0;
    int aiEmptyPits = 0;
    int playerEmptyPits = 0;

    for (int i = 0; i < BoardLogic.PITS_PER_SIDE; i++) {
        aiStones += boardState[i];
        if (boardState[i] == 0) {
            aiEmptyPits++;
        }
    }

    for (int i = BoardLogic.PITS_PER_SIDE + 1; i < BoardLogic.PLAYER_STORE; i++) {
        playerStones += boardState[i];
        if (boardState[i] == 0) {
            playerEmptyPits++;
        }
    }

    int score = (aiScore - playerScore) * 10; // Weight the store difference more heavily
    score += (aiStones - playerStones); // Add the difference in stones on the board
    score -= (aiEmptyPits - playerEmptyPits) * 2; // Penalize empty pits

    return score;
}
}
