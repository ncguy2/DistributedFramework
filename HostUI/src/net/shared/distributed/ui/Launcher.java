package net.shared.distributed.ui;

import net.shared.distributed.CoreStart;
import net.shared.distributed.ui.form.CoreHost;

import javax.swing.*;

public class Launcher {

    public static void StartSwing(String... args) {
        JFrame frame = new JFrame("CoreHost");
        frame.setContentPane(new CoreHost().GetRootPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(final String...args) {
        SwingUtilities.invokeLater(() -> StartSwing(args));

        CoreStart.main(args);
    }

}
