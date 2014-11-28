package userInterface;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLayeredPane;


public class BasicDesign extends JFrame {
	public BasicDesign() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{137, 287, 0};
		gridBagLayout.rowHeights = new int[]{262, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		getContentPane().add(panel, gbc_panel);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		panel.add(tabbedPane);
		
		VTextIcon textIcon1 = new VTextIcon(tabbedPane, "Photos");
		JScrollPane scrollPane_1 = new JScrollPane();
		tabbedPane.addTab(null, textIcon1, scrollPane_1);

		VTextIcon textIcon2 = new VTextIcon(tabbedPane, "Backgrounds");
		JScrollPane scrollPane_2 = new JScrollPane();
		tabbedPane.addTab(null, textIcon2, scrollPane_2);
		
		VTextIcon textIcon3 = new VTextIcon(tabbedPane, "Frames");
		JScrollPane scrollPane_3 = new JScrollPane();
		tabbedPane.addTab(null, textIcon3, scrollPane_3);
		
		//JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.LEFT);
		//tabbedPane.addTab( null, textIcon1, tabbedPane_1);
		//tabbedPane_2 = new JTabbedPane(JTabbedPane.LEFT);
		//tabbedPane.addTab(null, textIcon2, tabbedPane_2);
		//JTabbedPane tabbedPane_3 = new JTabbedPane(JTabbedPane.LEFT);
		//tabbedPane.addTab(null, textIcon3, tabbedPane_3);
		
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setBackground(Color.CYAN);
		GridBagConstraints gbc_layeredPane = new GridBagConstraints();
		gbc_layeredPane.fill = GridBagConstraints.BOTH;
		gbc_layeredPane.gridx = 1;
		gbc_layeredPane.gridy = 0;
		getContentPane().add(layeredPane, gbc_layeredPane);
		layeredPane.setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel panel_1 = new JPanel();
		layeredPane.add(panel_1);
		
	}

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
	/*public BasicDesign() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(65, 105, 225));
		contentPane.add(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(28, 0, 86, 252);
		panel.add(scrollPane);
		
		JScrollBar scrollBar = new JScrollBar();
		scrollBar.setBounds(113, 0, 17, 252);
		panel.add(scrollBar);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.RIGHT);
		tabbedPane.setBounds(0, 0, 33, 252);
		panel.add(tabbedPane);
		
		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.RIGHT);
		VTextIcon textIcon1 = new VTextIcon(tabbedPane, "Photos");
		//Icon graphicIcon1 = UIManager.getIcon("FileView.computerIcon");
		//CompositeIcon icon1 = new CompositeIcon(graphicIcon1, textIcon1);
		tabbedPane.addTab( null, textIcon1, tabbedPane_1);
		
		JTabbedPane tabbedPane_2 = new JTabbedPane(JTabbedPane.RIGHT);
		VTextIcon textIcon2 = new VTextIcon(tabbedPane, "Backgrounds");
		tabbedPane.addTab(null, textIcon2, tabbedPane_2);
		
		JTabbedPane tabbedPane_3 = new JTabbedPane(JTabbedPane.RIGHT);
		VTextIcon textIcon3 = new VTextIcon(tabbedPane, "Frames");
		tabbedPane.addTab(null, textIcon3, tabbedPane_3);
		
		Canvas canvas = new Canvas();
		canvas.setBackground(new Color(211, 211, 211));
		canvas.setBounds(282, 76, 113, 126);
		panel.add(canvas);
		
		Canvas canvas_1 = new Canvas();
		canvas_1.setBackground(new Color(250, 235, 215));
		canvas_1.setBounds(163, 76, 120, 126);
		panel.add(canvas_1);
	}*/
}