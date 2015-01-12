package main.java.userInterface;


import main.java.gesturerecognition.Imagedata;
import main.java.gesturerecognition.LeapListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class BasicDesign extends JFrame implements ComponentListener {

	private static final long serialVersionUID = 1L;
	private ContentPanel contentPanel; // Displays photos
    private JTabbedPane tabbedPane; // Contains library, tools, etc.
    private JSplitPane splitPane; // For splitting library and content panel
    private Toolbar toolbar;
    private DebugPanel debugPanel; // For showing debug information (e.g., speech recogntion)

    private Controller controller = new Controller();
    private MyImage[] library;
    private ArrayList<Action> performedActions = new ArrayList<Action>(), undoneActions = new ArrayList<Action>();

    // Members
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int scr_width = screenSize.width;
    private int scr_height = screenSize.height;
    // TODO: See if there is a conflict between Controller classes
    public com.leapmotion.leap.Controller leapController = new com.leapmotion.leap.Controller();
    public VolkerLeapListener leapListener = new VolkerLeapListener();

    /**********************************************************************************************/
    // Copied from gesturerecognition (DrawingPanel)

    // Which object is selected
    private Imagedata activeShape;

    // Resize mode
    private double cursorX;
    private double cursorY;

    // Leap cursor right
    private int leapRightX = 9999, leapRightY = 9999;
    private float leapRightScreenDist = 1.0f;
    private boolean leapRightClick = false;
    private int leapRightFingers = 0;

    // Leap cursor left
    private int leapLeftX = 9999, leapLeftY = 9999;
    private float leapLeftScreenDist = 1.0f;
    private boolean leapLeftClick = false;
    private int leapLeftFingers = 0;

    // Transform for rotation
    AffineTransformOp op;

    // Shape Mode
    public enum ShapeMode {
        IMAGE
    };

    private ShapeMode shapeModeIndex = ShapeMode.IMAGE;


    public ShapeMode getShapeModeIndex() {
        return shapeModeIndex;
    }

    public ToolMode getToolModeIndex() {
        return toolModeIndex;
    }

    @SuppressWarnings("unused")
    private ShapeMode randomShape() {
        int index = new Random().nextInt(ShapeMode.values().length);
        return ShapeMode.values()[index];
    }

    // Tool Mode
    public enum ToolMode {
        MOVE, ENLARGE, REDUCE, ROTATE, CUT
    };

    ToolMode toolModeIndex = ToolMode.MOVE;

    // Screen width & height
    private int screenWidth, screenHeight;

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }



    public void setLeapRightFingers(int leapFingers) {
        this.leapRightFingers = leapFingers;
        repaint();
    }

    public void setLeapRightClick(boolean leapClick) {
        this.leapRightClick = leapClick;
        repaint();
    }

    public void setLeapRightX(int leapX) {
        this.leapRightX = leapX;
        repaint();
    }

    public void setLeapRightY(int leapY) {
        this.leapRightY = leapY;
        repaint();
    }

    public void setLeapRightScreenDist(float leapScreenDist) {
        this.leapRightScreenDist = leapScreenDist;
        repaint();
    }

    public void setLeapLeftFingers(int leapFingers) {
        this.leapLeftFingers = leapFingers;
        repaint();
    }

    public void setLeapLeftClick(boolean leapClick) {
        this.leapLeftClick = leapClick;
        repaint();
    }

    public void setLeapLeftX(int leapX) {
        this.leapLeftX = leapX;
        repaint();
    }

    public void setLeapLeftY(int leapY) {
        this.leapLeftY = leapY;
        repaint();
    }

    public void setLeapLeftScreenDist(float leapScreenDist) {
        this.leapLeftScreenDist = leapScreenDist;
        repaint();
    }

    public void rotate(boolean clockwise) {
        switch (toolModeIndex) {
            case ROTATE:
                if(clockwise) {
                    activeShape.increaseRotation(0.30);
                    repaint();
                }
                else {
                    activeShape.increaseRotation(-0.30);
                    repaint();
                }
                break;
            default:
                break;
        }
    }

    /* MOUSE LISTENER */

    public void cursorPressed(int XPos, int YPos) {
        this.requestFocusInWindow();

        // Update mouse Coords
        cursorY = YPos;
        cursorX = XPos;

        switch (toolModeIndex) {
            case MOVE:
            case ENLARGE:
            case REDUCE:
            case CUT:
            case ROTATE:
                repaint();
                break;
            default:
                break;

        }
    }

    public void cursorReleased(int XPos, int YPos) {
        switch (toolModeIndex) {
            case MOVE:
            case ENLARGE:
            case REDUCE:
            case ROTATE:
            case CUT:
                break;
            default:
                System.out.println("Tool not found: " + toolModeIndex);
                break;
        }
    }

    public void cursorDragged(int XPos, int YPos) {
        double deltaY, deltaX;
        double normalizerX, normalizerY;
        if(!(activeShape==null)) {
            switch (toolModeIndex) {
                case ENLARGE:
                    System.out.println("Enlarge");
                    // Mouse movement since previous calculation
                    deltaY = YPos - cursorY;
                    deltaX = XPos - cursorX;

                    normalizerX = (double) activeShape.getWidth() / (double) (activeShape.getWidth() + activeShape.getHeight());
                    normalizerY = - ((double) activeShape.getHeight() / (double) (activeShape.getWidth() + activeShape.getHeight()));

                    // Moving up increases height, down decreases height
                    activeShape.setY1((int) (activeShape.getY1() + normalizerY));
                    activeShape.setY2((int) (activeShape.getY2() - 2*normalizerY));

                    // Moving right increases width
                    activeShape.setX1((int) (activeShape.getX1() - normalizerX));
                    activeShape.setX2((int) (activeShape.getX2() + 2*normalizerX));
                    break;
                case REDUCE:
                    System.out.println("Reduce");
                    // Mouse movement since previous calculation
                    deltaY = YPos - cursorY;
                    deltaX = XPos - cursorX;

                    normalizerX = (double) activeShape.getWidth() / (double) (activeShape.getWidth() + activeShape.getHeight());
                    normalizerY = - ((double) activeShape.getHeight() / (double) (activeShape.getWidth() + activeShape.getHeight()));

                    if(activeShape.getHeight() > 10) {
                        // Moving up increases height, down decreases height
                        activeShape.setY1((int) (activeShape.getY1() - 2*normalizerY));
                        activeShape.setY2((int) (activeShape.getY2() + 2*normalizerY));
                    }
                    if(activeShape.getWidth() > 10) {
                        // Moving right increases width
                        activeShape.setX1((int) (activeShape.getX1() + 2*normalizerX));
                        activeShape.setX2((int) (activeShape.getX2() - 2*normalizerX));
                    }
                    break;
                case MOVE:
                    System.out.println("Move");
                    // Mouse-movement since previous calculation
                    deltaY = YPos - cursorY;
                    deltaX = XPos - cursorX;

                    // Moving up increases height, down decreases height
                    activeShape.setY1((int) (activeShape.getY1() + deltaY));
                    activeShape.setY2((int) (activeShape.getY2() + deltaY));

                    // Moving right increases width
                    activeShape.setX1((int) (activeShape.getX1() + deltaX));
                    activeShape.setX2((int) (activeShape.getX2() + deltaX));
                    break;
                case ROTATE:
                    // do nothing
                    break;
                default:
                    System.out.println("No Tool selected");
                    break;
            }

            // Update mouse Coords
            cursorY = YPos;
            cursorX = XPos;
            repaint();
        }
    }

    public void cursorMoved(int XPos, int YPos) {
        final int x = XPos;
        final int y = YPos;
        // Only display a hand if the cursor is hovering over the items
        boolean foundobject = false;

        if (foundobject && toolModeIndex != ToolMode.MOVE)
            this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        else
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public void keyTyped(char key) {
        System.out.println("Key Typed: " + key);
        switch (key) {
            case '1':
                int randomX = (int) (Math.random() * (this.screenWidth - 200));
                int randomY = (int) (Math.random() * (this.screenHeight - 200));
                Imagedata imagedata = new Imagedata(randomX, randomY, 200 + randomX,
                        200 + randomY);
                break;
            case '8':

                break;
            case '9':
                addLouis();
                break;
            case '0':
                this.setToolMode(ToolMode.MOVE);
                break;
            case '.':
                this.setToolMode(ToolMode.ENLARGE);
                break;
            case '\n':
                setShapeMode(ShapeMode.IMAGE);
                setToolMode(ToolMode.MOVE);
                break;
            default:
                System.out.println("Key not assigned");
                break;
        }

    }

    private void addLouis() {
        int randomX = (int) (Math.random() * (this.screenWidth - 200));
        int randomY = (int) (Math.random() * (this.screenHeight - 200));
        Imagedata imagedata = new Imagedata(randomX, randomY, 200 + randomX,
                200 + randomY);
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("res/louis.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        imagedata.setImage(image);
    }


    public void setShapeMode(ShapeMode shapeMode) {
        this.shapeModeIndex = shapeMode;
    }

    public void setToolMode(ToolMode toolMode) {
        this.toolModeIndex = toolMode;
    }

    /**********************************************************************************************/


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


        //this.setMaximumSize(new Dimension(hi, wi));
        this.setPreferredSize(new Dimension(hi, wi));
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
        
        this.toolbar = new Toolbar();
        int preferredThumbSize = 100;
        PhotoBar photoBar = new PhotoBar(images, controller);
        this.tabbedPane = new TabbedPane(photoBar);
        tabbedPane.setPreferredSize(new Dimension(2*preferredThumbSize, (int) getPreferredSize().getHeight()));
        library = photoBar.loadImages(path, preferredThumbSize);

        this.contentPanel = new ContentPanel(controller);
        controller.initialize(contentPanel, this);
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

        toolbar.setToolBarListener(controller);




        /**************************/
        // Leap STUFF
        //leapListener.setSystemPanel(systemPanel);
        leapListener.setContentPanel(this.contentPanel);
        leapListener.setScrHeight(scr_height);
        leapListener.setScrWidth(scr_width);
        leapController.addListener(leapListener);
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

    public DebugPanel getDebugPanel(){
    	return debugPanel;
    }

    public ContentPanel getContentPanel() {
        return this.contentPanel;
    }
}