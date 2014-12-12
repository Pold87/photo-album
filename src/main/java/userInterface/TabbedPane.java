package main.java.userInterface;

import javax.swing.*;
import java.awt.*;

/**
 * Created by pold on 12/10/14.
 */
public class TabbedPane extends JTabbedPane{

    public TabbedPane() throws Exception{

        super(JTabbedPane.LEFT);

        VTextIcon textIcon1 = new VTextIcon(this, "Photos");
        PhotoBar photoBar = new PhotoBar();
        setBackground(Color.white);
        addTab(null, textIcon1, new JScrollPane(photoBar));

        JPanel panel_2 = new JPanel();
//        scrollPane_1.setViewportView(panel_2);
        panel_2.setLayout(new BorderLayout(0, 0));

        VTextIcon textIcon2 = new VTextIcon(this, "Backgrounds");
        JScrollPane scrollPane_2 = new JScrollPane();
        addTab(null, textIcon2, scrollPane_2);

        VTextIcon textIcon3 = new VTextIcon(this, "Frames");
        JScrollPane scrollPane_3 = new JScrollPane();
        addTab(null, textIcon3, scrollPane_3);
    }

    //Tabbed Pane that holds library
//    GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
//    gbc_tabbedPane.fill = GridBagConstraints.BOTH;
//    gbc_tabbedPane.insets = new Insets(0, 0, 0, 5);
//    gbc_tabbedPane.gridx = 0;
//    gbc_tabbedPane.gridy = 0;
//    panel.add(tabbedPane, gbc_tabbedPane);
//    tabbedPane.addComponentListener(this);


    public void addPanel() {

    }

}
