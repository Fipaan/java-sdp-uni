package lfs;

import lfs.print.*;
import lfs.errors.*;
import lfs.gui.*;

import javax.swing.*;

public class Main {
    private static App app = new App();
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            app.init();
        });
    }
}
