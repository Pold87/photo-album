package main.java.userInterface;


import main.java.speechrecognition.WitResponse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class BasicDesign extends JFrame implements ComponentListener {

    private ContentPanel contentPanel; // Displays photos
    private JTabbedPane tabbedPane; // Contains library, tools, etc.
    private JToolBar photoBar; // Actually contains photos.
    private JLabel library; // contains library of photos TODO: NO!
    private JSplitPane splitPane; // For splitting library and content panel
    private String path; // Path of images
    private String[] images; // Names of images
    private Toolbar toolbar;
    private DebugPanel debugPanel; // For showing debug information (e.g., speech recogntion)
//	private Photo[] pictures;

    public BasicDesign(int hi, int wi, String path, String[] images) throws IOException {

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

        this.contentPanel = new ContentPanel();
        this.photoBar = new PhotoBar();
        this.library = new PhotoLibrary();
        this.toolbar = new Toolbar();
        this.debugPanel = new DebugPanel();

        this.tabbedPane = new TabbedPane(photoBar);
        this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabbedPane, contentPanel);

        this.path = path;
        this.images = images;

        // Create MenuBar (File, View, etc.).
        setJMenuBar(createMenuBar());

        // Set layout (we could change it to GridBagLayout)
        setLayout(new BorderLayout());

        debugPanel.setPreferredSize(new Dimension(800, 200));

        add(toolbar, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(debugPanel, BorderLayout.SOUTH);

        toolbar.setToolBarListener(new ToolBarListener() {
            @Override
            public void recognizedText(String text) {
                debugPanel.appendText(text);
            }

            @Override
            public void recognizedWitResponse(WitResponse response) {
                if (response.getIntent().equals("select")) {

                    ArrayList<Integer> entities = response.getEntities();

                    for (MyImage i : contentPanel.getImageList()) {
                        if (entities.contains(i.getNum())) {
                            i.setActive(!i.isActive());
                        }
                    }
                }
            repaint();
            }
        });


//		gbl_panel.columnWidths = new int[]{width_library, width_book};
//		gbl_panel.rowHeights = new int[]{hi};
//		gbl_panel.columnWeights = new double[]{0.17, 0.83};
//		gbl_panel.rowWeights = new double[]{1.0};

//		gbc.gridwidth = 2;

        // Add tabbedPane
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.fill = GridBagConstraints.BOTH;
//        gbc.weightx = 0.3;
//        gbc.weighty = 1;
//
//        add(splitPane, gbc);

//		panel.setLayout(gbl_panel);
//		contentPanel.add(panel, gbc);

        // Add contentPanel
//		gbc.gridx = 1;
//		gbc.gridy = 0;
//		gbc.weightx = 0.7;
//		gbc.weighty = 1;
//		add(contentPanel);

        //Pane that holds canvas for book
//		JPanel book_panel = new JPanel();
//		GridBagLayout book_layout = new GridBagLayout();
//		book_panel.setLayout(book_layout);
//		book_layout.columnWidths = new int[]{width_book / 10, 2 * width_book / 5, 2 * width_book / 5, width_book / 10};
//		book_layout.rowHeights = new int[]{hi / 6, 2 * hi / 3, hi / 6};
//		book_layout.columnWeights = new double[]{0.1, 0.4, 0.4, 0.1};
//		book_layout.rowWeights = new double[]{0.17, 0.66, 0.17};
//		GridBagConstraints book_constraints = new GridBagConstraints();
//		book_constraints.fill = GridBagConstraints.BOTH;
//		book_constraints.gridx = 1;
//		book_constraints.gridy = 0;
//		book_panel.setBackground(Color.GRAY);
//		panel.add(book_panel, book_constraints);

        //Left page of photobook
//		JPanel photo_left = new JPanel();
//		GridBagConstraints gbc_photo_left = new GridBagConstraints();
//		gbc_photo_left.fill = GridBagConstraints.BOTH;
//		gbc_photo_left.gridx = 1;
//		gbc_photo_left.gridy = 1;
//		photo_left.setBackground(Color.WHITE);
//		book_panel.add(photo_left, gbc_photo_left);

        //Right page of photobook
//		JPanel photo_right = new JPanel();
//		GridBagConstraints gbc_photo_right = new GridBagConstraints();
//		gbc_photo_right.fill = GridBagConstraints.BOTH;
//		gbc_photo_right.gridx = 2;
//		gbc_photo_right.gridy = 1;
//		photo_right.setBackground(Color.LIGHT_GRAY);
//		book_panel.add(photo_right, gbc_photo_right);

//
//		photo_right.add(library);
//		panel_2.add(photoBar);
//
//		// start the image loading
//		double thumbSize = wi * 0.2;
//
//		//Array containing the images as objects of class Photo
//		pictures = loadimages((int) thumbSize, (int) thumbSize, thumbSize * 2);
//
//		pack();
//		setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//		setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setVisible(true);

    }

    // loads images and returns array of photos, so that photo fields can be accessed easily
    private Photo[] loadimages(int buttonWidth, int buttonHeight, double sideLength) {
        Photo[] photos = new Photo[images.length];
        for (int i = 0; i < images.length; i++) {
            String pic = images[i];
            try {
                Photo photo = new Photo(this.path, pic, buttonWidth, buttonHeight, (float) sideLength);
                photos[i] = photo;
                ThumbnailAction thumbAction = new ThumbnailAction(photo.getDispPic(), photo.getThumbnailIcon(), pic);
                JButton thumbButton = new JButton(thumbAction);
                photoBar.add(thumbButton, photoBar.getComponentCount());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return photos;
    }

    /**
     * Action class that shows the image specified in it's constructor.
     */
    private class ThumbnailAction extends AbstractAction {
        private static final long serialVersionUID = 1L;
        private ImageIcon displayPhoto;

        public ThumbnailAction(ImageIcon photo, ImageIcon thumb, String desc) {

            displayPhoto = photo;

            // The short description becomes the tooltip of a button.
            putValue(SHORT_DESCRIPTION, desc);

            // The LARGE_ICON_KEY is the key for setting the
            // icon when an Action is applied to a button.
            putValue(LARGE_ICON_KEY, thumb);
        }

        /**
         * Shows the full image in the main area and sets the application title.
         */
        public void actionPerformed(ActionEvent e) {
            library.setIcon(displayPhoto);
            //setTitle("Icon Demo: " + getValue(SHORT_DESCRIPTION).toString());
        }
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
}


	
