package main.java.userInterface;


import main.java.speechrecognition.WitResponse;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BasicDesign extends JFrame implements ComponentListener {

    private ContentPanel contentPanel; // Displays photos
    private JTabbedPane tabbedPane; // Contains library, tools, etc.
    private JSplitPane splitPane; // For splitting library and content panel
    private Toolbar toolbar;
    private DebugPanel debugPanel; // For showing debug information (e.g., speech recogntion)

    Map<String, Color> colors = new HashMap<String, Color>();


    public BasicDesign(int hi, int wi, String path) throws Exception {

        super("Photoalbum");
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
            System.out.println("Could not find Look & Feel 'Nimbus', using standard theme instead.");
        }

        URL url = getClass().getResource(path);
        File dir = new File(url.toURI());
        File[] imageFiles = dir.listFiles();
        String[] images = new String[imageFiles.length];
        for (int i = 0; i < imageFiles.length; ++i){
            if (imageFiles[i].isDirectory()) {
                System.out.println(imageFiles[i].getName());
            } else {
                images[i] = imageFiles[i].getName();
                System.out.println(imageFiles[i].getName());
            }
        }

        this.toolbar = new Toolbar();
        PhotoBar photoBar = new PhotoBar(images);
        this.tabbedPane = new TabbedPane(photoBar);
        MyImage[] imageArray = photoBar.loadImages(path, 100);
        ArrayList<MyImage> imageArrayList = new ArrayList<MyImage>();

        for (MyImage image:imageArray){
            imageArrayList.add(image);
        }

        this.contentPanel = new ContentPanel(imageArrayList);
        this.debugPanel = new DebugPanel();
        this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabbedPane, contentPanel);

        // Create MenuBar (File, View, etc.).
        setJMenuBar(createMenuBar());

        // Set layout (we could change it to GridBagLayout)
        setLayout(new BorderLayout());

        debugPanel.setPreferredSize(new Dimension(800, 200));

        addColors();

        add(toolbar, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(debugPanel, BorderLayout.SOUTH);

        photoBar.setPhotoBarListener(
                new PhotoBarListener() {
                    @Override
                    public void recognizedClick(MyImage image) {
                        image.setActive(!image.isActive());
                        for (MyImage i : contentPanel.getImageList()) {
                            if (i.isActive()) {
                                contentPanel.setImage(i);
                            }
                        }
                        contentPanel.repaint();
                    }
                }
        );

        toolbar.setToolBarListener(new ToolBarListener() {
            @Override
            public void recognizedText(String text) {
                debugPanel.appendText(text);
            }

            @Override
            public void recognizedWitResponse(WitResponse response) {
                if (response.getIntent().contains("select")) {
                    String entity = response.getEntities();
                    for (MyImage i : contentPanel.getImageList()) {
//                        if (entities.contains(i.getNum())) {
//                            i.setActive(!i.isActive());
//                        }

                    }
                }
                if (response.getIntent().contains("background")) {
                    String entity = response.getEntities();
                    if (entity != null) {
                        String theColor = entity;
                        contentPanel.setBackground(colors.get(theColor.toLowerCase()));
                    }

                    }
                repaint();
                }
            });

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }


    public void componentHidden(ComponentEvent e) {
        System.out.println(e.getComponent().getClass().getName() + " --- Hidden");
    }

    public void componentMoved(ComponentEvent e) {
        System.out.println(e.getComponent().getClass().getName() + " --- Moved");
    }

    public void componentResized(ComponentEvent e) {
        System.out.println(e.getComponent().getClass().getName() + " --- Resized");
//    	//Resizing thumbnails with window
//    	int width = e.getComponent().getWidth();
//    	for (int i = 0; i < photoBar.getComponentCount(); i++) {
//    		JButton button = (JButton) photoBar.getComponent(i);
//  		    BufferedImage img;
//			img = this.pictures[i].getbImage();
//			int w = 2*width/3;
//			BufferedImage resizedImg = new BufferedImage(w, w, BufferedImage.TYPE_INT_RGB);
//			Graphics2D g2 = resizedImg.createGraphics();
//			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//			g2.drawImage(img, 0, 0, w, w, null);
//			g2.dispose();
//			ImageIcon newIcon = new ImageIcon(resizedImg);
//			button.setPreferredSize(new Dimension(width,width));
//			button.setIcon(newIcon);
//			button.revalidate();
//			button.repaint();
//    	}
    }

    public void componentShown(ComponentEvent e) {
        System.out.println(e.getComponent().getClass().getName() + " --- Shown");

    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();


        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open Picture Folder...");
        JMenuItem exitItem = new JMenuItem("Exit");

        fileMenu.add(openItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu showMenu = new JMenu("View");
        JMenuItem showFormItem = new JMenuItem("Debug Window");
        showMenu.add(showFormItem);

        JMenu windowMenu = new JMenu("Window");

        windowMenu.add(showMenu);

        menuBar.add(fileMenu);
        menuBar.add(windowMenu);

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        return menuBar;
    }

    private void addColors() {
        colors.put("blue", Color.BLUE);
        colors.put("red", Color.RED);
        colors.put("green", Color.GREEN);
    }
}


	

