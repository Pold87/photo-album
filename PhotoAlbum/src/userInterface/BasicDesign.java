package GUI;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridLayout;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.JTabbedPane;
import java.awt.Canvas;

public class BasicDesign extends JFrame {

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
	
	}
}
