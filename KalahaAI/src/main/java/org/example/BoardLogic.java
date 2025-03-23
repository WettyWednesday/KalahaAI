package org.example;

public class BoardLogic {
    protected static final int PITS_PER_SIDE = 6;
    private static final int TOTAL_PITS = PITS_PER_SIDE * 2 + 2;
    public static final int AI_STORE = PITS_PER_SIDE;
    public static final int PLAYER_STORE = TOTAL_PITS - 1;

    private int[] board;
    private boolean AITurn;
    protected final MinimaxAgent agent = new MinimaxAgent( 12);
    protected int bestMove = -1;


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
        if (!isValidMove(pitIndex, AITurn)) return false;

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
        bestMove = agent.getBestMove(this);
        new Thread(() ->{
            try {
                Thread.sleep(1000);
                makeMove(bestMove);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
     }

        return true;
    }

    public boolean checkMove(int pitIndex, boolean AITurn){
        if (!isValidMove(pitIndex, AITurn)) return false;

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
        return true;
    }



    public boolean isValidMove(int pitIndex, boolean AITurn) {
        if(pitIndex < 0 || pitIndex >= TOTAL_PITS) return false;
        if (AITurn && (pitIndex >= PITS_PER_SIDE || board[pitIndex] == 0)) return false;
        return AITurn || (pitIndex >= PITS_PER_SIDE && pitIndex != AI_STORE && board[pitIndex] != 0);
    }

    private void checkCapture(int lastPit) {
        if (AITurn && lastPit < AI_STORE && board[lastPit] == 1) {
            int oppositePit = TOTAL_PITS - 2 - lastPit;
            if(board[oppositePit] == 0) return;
            board[AI_STORE] += board[oppositePit] + 1;
            board[oppositePit] = 0;
            board[lastPit] = 0;
        } else if (!AITurn && lastPit > AI_STORE && lastPit < PLAYER_STORE && board[lastPit] == 1) {
            int oppositePit = TOTAL_PITS - 2 - lastPit;
            if(board[oppositePit] == 0) return;
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
        boolean aiEmpty = true, playerEmpty = true, player1StoreOverHalf = true, player2StoreOverHalf = true;
        for (int i = 0; i < PITS_PER_SIDE; i++) {
            if (board[i] > 0) aiEmpty = false;
            if (board[i + PITS_PER_SIDE + 1] > 0) playerEmpty = false;
            if(board[AI_STORE] <= 24) player1StoreOverHalf = false;
            if(board[PLAYER_STORE] <= 24) player2StoreOverHalf = false;
        }
        if(aiEmpty){
            for(int i = 7; i<TOTAL_PITS-1;i++){
                board[PLAYER_STORE] += board[i];
                board[i] = 0;
            }
        }else if(playerEmpty){
            for(int i = 0; i<PITS_PER_SIDE;i++){
                board[AI_STORE] += board[i];
                board[i] = 0;
            }
        }
        return aiEmpty || playerEmpty || player1StoreOverHalf || player2StoreOverHalf;
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

    public String getWinner(){
        if(board[AI_STORE]>board[PLAYER_STORE]){
            return "AI";
        }else if(board[AI_STORE]<board[PLAYER_STORE]){
            return "Player";
        }else{
            return "Tie";
        }
    }
}
