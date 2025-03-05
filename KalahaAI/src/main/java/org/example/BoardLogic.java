package org.example;

public class BoardLogic {
    private static final int PITS_PER_SIDE = 6;
    private static final int TOTAL_PITS = PITS_PER_SIDE * 2 + 2;
    public static final int PLAYER_1_STORE = PITS_PER_SIDE;
    public static final int PLAYER_2_STORE = TOTAL_PITS - 1;

    private int[] board;
    private boolean AITurn;

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
        AITurn = false;
    }

public boolean makeMove(int pitIndex) {
    if (!isValidMove(pitIndex)) return false;

    int stones = board[pitIndex];
    board[pitIndex] = 0;
    int currentIndex = pitIndex;

        while (stones > 0) {
            currentIndex = (currentIndex + 1) % TOTAL_PITS;
            if ((AITurn && currentIndex == PLAYER_2_STORE) || (!AITurn && currentIndex == PLAYER_1_STORE)) {
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
        if (AITurn && (pitIndex >= PITS_PER_SIDE || board[pitIndex] == 0)) return false;
        if (!AITurn && (pitIndex < PITS_PER_SIDE || pitIndex == PLAYER_1_STORE || board[pitIndex] == 0)) return false;
        return true;
    }

    private void checkCapture(int lastPit) {
        if (AITurn && lastPit < PLAYER_1_STORE && board[lastPit] == 1) {
            int oppositePit = TOTAL_PITS - 2 - lastPit;
            board[PLAYER_1_STORE] += board[oppositePit] + 1;
            board[oppositePit] = 0;
            board[lastPit] = 0;
        } else if (!AITurn && lastPit > PLAYER_1_STORE && lastPit < PLAYER_2_STORE && board[lastPit] == 1) {
            int oppositePit = TOTAL_PITS - 2 - lastPit;
            board[PLAYER_2_STORE] += board[oppositePit] + 1;
            board[oppositePit] = 0;
            board[lastPit] = 0;
        }
    }

    private void switchTurn(int lastPit) {
        if ((AITurn && lastPit == PLAYER_1_STORE) || (!AITurn && lastPit == PLAYER_2_STORE)) {
            return;
        }
        AITurn = !AITurn;
    }

    public boolean isGameOver() {
        boolean player1Empty = true, player2Empty = true, player1StoreOverHalf = true, player2StoreOverHalf = true;
        for (int i = 0; i < PITS_PER_SIDE; i++) {
            if (board[i] > 0) player1Empty = false;
            if (board[i + PITS_PER_SIDE + 1] > 0) player2Empty = false;
            if(board[PLAYER_1_STORE] <= 24) player1StoreOverHalf = false;
            if(board[PLAYER_2_STORE] <= 24) player2StoreOverHalf = false;
        }
        return player1Empty || player2Empty || player1StoreOverHalf || player2StoreOverHalf;
    }

    public int[] getBoard() {
        return board;
    }

    public boolean isAITurn() {
        return AITurn;
    }
}
