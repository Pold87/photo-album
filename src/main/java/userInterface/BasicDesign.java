package main.java.userInterface;


import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.leapmotion.leap.Controller;
import main.java.gesturerecognition.VolkerLeapListener;

public class BasicDesign extends JFrame implements ComponentListener {

	private static final long serialVersionUID = 1L;
	private ContentPanel contentPanel; // Displays photos
    private JTabbedPane tabbedPane; // Contains library, tools, etc.
    private JSplitPane splitPane; // For splitting library and content panel
    private Toolbar toolbar;
    private DebugPanel debugPanel; // For showing debug information (e.g., speech recogntion)
    private MyImage[] library;
    public OurController ourController;

    // Leap stuff
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int scr_width = screenSize.width;
    private int scr_height = screenSize.height;

    // TODO: See if there is a conflict between Controller classes
    public Controller leapController = new Controller();
    public VolkerLeapListener leapListener = new VolkerLeapListener();

    Map<Integer, Color> colors = new HashMap<Integer, Color>();


    public BasicDesign(String path) throws Exception {

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

        super.setSize(new Dimension(scr_width, scr_height));
        this.setPreferredSize(new Dimension(scr_width, scr_height));
        super.setLocationRelativeTo(null);
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);

        URL url = getClass().getResource(path);
        File dir = new File(url.toURI());
        File[] imageFiles = dir.listFiles();
        String[] images = new String[imageFiles.length];
        for (int i = 0; i < imageFiles.length; ++i){
            if (imageFiles[i].isDirectory() || imageFiles[i].isHidden()) {
                System.out.println(imageFiles[i].getName());
            } else {
                images[i] = imageFiles[i].getName();
                System.out.println(imageFiles[i].getName());
            }
        }

        this.ourController = new OurController();

        this.toolbar = new Toolbar();
        int preferredThumbSize = 100;

        // Specify content for tabs on the left hand side

        PhotoBar photoBar = new PhotoBar(images, ourController);
        BackgroundBar backgroundBar = new BackgroundBar(ourController);


        this.tabbedPane = new TabbedPane(photoBar, backgroundBar);
        tabbedPane.setPreferredSize(new Dimension(2*preferredThumbSize, (int) getPreferredSize().getHeight()));
        library = photoBar.loadImages(path, preferredThumbSize);



        this.contentPanel = new ContentPanel(ourController);
        ourController.initialize(contentPanel, this);
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
        toolbar.setToolBarListener(ourController);


        /**************************/
        // Leap STUFF
        //leapListener.setSystemPanel(systemPanel);
        leapListener.setScrHeight(scr_height);
        leapListener.setScrWidth(scr_width);
        leapListener.setOurController(this.ourController);
        leapController.addListener(leapListener);
//        leapController.addListener(leapListener); // not necessary if our controller can do the stuff
        /**************************/


        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }


    public void componentHidden(ComponentEvent e) {
        System.out.println(e.getComponent().getClass().getName() + " --- Hidden");
    }

    public void componentMoved(ComponentEvent e) {
        System.out.println(e.getComponent().getClass().getName() + " --- Moved");
    }

    public MyImage [] getLibrary(){
    	return library;
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
            
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        return menuBar;
    }

    private void addColors() {
        colors.put(0, Color.BLUE);
        colors.put(1, Color.RED);
        colors.put(1, Color.GREEN);
    }
    
    public DebugPanel getDebugPanel(){
    	return debugPanel;
    }
    
    public Toolbar getToolbar(){
    	return toolbar;
    }
    
}