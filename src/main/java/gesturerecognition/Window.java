package main.java.gesturerecognition;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import com.leapmotion.leap.*;

@SuppressWarnings("serial")
public class Window extends JFrame {

	// Members
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int scr_width = screenSize.width;
	private int scr_height = screenSize.height;
	public Controller leapController = new Controller();
	public LeapListener leapListener = new LeapListener();
	
	
	// Methods
	public Window()  {
		super();
		
		super.setTitle("MMI Practicum - Aarle, van & Scheepers");
		super.setSize(new Dimension(scr_width, scr_height));
		// Make sure the window appears in the middle of your screen
		super.setLocationRelativeTo(null);
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		// Chooses a certain layout type for the elements in this frame
		super.getContentPane().setLayout(new BorderLayout());
		System.out.println("Window created");
		
		// Panels
		DrawingPanel contentPanel = new DrawingPanel(scr_width, scr_height -30);
		SystemPanel systemPanel = new SystemPanel(this);
		Dimension systemdim = new Dimension( 200, (int)systemPanel.getSize().getHeight());
		systemPanel.setPreferredSize(systemdim);
		
		// Leap
		//leapListener.setSystemPanel(systemPanel);
		leapListener.setContentPanel(contentPanel);
		leapListener.setScrHeight(scr_height);
		leapListener.setScrWidth(scr_width);
		leapController.addListener(leapListener);

		super.getContentPane().add(contentPanel, BorderLayout.CENTER);
		super.getContentPane().add(systemPanel, BorderLayout.LINE_END);
		

		super.setUndecorated(true);
		super.setVisible(true);
		
		contentPanel.setFocusable(true);
		contentPanel.requestFocusInWindow();
		
		System.out.println("Window end");
	}


	public void disconnectLeapMotion() {
		if ( leapController.isConnected() )
			leapController.removeListener(leapListener);
		
		System.out.println("Leap Motion removed Listener");		
	}
}
