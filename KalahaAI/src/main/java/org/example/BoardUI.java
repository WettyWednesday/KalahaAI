package org.example;
import static com.raylib.Colors.*;
import static com.raylib.Raylib.*;

public class BoardUI {
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 400;

    private BoardLogic boardLogic;

    public BoardUI(BoardLogic logic) {
        this.boardLogic = logic;
    }

    public void run() {
        InitWindow(SCREEN_WIDTH, SCREEN_HEIGHT, "Kalaha Board");
        SetTargetFPS(60);

        while (!WindowShouldClose()) {
            if (boardLogic.isGameOver()) {
                displayGameOver();
            } else {
                handleInput();
            }

            BeginDrawing();
            ClearBackground(RAYWHITE);
            drawBoard();
            EndDrawing();
        }

        CloseWindow();
    }

    private void drawBoard() {
        Rectangle boardRec = new Rectangle().x(100).y(80).width(600).height(200);
        DrawRectangleRounded(boardRec, 0.2f, 6, BROWN);

        int[] boardState = boardLogic.getBoard();
        int numPits = 6;
        float pitRadius = 30;
        float spacing = (boardRec.width() - (numPits * pitRadius * 2)) / (numPits - 1);
        float startX = boardRec.x() + pitRadius;
        float topRowY = boardRec.y() + pitRadius;
        float bottomRowY = boardRec.y() + boardRec.height() - pitRadius;

        for (int i = 0; i < numPits; i++) {
            float x = startX + i * (pitRadius * 2 + spacing);
            DrawCircle((int) x, (int) topRowY, pitRadius, DARKGRAY);
            DrawText(String.valueOf(boardState[i]), (int) x - 5, (int) topRowY - 5, 20, WHITE);

            DrawCircle((int) x, (int) bottomRowY, pitRadius, DARKGRAY);
            DrawText(String.valueOf(boardState[i + numPits + 1]), (int) x - 5, (int) bottomRowY - 5, 20, WHITE);
        }

        float storeWidth = 40;
        float storeHeight = boardRec.height() - 20;

        DrawRectangleRounded(new Rectangle().x(boardRec.x() - storeWidth - 10).y(boardRec.y() + 10).width(storeWidth).height(storeHeight), 0.3f, 6, DARKBROWN);
        DrawText(String.valueOf(boardState[BoardLogic.PLAYER_1_STORE]), (int) (boardRec.x() - storeWidth), (int) (boardRec.y() + storeHeight / 2), 20, WHITE);

        DrawRectangleRounded(new Rectangle().x(boardRec.x() + boardRec.width() + 10).y(boardRec.y() + 10).width(storeWidth).height(storeHeight), 0.3f, 6, DARKBROWN);
        DrawText(String.valueOf(boardState[BoardLogic.PLAYER_2_STORE]), (int) (boardRec.x() + boardRec.width() + 10), (int) (boardRec.y() + storeHeight / 2), 20, WHITE);
    }

    private void handleInput() {
        if (IsMouseButtonPressed(MOUSE_BUTTON_LEFT)) {
            int mouseX = GetMouseX();
            int mouseY = GetMouseY();
            int selectedPit = detectPitClicked(mouseX, mouseY);
            if (selectedPit != -1) {
                boardLogic.makeMove(selectedPit);
            }
        }
    }

    private int detectPitClicked(int mouseX, int mouseY) {
        float startX = 130;
        float pitSpacing = 100;
        float topRowY = 110;
        float bottomRowY = 250;
        float pitRadius = 30;

        for (int i = 0; i < 6; i++) {
            float x = startX + i * pitSpacing;
            if ((mouseX > x - pitRadius && mouseX < x + pitRadius) && (mouseY > topRowY - pitRadius && mouseY < topRowY + pitRadius)) {
                return i;
            }
            if ((mouseX > x - pitRadius && mouseX < x + pitRadius) && (mouseY > bottomRowY - pitRadius && mouseY < bottomRowY + pitRadius)) {
                return i + 7;
            }
        }
        return -1;
    }

    private void displayGameOver() {
        BeginDrawing();
        ClearBackground(RAYWHITE);
        DrawText("Game Over!", SCREEN_WIDTH / 2 - 50, SCREEN_HEIGHT / 2 - 20, 30, RED);
        EndDrawing();
        WaitTime(2);
    }
}
