package org.example;

public class BoardLogic {
    private static final int PITS_PER_SIDE = 6;
    private static final int TOTAL_PITS = PITS_PER_SIDE * 2 + 2;
    public static final int PLAYER_1_STORE = PITS_PER_SIDE;
    public static final int PLAYER_2_STORE = TOTAL_PITS - 1;

    private int[] board;
    private boolean playersTurn;

    public BoardLogic() {
        resetBoard();
    }

    public void resetBoard() {
        board = new int[TOTAL_PITS];
        for (int i = 0; i < TOTAL_PITS; i++) {
            if (i != PLAYER_1_STORE && i != PLAYER_2_STORE) {
                board[i] = 4;
            }
        }
        playersTurn = false;
    }

public boolean makeMove(int pitIndex) {
    if (!isValidMove(pitIndex)) return false;

    int stones = board[pitIndex];
    board[pitIndex] = 0;
    int currentIndex = pitIndex;

        while (stones > 0) {
            currentIndex = (currentIndex + 1) % TOTAL_PITS;
            if ((playersTurn && currentIndex == PLAYER_2_STORE) || (!playersTurn && currentIndex == PLAYER_1_STORE)) {
                continue;
            }
            board[currentIndex]++;
            stones--;
        }

    checkCapture(currentIndex);
    switchTurn(currentIndex);

    return true;
}

    private boolean isValidMove(int pitIndex) {
        if (playersTurn && (pitIndex >= PITS_PER_SIDE || board[pitIndex] == 0)) return false;
        if (!playersTurn && (pitIndex < PITS_PER_SIDE || pitIndex == PLAYER_1_STORE || board[pitIndex] == 0)) return false;
        return true;
    }

    private void checkCapture(int lastPit) {
        if (playersTurn && lastPit < PITS_PER_SIDE && board[lastPit] == 1) {
            int oppositePit = TOTAL_PITS - 2 - lastPit;
            board[PLAYER_1_STORE] += board[oppositePit] + 1;
            board[oppositePit] = 0;
            board[lastPit] = 0;
        } else if (!playersTurn && lastPit >= PITS_PER_SIDE + 1 && lastPit < PLAYER_2_STORE && board[lastPit] == 1) {
            int oppositePit = TOTAL_PITS - 2 - lastPit;
            board[PLAYER_2_STORE] += board[oppositePit] + 1;
            board[oppositePit] = 0;
            board[lastPit] = 0;
        }
    }

    private void switchTurn(int lastPit) {
        if ((playersTurn && lastPit == PLAYER_1_STORE) || (!playersTurn && lastPit == PLAYER_2_STORE)) {
            return;
        }
        playersTurn = !playersTurn;
    }

    public boolean isGameOver() {
        boolean player1Empty = true, player2Empty = true;
        for (int i = 0; i < PITS_PER_SIDE; i++) {
            if (board[i] > 0) player1Empty = false;
            if (board[i + PITS_PER_SIDE + 1] > 0) player2Empty = false;
        }
        return player1Empty || player2Empty;
    }

    public int[] getBoard() {
        return board;
    }

    public boolean isPlayersTurn() {
        return playersTurn;
    }
}
