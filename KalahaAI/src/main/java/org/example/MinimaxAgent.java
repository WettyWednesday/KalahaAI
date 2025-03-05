package org.example;

public class MinimaxAgent {

    private BoardLogic board;
    private int depth;

    public MinimaxAgent(BoardLogic board, int depth){
        this.board = board;
        this.depth = depth;
    }

    public  int minimax(){
        BoardLogic tempBoard = board.copy();
        int[] scoremoves = new int[6];
        for (int i=0; i<6; i++){
            scoremoves[i]= evaluate(tempBoard, i);
            tempBoard=board.copy();
        }
        int bestMove = -1;
        for (int i=0; i<6; i++){
            if (scoremoves[i] > bestMove){
                bestMove = i;
            }
        }
        return bestMove;
    }

    private int evaluate(BoardLogic board, int move){
        int AIScore = board.getBoard()[BoardLogic.AI_STORE];
        board.makeMove(move);
        if(board.getBoard()[BoardLogic.AI_STORE] > AIScore){
            return 1;
        } else{
            return 0;
        }
    }
}
