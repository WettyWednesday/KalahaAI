package main;
import static com.raylib.Colors.*;
import static com.raylib.Raylib.*;

import static com.raylib.Raylib.InitWindow;


public class Main {
    public static void main(String[] args) {
        InitWindow(800,600,"Herro");
        SetTargetFPS(60);

        while (!WindowShouldClose()) {
            BeginDrawing();
            ClearBackground(RAYWHITE);
            DrawText("Hello", 350, 280, 20, DARKGRAY);
            EndDrawing();
        }
        CloseWindow();
        System.out.println("Hello world!");
    }


}