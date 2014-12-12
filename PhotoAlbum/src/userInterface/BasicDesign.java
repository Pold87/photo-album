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
	//private JPanel photo_right;
	//private JPanel photo_left;
	
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
					BasicDesign frame = new BasicDesign(400,600);
					frame.pack();
			        frame.setLocationRelativeTo(null);
			        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			        frame.setTitle("Photo Book Builder");
			        //frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
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
	    
		//Content Pane
		int width_library = wi/6;
	    int width_book = 5*wi/6;
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{width_library, width_book};
		gbl_contentPane.rowHeights = new int[]{hi};
		gbl_contentPane.columnWeights = new double[]{0.17, 0.83};
		gbl_contentPane.rowWeights = new double[]{1.0};
		contentPane.setLayout(gbl_contentPane);
		
		//Tabbed Pane that holds library
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.insets = new Insets(0, 0, 0, 5);
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		contentPane.add(tabbedPane, gbc_tabbedPane);
		tabbedPane.addComponentListener(this);
		
		VTextIcon textIcon1 = new VTextIcon(tabbedPane, "Photos");
		JScrollPane scrollPane_1 = new JScrollPane();
		tabbedPane.addTab(null, textIcon1, scrollPane_1);
		
		JPanel panel_2 = new JPanel(new BorderLayout());
		scrollPane_1.setViewportView(panel_2);
       
		VTextIcon textIcon2 = new VTextIcon(tabbedPane, "Backgrounds");
		JScrollPane scrollPane_2 = new JScrollPane();
		tabbedPane.addTab(null, textIcon2, scrollPane_2);

		VTextIcon textIcon3 = new VTextIcon(tabbedPane, "Frames");
		JScrollPane scrollPane_3 = new JScrollPane();
		tabbedPane.addTab(null, textIcon3, scrollPane_3);

		buttonBar.setOrientation(SwingConstants.VERTICAL);
        buttonBar.setMargin(new Insets(0,0,0,0));
        panel_2.add(buttonBar);
        
		//Pane that holds canvas for book
		JPanel book_panel = new JPanel();
		GridBagLayout book_layout = new GridBagLayout();
		book_panel.setLayout(book_layout);
		book_layout.columnWidths = new int[]{width_book/10, (int) (width_book*0.4), 2*width_book/5, width_book/10};
		book_layout.rowHeights = new int[]{hi/6, 2*hi/3, hi/6};
		book_layout.columnWeights = new double[]{0.1, 0.4, 0.4, 0.1};
		book_layout.rowWeights = new double[]{0.17, 0.66, 0.17};
		GridBagConstraints book_constraints = new GridBagConstraints();
		book_constraints.fill = GridBagConstraints.BOTH;
		book_constraints.gridx = 1;
		book_constraints.gridy = 0;
		book_panel.setBackground(Color.GRAY);
		contentPane.add(book_panel, book_constraints);
	
		//Left page of photobook
		JPanel photo_left = new JPanel();
		GridBagConstraints gbc_photo_left = new GridBagConstraints();
		gbc_photo_left.fill = GridBagConstraints.BOTH;
		gbc_photo_left.gridx = 1;
		gbc_photo_left.gridy = 1;
		photo_left.setBackground(Color.WHITE);
		//photo_left.setLayout(null);
		book_panel.add(photo_left, gbc_photo_left);
		
		//Right page of photobook
		JPanel photo_right = new JPanel();
		GridBagConstraints gbc_photo_right = new GridBagConstraints();
		gbc_photo_right.fill = GridBagConstraints.BOTH;
		gbc_photo_right.gridx = 2;
		gbc_photo_right.gridy = 1;
		photo_right.setBackground(Color.LIGHT_GRAY);
		//photo_right.setLayout(null);
		book_panel.add(photo_right, gbc_photo_right);
		
	
		//How image labels are displayed
        photographLabel.setVerticalTextPosition(JLabel.BOTTOM);
        photographLabel.setHorizontalTextPosition(JLabel.CENTER);
        photographLabel.setHorizontalAlignment(JLabel.CENTER);
        photo_right.add(photographLabel);
        
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
        

	

