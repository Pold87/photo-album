package userInterface;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BasicDesign extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{422, 0};
		gbl_contentPane.rowHeights = new int[]{243, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		/*
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		panel.add(scrollPane, gbc_scrollPane);
		*/
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(65, 105, 225));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		contentPane.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{100, 300};
		gbl_panel.rowHeights = new int[]{252, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0};
		gbl_panel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.setBackground(Color.CYAN);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.insets = new Insets(0, 0, 0, 5);
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		panel.add(tabbedPane, gbc_tabbedPane);
		VTextIcon textIcon1 = new VTextIcon(tabbedPane, "Photos");
		
		JTabbedPane tabbedPane_2 = new JTabbedPane(JTabbedPane.RIGHT);
		tabbedPane_2.setBackground(Color.PINK);
		VTextIcon textIcon2 = new VTextIcon(tabbedPane, "Backgrounds");
		tabbedPane.addTab(null, textIcon2, tabbedPane_2);
		
		JTabbedPane tabbedPane_3 = new JTabbedPane(JTabbedPane.RIGHT);
		VTextIcon textIcon3 = new VTextIcon(tabbedPane, "Frames");
		
		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.LEFT);
		//Icon graphicIcon1 = UIManager.getIcon("FileView.computerIcon");
		//CompositeIcon icon1 = new CompositeIcon(graphicIcon1, textIcon1);
		tabbedPane.addTab( null, textIcon1, tabbedPane_1);
		tabbedPane.addTab(null, textIcon3, tabbedPane_3);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 0;
		panel.add(panel_1, gbc_panel_1);
		/*
		JScrollBar scrollBar = new JScrollBar();
		GridBagConstraints gbc_scrollBar = new GridBagConstraints();
		gbc_scrollBar.fill = GridBagConstraints.BOTH;
		gbc_scrollBar.insets = new Insets(0, 0, 0, 5);
		gbc_scrollBar.gridx = 2;
		gbc_scrollBar.gridy = 0;
		panel.add(scrollBar, gbc_scrollBar);
		/*
		Canvas canvas_1 = new Canvas();
		canvas_1.setBackground(new Color(250, 235, 215));
		GridBagConstraints gbc_canvas_1 = new GridBagConstraints();
		gbc_canvas_1.anchor = GridBagConstraints.WEST;
		gbc_canvas_1.gridx = 4;
		gbc_canvas_1.gridy = 0;
		panel.add(canvas_1, gbc_canvas_1);
		
		Canvas canvas = new Canvas();
		canvas.setBackground(new Color(211, 211, 211));
		GridBagConstraints gbc_canvas = new GridBagConstraints();
		gbc_canvas.anchor = GridBagConstraints.EAST;
		gbc_canvas.gridx = 4;
		gbc_canvas.gridy = 0;
		panel.add(canvas, gbc_canvas);
		*/
		 //C:\\Users\\Dennis\\Documents\\GitHub\\photo-album\\PhotoAlbum\\src

		try{
			BufferedImage Bimage = ImageIO.read(new File("src\\picture1.jpg"));
			BufferedImage resized = resize(Bimage, 100, 100);
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
	}
}
