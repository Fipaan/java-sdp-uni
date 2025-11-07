package fipaan.game;

import java.io.IOException;

public class Main {
    private static App app = null;
    public static void main(String[] args) throws IOException {
        app = new App();
        app.run();
    }
}
