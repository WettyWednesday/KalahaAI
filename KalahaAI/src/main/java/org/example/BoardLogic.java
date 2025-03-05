package org.example;

public class BoardLogic {
    private static final int PITS_PER_SIDE = 6;
    private static final int TOTAL_PITS = PITS_PER_SIDE * 2 + 2;
    public static final int AI_STORE = PITS_PER_SIDE;
    public static final int PLAYER_STORE = TOTAL_PITS - 1;

    private int[] board;
    private boolean AITurn;
    private MinimaxAgent agent = new MinimaxAgent(this, 5);


    public BoardLogic() {
        resetBoard();
    }

    public void resetBoard() {
        board = new int[TOTAL_PITS];
        for (int i = 0; i < TOTAL_PITS; i++) {
            if (i != AI_STORE && i != PLAYER_STORE) {
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
            if ((AITurn && currentIndex == PLAYER_STORE) || (!AITurn && currentIndex == AI_STORE)) {
                continue;
            }
            board[currentIndex]++;
            stones--;
        }

    checkCapture(currentIndex);
    switchTurn(currentIndex);
    if(AITurn){
    try {
        Thread.sleep(2000);
        makeMove(agent.minimax());
    } catch (InterruptedException e) {
        e.printStackTrace();
    }}

    return true;
}

public void undoMove(int stones) {

}

    private boolean isValidMove(int pitIndex) {
        if (AITurn && (pitIndex >= PITS_PER_SIDE || board[pitIndex] == 0)) return false;
        if (!AITurn && (pitIndex < PITS_PER_SIDE || pitIndex == AI_STORE || board[pitIndex] == 0)) return false;
        return true;
    }

    private void checkCapture(int lastPit) {
        if (AITurn && lastPit < AI_STORE && board[lastPit] == 1) {
            int oppositePit = TOTAL_PITS - 2 - lastPit;
            board[AI_STORE] += board[oppositePit] + 1;
            board[oppositePit] = 0;
            board[lastPit] = 0;
        } else if (!AITurn && lastPit > AI_STORE && lastPit < PLAYER_STORE && board[lastPit] == 1) {
            int oppositePit = TOTAL_PITS - 2 - lastPit;
            board[PLAYER_STORE] += board[oppositePit] + 1;
            board[oppositePit] = 0;
            board[lastPit] = 0;
        }
    }

    private void switchTurn(int lastPit) {
        if ((AITurn && lastPit == AI_STORE) || (!AITurn && lastPit == PLAYER_STORE)) {
            return;
        }
        AITurn = !AITurn;
    }

    public boolean isGameOver() {
        boolean player1Empty = true, player2Empty = true, player1StoreOverHalf = true, player2StoreOverHalf = true;
        for (int i = 0; i < PITS_PER_SIDE; i++) {
            if (board[i] > 0) player1Empty = false;
            if (board[i + PITS_PER_SIDE + 1] > 0) player2Empty = false;
            if(board[AI_STORE] <= 24) player1StoreOverHalf = false;
            if(board[PLAYER_STORE] <= 24) player2StoreOverHalf = false;
        }
        return player1Empty || player2Empty || player1StoreOverHalf || player2StoreOverHalf;
    }

    public int[] getBoard() {
        return board;
    }

    public boolean isAITurn() {
        return AITurn;
    }

    public BoardLogic copy() {
        BoardLogic copy = new BoardLogic();
        copy.board = board.clone();
        copy.AITurn = AITurn;
        return copy;
    }
}
