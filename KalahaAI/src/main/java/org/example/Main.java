package org.example;
import static com.raylib.Colors.*;
import static com.raylib.Raylib.*;

public class Main {
    public static void main(String[] args) {
        BoardLogic logic = new BoardLogic();
        BoardUI ui = new BoardUI(logic);
        ui.run();

    }
}