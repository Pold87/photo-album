package main.java.userInterface;


import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;


import com.leapmotion.leap.Controller;
import main.java.gesturerecognition.VolkerLeapListener;

public class BasicDesign extends JFrame {

	private static final long serialVersionUID = 1L;
	private ContentPanel contentPanel; // Displays photos
    private JTabbedPane tabbedPane; // Contains library, tools, etc.
    private JSplitPane splitPane; // For splitting library and content panel
    private Toolbar toolbar;
    private PhotoBar photoBar;
    private DebugPanel debugPanel; // For showing debug information (e.g., speech recogntion)
    private MyImage[] library;
    public OurController ourController;

    // Leap stuff

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int scr_width = screenSize.width;
    private int scr_height = screenSize.height;


    // TODO: See if there is a conflict between Controller classes
    public Controller leapController;
    public VolkerLeapListener leapListener;


    public BasicDesign(String path) throws Exception {

        super("Photoalbum");
        repaint();
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
        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        URL url = getClass().getResource(path);
        File dir = new File(url.toURI());
        File[] imageFiles = dir.listFiles();

        String[] images = new String[0];
        if (imageFiles != null) {
            images = new String[imageFiles.length];
        }

        if (imageFiles != null) {
            for (int i = 0; i < images.length; ++i) {
                if (imageFiles[i].isDirectory() || imageFiles[i].isHidden()) {
                    System.out.println(imageFiles[i].getName());
                } else {
                    images[i] = imageFiles[i].getName();
                    System.out.println(imageFiles[i].getName());
                }
            }
        }

        this.ourController = new OurController();

        this.toolbar = new Toolbar();
        toolbar.setController(this.ourController);
        int preferredThumbSize = 100;

        // Specify content for tabs on the left hand side

        this.photoBar = new PhotoBar(images, ourController);
        BackgroundBar backgroundBar = new BackgroundBar(ourController);


        this.tabbedPane = new TabbedPane(photoBar, backgroundBar);
        tabbedPane.setPreferredSize(new Dimension(2*preferredThumbSize, (int) getPreferredSize().getHeight()));
        library = photoBar.loadImages(path, preferredThumbSize);



        this.contentPanel = new ContentPanel(ourController, library);
        ourController.initialize(contentPanel, this);
        this.debugPanel = new DebugPanel();
        this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabbedPane, contentPanel);



        // Create MenuBar (File, View, etc.).
        setJMenuBar(createMenuBar());

        // Set layout (we could change it to GridBagLayout)
        setLayout(new BorderLayout());

        debugPanel.setPreferredSize(new Dimension(800, 200));

        add(toolbar, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(debugPanel, BorderLayout.SOUTH);
        toolbar.setToolBarListener(ourController);


        /**************************/
        // Leap STUFF
        if(App.testMode != App.TestMode.TestMouse){
        	leapController = new Controller();
        	leapListener = new VolkerLeapListener();
        	leapListener.setContentPanel(contentPanel);
        	leapListener.setScrHeight(scr_height);
        	leapListener.setScrWidth(scr_width);
        	leapListener.setOurController(this.ourController);
        	leapController.addListener(leapListener);
        }
        /**************************/

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public MyImage [] getLibrary(){
    	return library;
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

        exitItem.addActionListener(actionEvent -> System.exit(0));
        return menuBar;
    }

    public DebugPanel getDebugPanel(){
    	return debugPanel;
    }

    public Toolbar getToolbar(){
    	return toolbar;
    }


}