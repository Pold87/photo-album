package main.java.userInterface;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class ModeSelector extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JTextField txtParticipantNumber;
	int participantNumber = 0;
	
	public ModeSelector() {
		txtParticipantNumber = new JTextField();
		txtParticipantNumber.setText("Participant Number");
		getContentPane().add(txtParticipantNumber, BorderLayout.NORTH);
		txtParticipantNumber.setColumns(10);
		
		JButton btnTrain = new JButton("Train");
		getContentPane().add(btnTrain, BorderLayout.WEST);
		btnTrain.addActionListener(this);
		
		JButton btnTest1 = new JButton("Test Leap & Voice");
		getContentPane().add(btnTest1, BorderLayout.CENTER);
		btnTest1.addActionListener(this);
		
		JButton btnTest2 = new JButton("Test Mouse");
		getContentPane().add(btnTest2, BorderLayout.EAST);
		btnTest2.addActionListener(this);
		
	    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    setVisible(true);
	    pack();
	    this.setLocation(700, 350);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try{
			participantNumber = Integer.parseInt(txtParticipantNumber.getText());
		}catch(Exception ex){
			
		}
		if(e.getActionCommand().equalsIgnoreCase("Train"))
			App.modeSelected(participantNumber, App.TestMode.Train);
		else if(e.getActionCommand().equalsIgnoreCase("Test Mouse"))
			App.modeSelected(participantNumber, App.TestMode.TestMouse);
		else
			App.modeSelected(participantNumber, App.TestMode.TestLeap);
		this.dispose();
	}
}
