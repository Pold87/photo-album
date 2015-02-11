package main.java.userInterface;

import javax.swing.*;
import java.awt.*;

public class TabbedPane extends JTabbedPane {
    private static final long serialVersionUID = 1L;

    public TabbedPane(PhotoBar photoBar, BackgroundBar backgroundBar) throws Exception {

        super(JTabbedPane.LEFT);

        VTextIcon textIcon1 = new VTextIcon(this, "Photos");
        setBackground(Color.white);
        addTab(null, textIcon1, new JScrollPane(photoBar));

        JPanel panel_2 = new JPanel();
//        scrollPane_1.setViewportView(panel_2);
        panel_2.setLayout(new BorderLayout(0, 0));

        VTextIcon textIcon2 = new VTextIcon(this, "Backgrounds");
        JScrollPane scrollPane_2 = new JScrollPane(backgroundBar);
        addTab(null, textIcon2, scrollPane_2);

    }

}
