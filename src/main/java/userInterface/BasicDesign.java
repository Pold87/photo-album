package main.java.userInterface;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import main.java.gesturerecognition.VolkerLeapListener;

import com.leapmotion.leap.Controller;

public class BasicDesign extends JFrame {

	private static final long serialVersionUID = 1L;
	private ContentPanel contentPanel; // Displays photos
    private JTabbedPane tabbedPane; // Contains library, tools, etc.
    private JSplitPane splitPane; // For splitting library and content panel
    private Toolbar toolbar;
    public PhotoBar photoBar;
    private DebugPanel debugPanel; // For showing debug information (e.g., speech recognition)
    private MyImage[] library;
    public OurController ourController;
    private String task;

    private long startTime;

    // Leap stuff

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    private int scr_width = screenSize.width;
    private int scr_height = screenSize.height;

    public Controller leapController;
    public VolkerLeapListener leapListener;


    private class MyDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {

            if (e.getID() == KeyEvent.KEY_RELEASED) {

                switch (e.getKeyChar()) {

                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                        ourController.getLogger().setTaskNumber(KeyEvent.getKeyText(e.getKeyChar()));
                        try {
                            ourController.loadContentPanel(task, KeyEvent.getKeyText(e.getKeyChar()));
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                        resetStartTime();
                        break;

                    case 's':
                        try {
                            contentPanel.saveContentPanel();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        break;
                    case 't':
                        contentPanel.setDrawRectangles(!contentPanel.isDrawRectangles());
                        break;
                    case 'o':
                        contentPanel.overwritePictures();
                        break;
                    case 'p':
                        contentPanel.setDrawPictures(!contentPanel.isDrawPictures());
                        break;
                    case 'b':
                        ourController.killWit();
                }

            }
            return false;
        }
    }


    public BasicDesign(String path, String task) throws Exception {

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

        this.task = task;

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
        this.debugPanel.setVisible(false);
        this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabbedPane, contentPanel);




        // Create MenuBar (File, View, etc.).
        setJMenuBar(createMenuBar());

        // Set layout (we could change it to GridBagLayout)
        setLayout(new BorderLayout());

        debugPanel.setPreferredSize(new Dimension(800, 200));
        debugPanel.setVisible(false);

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


        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new MyDispatcher());


        startTime = System.currentTimeMillis();

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public int getScr_height() {
        return scr_height;
    }

    public int getScr_width() {
        return scr_width;
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
        JMenuItem showFormItem = new JCheckBoxMenuItem("Debug Window");

        showFormItem.setSelected(false);
        showMenu.add(showFormItem);

        JMenu windowMenu = new JMenu("Window");

        windowMenu.add(showMenu);

        menuBar.add(fileMenu);
        menuBar.add(windowMenu);

        exitItem.addActionListener(actionEvent -> System.exit(0));

        showFormItem.addActionListener(e -> {
            JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) e.getSource();
            debugPanel.setVisible(menuItem.isSelected());
        });


        return menuBar;
    }

    public DebugPanel getDebugPanel(){
    	return debugPanel;
    }

    public Toolbar getToolbar(){
    	return toolbar;
    }

    public ArrayList<Integer> getPictureNums() {

        ArrayList<Integer> nums = new ArrayList<>();


        for (MyImage i : this.library) {
            nums.add(i.getNum());
        }

        return nums;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }


    public long getStartTime() {
        return startTime;
    }

    public void resetStartTime() {
        this.startTime = System.currentTimeMillis();;
    }

}