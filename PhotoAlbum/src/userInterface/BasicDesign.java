package userInterface;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.SwingConstants;

public class BasicDesign extends JFrame implements ComponentListener{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JToolBar buttonBar = new JToolBar();
	private JLabel photographLabel = new JLabel();
	
	//Images (names will be used as labels)
	String path = "C:\\Users\\Franziska\\Documents\\GitHub\\photo-album\\PhotoAlbum\\src\\pictures\\";
	private String[] images = { "IMG_1.jpg", "IMG_2.jpg", "IMG_3.jpg", "IMG_4.jpg", "IMG_5.jpg", "IMG_6.jpg"};
	private Photo[] pictures;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BasicDesign frame = new BasicDesign(600,600);
					frame.pack();
			        frame.setLocationRelativeTo(null);
			        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			        frame.setTitle("Photo Book Builder");
			        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
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
	public BasicDesign(int hi, int wi) {
		
		/**
		* Everything that is necessary for layout
		*/
	    int width_library = wi/6;
	    int width_book = 5*wi/6;
	    
		
		//Content Pane
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		contentPane.setLayout(gbl_contentPane);
		
		//Panel that holds everything
		JPanel panel = new JPanel();
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{width_library, width_book};
		gbl_panel.rowHeights = new int[]{hi};
		gbl_panel.columnWeights = new double[]{0.17, 0.83};
		gbl_panel.rowWeights = new double[]{1.0};
		GridBagConstraints gbl_c = new GridBagConstraints();
		gbl_c.fill = GridBagConstraints.BOTH; 
		gbl_c.gridwidth = 2;
        gbl_c.gridx = 0; 
        gbl_c.gridy = 0; 
        gbl_c.weightx = 1;
        gbl_c.weighty = 1;
		panel.setLayout(gbl_panel);
		contentPane.add(panel,gbl_c);
		
		//Tabbed Pane that holds library
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.insets = new Insets(0, 0, 0, 5);
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		panel.add(tabbedPane, gbc_tabbedPane);
		tabbedPane.addComponentListener(this);
		
		VTextIcon textIcon1 = new VTextIcon(tabbedPane, "Photos");
		JScrollPane scrollPane_1 = new JScrollPane();
		tabbedPane.addTab(null, textIcon1, scrollPane_1);
		
		JPanel panel_2 = new JPanel();
		scrollPane_1.setViewportView(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));

		VTextIcon textIcon2 = new VTextIcon(tabbedPane, "Backgrounds");
		JScrollPane scrollPane_2 = new JScrollPane();
		tabbedPane.addTab(null, textIcon2, scrollPane_2);

		VTextIcon textIcon3 = new VTextIcon(tabbedPane, "Frames");
		JScrollPane scrollPane_3 = new JScrollPane();
		tabbedPane.addTab(null, textIcon3, scrollPane_3);

		//Pane that holds canvas for book
		JPanel book_panel = new JPanel();
		GridBagConstraints gbc_book_panel = new GridBagConstraints();
		gbc_book_panel.fill = GridBagConstraints.BOTH;
		gbc_book_panel.gridx = 1;
		gbc_book_panel.gridy = 0;
		
		book_panel.setBackground(Color.GRAY);
		panel.add(book_panel, gbc_book_panel);
	
		//How image labels are displayed
        photographLabel.setVerticalTextPosition(JLabel.BOTTOM);
        photographLabel.setHorizontalTextPosition(JLabel.CENTER);
        photographLabel.setHorizontalAlignment(JLabel.CENTER);
        photographLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        buttonBar.setOrientation(SwingConstants.VERTICAL);
        
        book_panel.add(photographLabel);
        panel_2.add(buttonBar);
        
        // start the image loading 
        double thumbSize = wi*0.2;
        
        //Array containing the images as objects of class Photo
        pictures = loadimages((int) thumbSize, (int) thumbSize, thumbSize*2);
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
	                buttonBar.add(thumbButton, buttonBar.getComponentCount());
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
    private class ThumbnailAction extends AbstractAction{
		private static final long serialVersionUID = 1L;
		private ImageIcon displayPhoto;
        public ThumbnailAction(ImageIcon photo, ImageIcon thumb, String desc){
        	
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
        
       
    public void componentHidden(ComponentEvent e) {
        System.out.println(e.getComponent().getClass().getName() + " --- Hidden");
    }

    public void componentMoved(ComponentEvent e) {
        System.out.println(e.getComponent().getClass().getName() + " --- Moved");
    }

    public void componentResized(ComponentEvent e) {
    	System.out.println(e.getComponent().getClass().getName() + " --- Resized");
    	//Resizing thumbnails with window
    	int width = e.getComponent().getWidth();
    	for (int i = 0; i < buttonBar.getComponentCount(); i++) {
    		JButton button = (JButton) buttonBar.getComponent(i);
  		    BufferedImage img;
			img = this.pictures[i].getbImage();
			int w = 2*width/3;
			BufferedImage resizedImg = new BufferedImage(w, w, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = resizedImg.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.drawImage(img, 0, 0, w, w, null);
			g2.dispose();
			ImageIcon newIcon = new ImageIcon(resizedImg);
			button.setPreferredSize(new Dimension(width,width));
			button.setIcon(newIcon);
			button.revalidate();
			button.repaint();
    	}
    }

    public void componentShown(ComponentEvent e) {
        System.out.println(e.getComponent().getClass().getName() + " --- Shown");

    }
}
        

	

