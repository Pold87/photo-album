package userInterface;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.awt.GridLayout;
import javax.swing.SwingConstants;

public class BasicDesign extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	    
	private String imagedir = "C:\\";
	
	private MissingIcon placeholderIcon = new MissingIcon();
	private JToolBar buttonBar = new JToolBar();
	private JLabel photographLabel = new JLabel();
	
	//Images (names will be used as labels)
	    private String[] images = { "IMG_1.jpg", "IMG_2.jpg",
	    "IMG_3.jpg", "IMG_4.jpg", "IMG_5.jpg"};
	    

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BasicDesign frame = new BasicDesign();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	/**
	 * Create the frame.
	 */
	public BasicDesign() {
		
		/**
		* Everything that is necessary for layout
		*/
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Photo Book Builder");
	    setSize(600, 400);
	    setLocationRelativeTo(null);
		
		//Content Pane
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{422, 0};
		gbl_contentPane.rowHeights = new int[]{243, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		//Panel that holds everything
		JPanel panel = new JPanel();
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{100, 300};
		gbl_panel.rowHeights = new int[]{252, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0};
		gbl_panel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		contentPane.add(panel);
		
		//Tabbed Pane that holds library
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.insets = new Insets(0, 0, 0, 5);
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		panel.add(tabbedPane, gbc_tabbedPane);
		VTextIcon textIcon1 = new VTextIcon(tabbedPane, "Photos");
		JScrollPane scrollPane_1 = new JScrollPane();
		tabbedPane.addTab(null, textIcon1, scrollPane_1);

		VTextIcon textIcon2 = new VTextIcon(tabbedPane, "Backgrounds");
		JScrollPane scrollPane_2 = new JScrollPane();
		tabbedPane.addTab(null, textIcon2, scrollPane_2);

		VTextIcon textIcon3 = new VTextIcon(tabbedPane, "Frames");
		JScrollPane scrollPane_3 = new JScrollPane();
		tabbedPane.addTab(null, textIcon3, scrollPane_3);

		//Pane that holds canvas for book
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 0;
		panel.add(panel_1, gbc_panel_1);
	
		/**
		* Everything that is necessary for images
		*/
		//How image labels are displayed
        photographLabel.setVerticalTextPosition(JLabel.BOTTOM);
        photographLabel.setHorizontalTextPosition(JLabel.CENTER);
        photographLabel.setHorizontalAlignment(JLabel.CENTER);
        photographLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        buttonBar.add(Box.createGlue());
        buttonBar.add(Box.createGlue());
        
        buttonBar.setOrientation(SwingConstants.VERTICAL);
        
        buttonBar.add(photographLabel);
        scrollPane_1.add(buttonBar);
        
        // start the image loading 
        loadimages();
	}
	

    private void loadimages() {
            for (int i = 0; i < images.length; i++) {
            	String pic = images[i];
                ImageIcon icon;
                BufferedImage bImage;
				try {
					bImage = ImageIO.read(new File(imagedir + pic));
					icon = new ImageIcon(bImage);
	                ImageIcon thumbnailIcon = new ImageIcon(getScaledImage(bImage, 32, 32));
	                
	                ThumbnailAction thumbAction;
	                thumbAction = new ThumbnailAction(icon, thumbnailIcon, pic);
	                JButton thumbButton = new JButton(thumbAction);
	                buttonBar.add(thumbButton, buttonBar.getComponentCount()-1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
            }
    };
    
    /**
     * Resizes an image using a Graphics2D object backed by a BufferedImage.
     * @param srcImg - source image to scale
     * @param w - desired width
     * @param h - desired height
     * @return - the new resized image
     */
    private Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }
    
    /**
     * Action class that shows the image specified in it's constructor.
     */
    private class ThumbnailAction extends AbstractAction{
        
        /**
         *The icon if the full image we want to display.
         */
        private Icon displayPhoto;
        
        /**
         * @param Icon - The full size photo to show in the button.
         * @param Icon - The thumbnail to show in the button.
         * @param String - The descriptioon of the icon.
         */
        public ThumbnailAction(Icon photo, Icon thumb, String desc){
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
            photographLabel.setIcon(displayPhoto);
            setTitle("Icon Demo: " + getValue(SHORT_DESCRIPTION).toString());
        }
    }
}
        
	/*try{
			
			BufferedImage bImage = ImageIO.read(new File("C:\\picture1.jpg"));
			BufferedImage resized = resize(bImage, 100, 100);
			ImageIcon image = new ImageIcon(resized);
			panel_1.setLayout(new BorderLayout(0, 0));
			JLabel label  = new JLabel ("Trololol", image, JLabel.CENTER);
			label.setPreferredSize(new Dimension(200, 200));
			panel_1.add(label, BorderLayout.CENTER);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static BufferedImage resize(BufferedImage image, int width, int height) {
	    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
	    Graphics2D g2d = (Graphics2D) bi.createGraphics();
	    g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
	    g2d.drawImage(image, 0, 0, width, height, null);
	    g2d.dispose();
	    return bi;
	}}*/
	

