package org.example;
import static com.raylib.Colors.*;
import static com.raylib.Raylib.*;

public class Main {
    public static void main(String[] args) {
        InitWindow(800, 450, "Demo");
        SetTargetFPS(60);
        Camera3D camera = new Camera3D()
                ._position(new Vector3().x(18).y(16).z(18))
                .target(new Vector3())
                .up(new Vector3().x(0).y(1).z(0))
                .fovy(45).projection(CAMERA_PERSPECTIVE);
        // Add this line only if Raylib version < 4.5:
        // SetCameraMode(camera, CAMERA_ORBITAL);

        while (!WindowShouldClose()) {
            UpdateCamera(camera, CAMERA_ORBITAL);
            BeginDrawing();
            ClearBackground(RAYWHITE);
            BeginMode3D(camera);
            DrawGrid(20, 1.0f);
            EndMode3D();
            DrawText("Hello world", 190, 200, 20, VIOLET);
            DrawFPS(20, 20);
            EndDrawing();
        }
        CloseWindow();

    }
}