import presentation.Window;

import java.awt.*;

public class Entry {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Window mainWindow = new Window();
            mainWindow.setVisible(true);
        });
    }
}
